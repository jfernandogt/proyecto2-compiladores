package com.jfbarrios;


import picocli.CommandLine;

import static picocli.CommandLine.*;

import java.io.StringReader;
import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name = "json-validator", mixinStandardHelpOptions = true, version = "0.0.1",
        description = "JSON Validator")
public class App implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        do {
            System.out.print("Ingrese el JSON a validar: ");
            input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }

            JSONLexer lexer = new JSONLexer(new StringReader(input));
            Parser parser = new Parser(lexer);
            System.out.println("Resultado: " + parser.parse().value);

        } while (true);

        return 0;
    }

    public static void main( String[] args )
    {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }


}
