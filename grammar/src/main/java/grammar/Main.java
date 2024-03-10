package grammar;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.st.grammar.ASTListener;
import com.st.grammar.DefaultStructuredTextListener;
import com.st.grammar.ModelCreatorASTListener;
import com.st.grammar.StructuredTextLexer;
import com.st.grammar.StructuredTextParser;
import com.st.grammar.StructuredTextParser.Assignment_statementContext;
import com.st.grammar.StructuredTextParser.Case_statementContext;
import com.st.grammar.StructuredTextParser.Compilation_unitContext;
import com.st.grammar.StructuredTextParser.Data_type_declarationContext;
import com.st.grammar.StructuredTextParser.Function_block_declarationContext;
import com.st.grammar.StructuredTextParser.Interface_declarationContext;
import com.st.grammar.StructuredTextParser.Program_declarationContext;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Start");

        application();

        /*
         * assignment();
         * binaryInteger();
         * functionProgram();
         * functionInterface();
         * functionBlock();
         * //functionBlock2();
         * //functionBlock3();
         * //functionBlock4();
         * case_test();
         * tcunit();
         * type();
         * expressions();
         * //eventLog();
         */

        System.out.println("End");
    }

    private static void application() throws IOException {
        //System.out.println("application");

        //String pathAsString = "C:/Users/U5353/Documents/OpenPLC/OpenPLC_Editor_TestProject/generated_st_code.st";
        //String pathAsString = "grammar\\src\\test\\resources\\iec61131_structuredtext\\self_contained_program.st";
        String pathAsString = "grammar\\src\\test\\resources\\iec61131_structuredtext\\small_program.st";
        //String pathAsString = "grammar\\src\\test\\resources\\iec61131_structuredtext\\configuration.st";
        //String pathAsString = "grammar\\src\\test\\resources\\iec61131_structuredtext\\configuration_with_variables.st";
        //String pathAsString = "grammar\\src\\test\\resources\\iec61131_structuredtext\\type_bool_struct.st";

        final CharStream charStream = CharStreams
                .fromFileName(pathAsString);

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse
        // Function_block_declarationContext root = parser.function_block_declaration();
        // Assignment_statementContext root = parser.assignment_statement();
        Compilation_unitContext root = parser.compilation_unit();

        //ASTListener listener = new ASTListener();
        ModelCreatorASTListener listener = new ModelCreatorASTListener();
        //DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        //System.out.println();

        System.out.println("\nTypes");
        System.out.println(listener.globalTypeScope);

        System.out.println("\nProgram");
        System.out.println(listener.program);

        System.out.println("\nConfiguration");
        System.out.println(listener.configuration);
    }

    private static void assignment() throws IOException {
        System.out.println("assignment");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/assignment.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse
        // Function_block_declarationContext root = parser.function_block_declaration();
        Assignment_statementContext root = parser.assignment_statement();

        ASTListener listener = new ASTListener();
        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void binaryInteger() throws IOException {
        System.out.println("binaryInteger");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/binary_integer.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse
        // Function_block_declarationContext root = parser.function_block_declaration();
        Assignment_statementContext root = parser.assignment_statement();

        ASTListener listener = new ASTListener();
        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void eventLog() throws IOException {

        System.out.println("EventLog");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/event_log.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse
        // Function_block_declarationContext root = parser.function_block_declaration();
        Compilation_unitContext root = parser.compilation_unit();

        ASTListener listener = new ASTListener();
        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void type() throws IOException {

        System.out.println("Test Type");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");
                .fromFileName("src/test/resources/iec61131_structuredtext/type.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a type declaration
        Data_type_declarationContext root = parser.data_type_declaration();

        ASTListener listener = new ASTListener();
        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void case_test() throws IOException {

        System.out.println("case_test");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/case.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a case declaration
        Case_statementContext root = parser.case_statement();

        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionProgram() throws IOException {

        System.out.println("Test Program");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
        // .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
        // .fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        final Program_declarationContext root = parser.program_declaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        // Function_block_declarationContext root = parser.function_block_declaration();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(new DefaultStructuredTextListener(), root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionInterface() throws IOException {

        System.out.println("Test Interface");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
                .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
        // .fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        final Interface_declarationContext root = parser.interface_declaration();

        // function block
        // Function_block_declarationContext root = parser.function_block_declaration();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(new DefaultStructuredTextListener(), root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionBlock() throws IOException {

        System.out.println("functionBlock()");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionBlock2() throws IOException {

        System.out.println("functionBlock2()");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/function_block_2.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionBlock3() throws IOException {

        System.out.println("functionBlock3()");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/function_block_3.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionBlock4() throws IOException {

        System.out.println("functionBlock4()");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/function_block_4.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void tcunit() throws IOException {

        System.out.println("tcunit()");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
                .fromFileName(
                        "src/test/resources/iec61131_structuredtext/tcunit/FB_AdjustAssertFailureMessageToMax253CharLength.TcPOU");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void expressions() throws IOException {

        System.out.println("expressions()");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
                .fromFileName("src/test/resources/iec61131_structuredtext/expressions.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        Program_declarationContext root = parser.program_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

}
