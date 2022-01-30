package SemanticAnalysis;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestSemantic {
    public static void main(String[] args) throws IOException {
        if (args.length == 0)
            System.err.println("No file arguments givens");
        else {
            // parse each file argument given
            for (String arg : args) {
                FileReader file;

                // attempt to open file
                try {
                    file = new FileReader(arg);
                } catch (FileNotFoundException e) {
                    System.err.println(arg + " was not found!");
                    continue; // try next file
                }

                // create semantic analyzer
                SemanticAnalyzer semantic = new SemanticAnalyzer(file);
                System.out.println("Analyzing " + arg + "...");

                // initiate parse and clock time
                long startTime = System.currentTimeMillis();
                semantic.analyzeProgram();
                long endTime = System.currentTimeMillis();

                // print out statistics
                System.out.println("File has finished analyzing!");
                System.out.println("Execution time: " + (endTime - startTime) + "ms");
                System.out.println(semantic.getErrors() + " errors reported");
                System.out.println("---");
            }
        }
    }
}