package com.example;

class Token {
    TokenType type;
    String value;
    Token next = null;

    void print() {
        System.out.println("Token: " + type + "\t\t " + value);
    }

    public Token(TokenType type) {
        this(type, null);
    }

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
        print();
    }

    enum TokenType {
        UNKNOWN,
        PROGRAM,
        END,
        INDENTIFIER,
        RETURN,
        LET,
        INTEGER,
        SEMICOLON,
        ASSIGNMENT,
        OPEN_PREN,
        CLOSE_PREN,
        PLUS,
        MINUS,
        STAR,
        FORWARD_SLASH
    }

    public Token(String keyword) {
        switch (keyword) {
            case "ret":
                type = TokenType.RETURN;
                break;
            case "let":
                type = TokenType.LET;
                break;
            default:
                type = TokenType.INDENTIFIER;
                value = keyword;
        }
        print();
    }
}