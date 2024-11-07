package com.example;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Parser {
    public interface Node {
    }
    public class NodeExpr implements Node{
        Token tokenINT;
    
    }
    public class NodeExit implements Node{
        NodeExpr exprNode = new NodeExpr();
    }
    Token curr;


    Optional<NodeExit> parse(Token head){
        this.curr = head;
        
        NodeExit nodeExit = new NodeExit();
        while(peekToken().isPresent()){
            if (peekToken().get().type == "END") break;
            if(peekToken().get().type == "RETURN"){
                consumeToken();
                Optional<NodeExpr> o = parseExpr();
                if (o.isPresent()){
                    nodeExit.exprNode = o.get();
                }else{
                    System.out.println("invalid syntax! wrong expression");
                    return Optional.empty();
                }
                if(peekToken().isPresent() & peekToken().get().type == "SEMICOLON"){
                    consumeToken();
                }else{
                    System.out.println("invalid syntax! missing ;");
                    return Optional.empty();
                } 
            }
            consumeToken();
        }
        return Optional.of(nodeExit);
    }

    Optional<NodeExpr> parseExpr(){
        if(peekToken().isPresent() && peekToken().get().type == "INTEGER"){
            NodeExpr nodeExpr = new NodeExpr();
            Token t = consumeToken();
            nodeExpr.tokenINT = t;
            System.out.println(nodeExpr.tokenINT.type);
            return Optional.of(nodeExpr);
        }
        return Optional.empty();
    }

    Optional<Token> peekToken() { return peekToken(1);}
    Optional<Token> peekToken(int offset){
        if (curr.type == "END") return Optional.empty();
        return Optional.of(curr.next);
    }
    Token consumeToken (){
        curr = curr.next;
        return curr;
    }
}

