package com.example;

import java.io.BufferedReader;
import java.io.IOException;

public class Tokenizer {
    Token[] tokens = new Token[100];
    int token_index = 0;
    public Token[] tokenize(BufferedReader br) throws IOException{


        while (true) {
            int charInt = br.read();
            char c = (char) charInt;
            if (charInt== -1){
                break;
            }
            if (Character.isAlphabetic(c)){
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                // return 0;
                while (true){
                    charInt = br.read();
                    c = (char) charInt;
                    if (!Character.isAlphabetic((char) charInt)){
                        break;
                    }else{
                        sb.append(c);
                    }
                }
                Token t = new Token("literal", sb.toString()); 
                tokens[token_index] = t;
                token_index ++;
                // System.out.println("word: "+ sb.toString());
            }
             if(Character.isDigit(c)){
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                while (true){
                    charInt = br.read();
                    c = (char) charInt;
                    if(!Character.isDigit((char) charInt)){
                        break;
                    }else{
                        sb.append(c);
                    }
                }
                Token t = new Token("integer", sb.toString()); 
                tokens[token_index] = t;
                token_index ++;
                // System.out.println("digit: " + sb.toString());
            }
             if(c == ';'){
                 Token t = new Token("semicolon"); 
                 tokens[token_index] = t;
                 token_index ++;
                //  System.out.println("semicolon");    
            }
            else if (Character.isWhitespace(c)){
                // System.out.println("whitespace");
            }
        }
        return  this.tokens;
    }
    public Token token_prog(BufferedReader br) throws IOException{
        Token head = new Token("PROGRAM");
        Token tail = head;
        while (true){
            int charInt = br.read();
            char c = (char) charInt;
            if (charInt== -1){
                break;
            }
            if (Character.isAlphabetic(c)){
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                // return 0;
                while (true){
                    charInt = br.read();
                    c = (char) charInt;
                    if (!Character.isAlphabetic((char) charInt)){
                        break;
                    }else{
                        sb.append(c);
                    }
                }
                Token t = new Token("literal", sb.toString()); 
                tail.next = t;
                tail = t;
            }
            if(Character.isDigit(c)){
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                while (true){
                    charInt = br.read();
                    c = (char) charInt;
                    if(!Character.isDigit((char) charInt)){
                        break;
                    }else{
                        sb.append(c);
                    }
                }
                Token t = new Token("integer", sb.toString()); 
                tail.next = t;
                tail = t;
            }
            if(c == ';'){
                Token t = new Token("semicolon"); 
                tail.next = t;
                tail = t;
               //  System.out.println("semicolon");    
            }

            if(c == '='){
                Token t = new Token("assignment"); 
                tail.next = t;
                tail = t;
            }
            if(c == '('){
                Token t = new Token("open_pren");
                tail.next = t;
                tail = t;
            }
            if(c == ')'){
                Token t = new Token("close_pren");
                tail.next = t;
                tail = t;
            }
            else if (Character.isWhitespace(c)){
               // System.out.println("whitespace");
            }
        }
        Token end = new Token("END");
        tail.next = end;
        return head;
    }
}