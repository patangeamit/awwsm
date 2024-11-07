package com.example;

public class Assembler {
    // old code.
    public static String to_assembly(Token[] tokens){
        boolean return_check = false;
        StringBuilder sb = new StringBuilder();
        sb.append("global _start\n_start:");
        for(int i = 0; i < tokens.length; i++){
            if (tokens[i]==null){
                break;
            }else
            if (tokens[i].type == "RETURN" ){
                if (tokens[i+1]==null) return "Invalid syntax! nothing to return.";
                if (tokens[i+2]==null) return "Invalid syntax! missing semicolon.";
                if (tokens[i+1].type == "INTEGER"){
                    if (tokens[i+2].type == "SEMICOLON"){
                        sb.append("\n    mov rax, 60\n    mov rdi, " + tokens[i+1].value + "\n    syscall");
                        return_check = true;
                    }else{
                        return "Invalid syntax!\nmissing semicolon.";
                    }
                }else{
                    return "Invalid syntax!\nan integer should be returned.";
                }
            }
        }
        if (!return_check) return "nothing returned";
        return sb.toString();
    }
    
    public static String to_assembly(Token head){
        boolean return_check = false;
        StringBuilder sb = new StringBuilder();
        sb.append("global _start\n_start:");
        Token curr = head;
        while (curr.next != null){
            if (curr.type == "RETURN" ){
                if (curr.next==null) return "Invalid syntax! nothing to return.";
                if (curr.next.next==null) return "Invalid syntax! missing semicolon.";
                if (curr.next.type == "INTEGER"){
                    if (curr.next.next.type == "SEMICOLON"){
                        sb.append("\n    mov rax, 60\n    mov rdi, " + curr.next.value + "\n    syscall");
                        return_check = true;
                    }else{
                        return "Invalid syntax!\nmissing semicolon.";
                    }
                }else{
                    return "Invalid syntax!\nan integer should be returned.";
                }
            }
            curr = curr.next;
        }
        if (!return_check) return "nothing returned";
        return sb.toString();
    }
}
