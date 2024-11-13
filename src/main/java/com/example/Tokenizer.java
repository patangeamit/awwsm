package com.example;

import java.io.BufferedReader;
import java.io.IOException;

import com.example.Token.TokenType;

public class Tokenizer {
    Token[] tokens = new Token[100];
    int token_index = 0;

    public Token token_prog(BufferedReader br) throws IOException {
        Token head = new Token(TokenType.PROGRAM);
        Token tail = head;
        while (true) {
            int charInt = br.read();
            char c = (char) charInt;
            if (charInt == -1) {
                break;
            }
            if (Character.isAlphabetic(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                // return 0;
                while (true) {
                    charInt = br.read();
                    c = (char) charInt;
                    if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                        break;
                    } else {
                        sb.append(c);
                    }
                }
                Token t = new Token(sb.toString());
                tail.next = t;
                tail = t;
            }
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                while (true) {
                    charInt = br.read();
                    c = (char) charInt;
                    if (!Character.isDigit((char) charInt)) {
                        break;
                    } else {
                        sb.append(c);
                    }
                }
                Token t = new Token(TokenType.INTEGER, sb.toString());
                tail.next = t;
                tail = t;
            }
            if (c == ';') {
                Token t = new Token(TokenType.SEMICOLON);
                tail.next = t;
                tail = t;
            }

            if (c == '=') {
                Token t = new Token(TokenType.ASSIGNMENT);
                tail.next = t;
                tail = t;
            }
            if (c == '(') {
                Token t = new Token(TokenType.OPEN_PREN);
                tail.next = t;
                tail = t;
            }
            if (c == ')') {
                Token t = new Token(TokenType.CLOSE_PREN);
                tail.next = t;
                tail = t;
            }
            if (c == '+') {
                Token t = new Token(TokenType.PLUS);
                tail.next = t;
                tail = t;
            }
            if (c == '-') {
                Token t = new Token(TokenType.MINUS);
                tail.next = t;
                tail = t;
            }
            if (c == '*') {
                Token t = new Token(TokenType.STAR);
                tail.next = t;
                tail = t;
            }
            if (c == '/') {
                Token t = new Token(TokenType.FORWARD_SLASH);
                tail.next = t;
                tail = t;
            }
            if (Character.isWhitespace(c)) {
                // System.out.println("whitespace");
            }
        }
        return head;
    }
}