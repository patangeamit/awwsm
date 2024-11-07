package com.example;

class Token{
    String type;
    int value = 0;
    Token next = null;
    public Token(String type){
        this(type,"0");
    }
    public Token(String type, String value){
        if (type == "literal"){
            switch(value){
                case "ret":
                    this.type = "RETURN";
                    break;
                case "let":
                    this.type = "LET";
                    break;
                default:
                    this.type = "INDENTIFIER";
                    
                    // this.value = value;
            }
        }
        else if(type == "integer"){
            this.type = "INTEGER";
            this.value = Integer.parseInt(value);
        }
        else if(type == "semicolon"){
            this.type = "SEMICOLON";
        }
        else if(type == "assignment"){
            this.type = "ASSIGNMENT";
        }else if(type == "open_pren"){
            this.type = "OPEN_PREN";
        }else if(type == "close_pren"){
            this.type = "CLOSE_PREN";
        }
        else{
            this.type = type.toUpperCase();
            // this.value =  value;
        }
        System.out.println("TOKEN GEN: "+ this.type + "\t " + this.value);
    }
}