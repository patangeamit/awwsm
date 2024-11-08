package com.example;

import com.example.Parser.Node;
import com.example.Parser.NodeExpr;
import com.example.Parser.NodeExprIdnt;
import com.example.Parser.NodeExprInt;
import com.example.Parser.NodeProg;
import com.example.Parser.NodeStmt;
import com.example.Parser.NodeStmtExit;
import com.example.Parser;

public class Generator {
    StringBuilder assembly = new StringBuilder();

    public enum STMT {
        ret, let
    }

    void gen_expr(NodeExpr expr) {
        if (expr.var instanceof NodeExprInt) {
            NodeExprInt exprInt = (NodeExprInt) expr.var;
            assembly.append("    mov rax, " + exprInt.tokenInt.value + "\n");
            assembly.append("    push rax\n");
        }
    }

    void gen_stmt(NodeStmt stmt) {
        if (stmt.var instanceof NodeStmtExit) {
            NodeStmtExit stmtExit = (NodeStmtExit) stmt.var;
            gen_expr((NodeExpr) stmtExit.expr);
            assembly.append("    mov rax, 60\n");
            assembly.append("    pop rdi\n");
            assembly.append("    syscall\n");
        }
    }

    public String generate(NodeProg nodeRoot) {
        // Node exprInt = nodeRoot.statements.get(0).var;
        assembly.append("global _start\n_start:\n");
        if (!nodeRoot.statements.isEmpty()) {
            NodeStmt stmt = (NodeStmt) nodeRoot.statements.get(0);
            gen_stmt(stmt);
        }
        assembly.append("    mov rax, 60\n");
        assembly.append("    mov rdi, 0\n");
        assembly.append("    syscall\n");
        return assembly.toString();
    }
}