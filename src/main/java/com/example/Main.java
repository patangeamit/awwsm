package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class Main {

    // Source Code -> Tokenizer -> Parser -> Assembler -> Output;
    public static void main(String[] args) throws FileNotFoundException { // PROGRAM STARTS HERE
        Tokenizer tokenizer = new Tokenizer();
        StringBuilder outputBuilder = new StringBuilder();
        Parser parser = new Parser();
        try (BufferedReader br = new BufferedReader(new FileReader("main.aww"))) {

            Token head = tokenizer.token_prog(br);
            Generator generator = new Generator();
            Optional<NodeProg> o = parser.parseProg(head);
            if (o.isPresent()) {
                NodeProg program = o.get();
                outputBuilder.append(generator.generate(program));
            } else {
                outputBuilder.append("No exit statement found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("main.asm"))) {
            writer.write(outputBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        printStatus(outputBuilder.toString());
    }

    public static void printStatus(String assembly) {
        System.out.println("\n" + assembly + "\n");
        System.out.println("nasm -felf64 main.asm && ld main.o -o main && ./main");
        System.out.println("nasm -felf64 main.asm && ld main.o -o main && ./main ; echo $?");
    }
}

// nasm -felf64 test.asm && ld test.o -o test && ./test