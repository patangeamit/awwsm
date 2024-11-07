package com.example;

import com.example.Parser.NodeExit;

public class Generator {
    public String generate(NodeExit nodeRoot){
        String output = "global _start\n" +
        "_start:\n" +
        "    mov rax, 60\n" +
        "    mov rdi, " + nodeRoot.exprNode.tokenINT.value + "\n" +
        "    syscall";
        return output;
    }    
}
