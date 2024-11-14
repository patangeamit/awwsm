package com.example;

import java.util.HashMap;

import com.example.Token.TokenType;

public class Generator {
    StringBuilder assembly = new StringBuilder();
    int stack_size = 0;
    HashMap<String, Integer> variables = new HashMap<String, Integer>();

    public enum STMT {
        ret, let
    }

    public void push(String reg) {
        assembly.append("    push " + reg + "\n");
        stack_size++;
    }

    public void pop(String reg) {
        assembly.append("    pop " + reg + "\n");
        stack_size--;
    }

    public String generate(NodeProg nodeRoot) {
        assembly.append("global _start\n_start:\n");
        for (NodeStmt nodeStmt : nodeRoot.statements) {
            gen_stmt(nodeStmt);
        }
        // in case of no return statement
        assembly.append("    mov rax, 60\n");
        assembly.append("    mov rdi, 0\n");
        assembly.append("    syscall\n");
        return assembly.toString();
    }

    void gen_stmt(NodeStmt stmt) {
        if (stmt.var instanceof NodeStmtExit) {
            NodeStmtExit stmtExit = (NodeStmtExit) stmt.var;
            gen_expr((NodeExpr) stmtExit.expr);
            assembly.append("    mov rax, 60\n");
            pop("rdi");
            assembly.append("    syscall\n");
        } else if (stmt.var instanceof NodeStmtLet) {
            // only variable values are actually stored in program's stack during runtime.
            // the identifiers are stored during compile time in a hashmap which locates
            // the identifier with it's position in the program stack.
            NodeStmtLet stmtLet = (NodeStmtLet) stmt.var;
            // check if the variable is already stored in stack
            if (variables.containsKey(stmtLet.idnt.value)) {
                // err: variable already used
                System.out.println("Err: Variable '" + stmtLet.idnt.value + "' already used");
            } else {
                variables.put(stmtLet.idnt.value, stack_size);
                gen_expr(stmtLet.expr);
            }
        }
    }

    void gen_expr(NodeExpr expr) {
        if (expr.var instanceof NodeExprTerm) {
            gen_term((NodeExprTerm) expr.var);
        } else if (expr.var instanceof NodeExprBin) {
            gen_binary((NodeExprBin) expr.var);
        }
    }

    void gen_binary(NodeExprBin exprBin) {
        gen_expr((NodeExpr) exprBin.right);
        gen_expr((NodeExpr) exprBin.left);
        if (exprBin.tokenOp.type == TokenType.PLUS) {
            pop("rax");
            pop("rbx");
            assembly.append("    add rax, rbx\n");
            push("rax");
        } else if (exprBin.tokenOp.type == TokenType.MINUS) {
            pop("rax");
            pop("rbx");
            assembly.append("    sub rax, rbx\n");
            push("rax");
        } else if (exprBin.tokenOp.type == TokenType.STAR) {
            pop("rax");
            pop("rbx");
            assembly.append("    mul rbx\n");
            push("rax");
        } else if (exprBin.tokenOp.type == TokenType.FORWARD_SLASH) {
            pop("rax");
            pop("rbx");
            assembly.append("    div rbx\n");
            push("rax");
        }
    }

    void gen_term(NodeExprTerm exprTerm) {
        // pushes the value (or a variable's value) on top of the stack
        if (exprTerm.var instanceof NodeExprTermInt) {
            NodeExprTermInt exprInt = (NodeExprTermInt) exprTerm.var;
            assembly.append("    mov rax, " + exprInt.tokenInt.value + "\n");
            push("rax");
        } else if (exprTerm.var instanceof NodeExprTermIdnt) {
            // expr is an identifier
            // find the identifier's value's stack position from the HashMap, calculates the
            // difference between the stack size and multiplies it with 8 (because a stack
            // element is 8bytes long)
            NodeExprTermIdnt exprIdnt = (NodeExprTermIdnt) exprTerm.var;
            if (!variables.containsKey(exprIdnt.tokenIdnt.value)) {
                System.out.println("Err: Undeclared variable '" + exprIdnt.tokenIdnt.value + "'");
                // err: undeclared identifier
            } else {
                int offset = 8 * (stack_size - variables.get(exprIdnt.tokenIdnt.value) - 1);
                push("QWORD [rsp + " + offset + " ]\n");
            }
        }
    }

}