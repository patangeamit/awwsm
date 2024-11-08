package com.example;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.Token.TokenType;

public class Parser {
    public interface Node {
    }

    public class NodeProg implements Node {
        List<NodeStmt> statements = new ArrayList<>();

        NodeProg() {
        }

        NodeProg(List<NodeStmt> s) {
            statements = s;
        }
    }

    public class NodeStmt implements Node {
        Node var; // can be NodeStmtExit or NodeStmtLet

        NodeStmt(Node var) {
            this.var = var;
        }
    }

    public class NodeStmtExit implements Node {
        // exit <expr> ;
        NodeExpr expr;

        NodeStmtExit(NodeExpr e) {
            expr = e;
        }
    }

    public class NodeStmtLet implements Node {
        // let <idnt> = <expr> ;
        Token idnt;
        NodeExpr expr;

        NodeStmtLet(Token idnt, NodeExpr expr) {
            this.idnt = idnt;
            this.expr = expr;
        }
    }

    public class NodeExpr implements Node {
        // <var>
        Node var; // can be NodeExprIdnt or NodeExprInt

        NodeExpr(Node var) {
            this.var = var;
        }
    }

    public class NodeExprInt implements Node {
        // <tokenInt>
        Token tokenInt;

        NodeExprInt(Token t) {
            tokenInt = t;
        }
    }

    public class NodeExprIdnt implements Node {
        // <tokenIdnt>
        Token tokenIdnt;

        NodeExprIdnt(Token t) {
            tokenIdnt = t;
        }

    }

    Token curr;

    Optional<NodeProg> parseProg(Token head) {
        curr = head;
        System.out.println(head.type);
        // consume the first token which will be PROGRAM token
        consumeToken(1);
        NodeProg program = new NodeProg();
        while (peekToken().isPresent()) {
            // return expr ;
            if (peekToken().get().type == TokenType.RETURN) {
                // consume return token
                consumeToken(2);
                Optional<NodeExpr> expr = parseExpr();
                if (expr.isEmpty())
                    return Optional.empty(); // err: invalid expression
                if (peekToken().isPresent() & peekToken().get().type == TokenType.SEMICOLON) {
                    // consume ;
                    consumeToken();
                    program.statements.add(new NodeStmt(new NodeStmtExit(expr.get())));
                } else
                    return Optional.empty(); // err: semicolon missing
            }
            // let idnt = expr ;
            else if (peekToken().get().type == TokenType.LET) {
                // consume let
                consumeToken();
                if (peekToken().isPresent()
                        && peekToken().get().type == TokenType.INDENTIFIER
                        && peekToken(1).isPresent()
                        && peekToken(1).get().type == TokenType.ASSIGNMENT) {
                    // consume idnt
                    Token idnt = consumeToken();
                    // consume =
                    consumeToken();
                    Optional<NodeExpr> expr = parseExpr();
                    if (expr.isEmpty())
                        return Optional.empty(); // err: invalid expression
                    if (peekToken().isPresent() && peekToken().get().type == TokenType.SEMICOLON) {
                        // consume ;
                        consumeToken();
                        program.statements.add(new NodeStmt(new NodeStmtLet(idnt, expr.get())));
                    } else
                        return Optional.empty(); // err: semicolon missing
                } else {
                    return Optional.empty();
                }
            } else
                break;
        }
        return Optional.of(program);
    }

    Optional<NodeExpr> parseExpr() {
        // int
        if (peekToken().isPresent() && peekToken().get().type == TokenType.INTEGER) {

            NodeExpr expr = new NodeExpr(new NodeExprInt(consumeToken(3)));
            return Optional.of(expr);
        }
        // identifier
        else if (peekToken().isPresent() && peekToken().get().type == TokenType.INDENTIFIER) {
            NodeExpr expr = new NodeExpr(new NodeExprIdnt(consumeToken()));
            return Optional.of(expr);
        }
        // fail
        else {
            return Optional.empty(); // err: empty expression
        }
    }

    Optional<Token> peekToken() {
        return peekToken(0);
    }

    Optional<Token> peekToken(int offset) {
        Token temp = curr;
        while (offset > 0) {
            if (temp.next != null)
                temp = temp.next;
            else
                return Optional.empty();
            offset = offset - 1;
        }
        if (curr != null)
            return Optional.of(temp);
        return Optional.empty();
    }

    Token consumeToken() {
        return consumeToken(0);
    }

    Token consumeToken(int i) {
        Token temp = curr;
        curr = curr.next;
        return temp;

    }

}
