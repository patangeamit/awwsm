package com.example;

import java.lang.StackWalker.Option;
import java.util.Optional;
import com.example.Token.TokenType;

public class Parser {
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
        Optional<Node> var = parseAddSub();
        if (var.isPresent()) {
            return Optional.of((NodeExpr) var.get());
        }
        return Optional.empty();
    }

    Optional<Node> parseAddSub() {
        Node left = parseFactor().get();
        while (peekToken().isPresent()
                && (peekToken().get().type == TokenType.PLUS | peekToken().get().type == TokenType.MINUS)) {
            Token operator = consumeToken();
            Node right = parseFactor().get();
            left = new NodeExpr(new NodeExprBin(operator, left, right));
        }
        if (left instanceof NodeExpr)
            return Optional.of(left);
        return Optional.of(new NodeExpr(left));
    }

    Optional<Node> parseFactor() {
        Node left = parseTerm().get();
        while (peekToken().isPresent()
                && (peekToken().get().type == TokenType.STAR | peekToken().get().type == TokenType.FORWARD_SLASH)) {
            Token operator = consumeToken();
            Node right = parseTerm().get();
            if (!(left instanceof NodeExpr)) {
                left = new NodeExpr(left);
            }
            if (!(right instanceof NodeExpr)) {
                right = new NodeExpr(right);
            }
            left = new NodeExpr(new NodeExprBin(operator, left, right));
        }
        if (left instanceof NodeExpr)
            return Optional.of(left);
        return Optional.of(new NodeExpr(left));
    }

    Optional<Node> parseTerm() {
        if (peekToken().isPresent() && peekToken().get().type == TokenType.OPEN_PREN) {
            consumeToken(); // consume (
            Optional<Node> expr = parseAddSub();
            if (expr.isPresent() && peekToken().get().type == TokenType.CLOSE_PREN) {
                consumeToken(); // consume )
                return expr;
            } else {
                return Optional.empty();
            }
        } else if (peekToken().isPresent() && peekToken().get().type == TokenType.INDENTIFIER) {
            Node expTermIdnt = new NodeExprTerm(new NodeExprTermIdnt(consumeToken()));
            return Optional.of(expTermIdnt);
        } else if (peekToken().isPresent() && peekToken().get().type == TokenType.INTEGER) {
            Node expTermInt = new NodeExprTerm(new NodeExprTermInt(consumeToken()));
            return Optional.of(expTermInt);
        } else {
            return Optional.empty();
        }
    }
    // Optional<NodeExpr> parseExpr() {
    // Optional<NodeExprBin> exprBin = parseExprBin();
    // if (exprBin.isPresent()) {
    // return Optional.of(new NodeExpr(exprBin.get()));
    // } else {
    // // int
    // if (peekToken().isPresent() && peekToken().get().type == TokenType.INTEGER) {

    // NodeExpr expr = new NodeExpr(new NodeExprInt(consumeToken(3)));
    // return Optional.of(expr);
    // }
    // // identifier
    // else if (peekToken().isPresent() && peekToken().get().type ==
    // TokenType.INDENTIFIER) {
    // NodeExpr expr = new NodeExpr(new NodeExprIdnt(consumeToken()));
    // return Optional.of(expr);
    // }
    // // fail
    // else {
    // return Optional.empty(); // err: empty expression
    // }
    // }
    // }

    // Optional<NodeExprBin> parseExprBin() {
    // if (peekToken().isPresent() && peekToken(1).isPresent()) {
    // if (peekToken(1).get().type == TokenType.PLUS) {
    // // consume lhs
    // Token lhsToken = consumeToken();
    // NodeExpr lhs;
    // if (lhsToken.type == TokenType.INDENTIFIER) {
    // lhs = new NodeExpr(new NodeExprIdnt(lhsToken));

    // } else if (lhsToken.type == TokenType.INTEGER) {
    // lhs = new NodeExpr(new NodeExprInt(lhsToken));
    // } else {
    // lhs = null;
    // return Optional.empty(); // err: unknown token in expr
    // }
    // // consume +
    // consumeToken();
    // Optional<NodeExpr> rhs = parseExpr();
    // if (rhs.isPresent()) {
    // NodeExprBin exprBin = new NodeExprBin(new NodeExprBinAdd(lhs, rhs.get()));
    // } else {
    // return Optional.empty();
    // }
    // }
    // }
    // }

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