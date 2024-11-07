package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.StackWalker.Option;
import java.util.Optional;

import com.example.Parser.NodeExit;

public class Main {
    
    // Source Code -> Tokenizer -> Parser -> Assembler -> Output;
    public static void main(String[] args) throws FileNotFoundException {
        Tokenizer tokenizer = new Tokenizer();
        StringBuilder outputBuilder = new StringBuilder();
        Parser parser = new Parser();
        try (BufferedReader br = new BufferedReader(new FileReader("test.aww"))) {
            
            Token head = tokenizer.token_prog(br);

            // String assembly_code = Assembler.to_assembly(head);
            // outputBuilder.append(assembly_code);

            Generator generator = new Generator();
            Optional<NodeExit> o = parser.parse(head);
            // System.out.println(o.get().exprNode);
            if (o.isPresent()) {
                NodeExit exit = o.get();
                outputBuilder.append(generator.generate(exit));
            }else{
                outputBuilder.append("No exit statement found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test.asm"))) {
            writer.write(outputBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        printStatus(outputBuilder.toString());
        //doesn't work for some reason.

        // try {
        //     Process p = Runtime.getRuntime().exec("nasm -felf64 test.asm && ld test.o -o test");
        //     BufferedReader reader = new BufferedReader(
        //         new InputStreamReader(p.getInputStream()));
        //         String line;
        //     while ((line = reader.readLine()) != null) {
        //         System.out.println(line);
        //         }reader.close();

        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        
    }
    public static void printStatus(String assembly){
        System.out.println("\n"+assembly+"\n");
        System.out.println("nasm -felf64 test.asm && ld test.o -o test && ./test");
    }
}

// nasm -felf64 test.asm && ld test.o -o test && ./test