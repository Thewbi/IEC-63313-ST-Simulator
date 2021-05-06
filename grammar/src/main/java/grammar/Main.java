package grammar;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.st.grammar.ASTListener;
import com.st.grammar.DefaultStructuredTextListener;
import com.st.grammar.StructuredTextLexer;
import com.st.grammar.StructuredTextParser;
import com.st.grammar.StructuredTextParser.Data_type_declarationContext;
import com.st.grammar.StructuredTextParser.Function_block_declarationContext;
import com.st.grammar.StructuredTextParser.Interface_declarationContext;
import com.st.grammar.StructuredTextParser.Program_delcarationContext;

import common.Node;

public class Main {

	public static void main(final String[] args) throws IOException {
		functionProgram();
		functionInterface();
		functionBlock();
		functionBlock2();
		type();
		expressions();
	}
	
	private static void type() throws IOException {
		
		System.out.println("Test Type");
		
		final CharStream charStream = CharStreams
				// .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/program.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");
				.fromFileName("src/test/resources/iec61131_structuredtext/type.st");

		final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

		// create a buffer of tokens pulled from the lexer
		final CommonTokenStream tokens = new CommonTokenStream(lexer);

		final StructuredTextParser parser = new StructuredTextParser(tokens);

		// parse a type declaration
        Data_type_declarationContext root = parser.data_type_declaration();

//		// Create a generic parse tree walker that can trigger callbacks
		final ParseTreeWalker walker = new ParseTreeWalker();
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(new DefaultStructuredTextListener(), root);
		
		// print a \n after translation
		System.out.println(); 
	}
	
	private static void functionProgram() throws IOException {
		
		System.out.println("Test Program");
		
		final CharStream charStream = CharStreams
				// .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
				.fromFileName("src/test/resources/iec61131_structuredtext/program.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

		final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

		// create a buffer of tokens pulled from the lexer
		final CommonTokenStream tokens = new CommonTokenStream(lexer);

		final StructuredTextParser parser = new StructuredTextParser(tokens);

		// parse a program
		final Program_delcarationContext root = parser.program_delcaration();
		
		// parse an interface
//		final Interface_declarationContext root = parser.interface_declaration();
		
		// function block
//		Function_block_declarationContext root = parser.function_block_declaration();

//		// Create a generic parse tree walker that can trigger callbacks
		final ParseTreeWalker walker = new ParseTreeWalker();
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(new DefaultStructuredTextListener(), root);
		
		// print a \n after translation
		System.out.println();
	}
	
	private static void functionInterface() throws IOException {
		
		System.out.println("Test Interface");
		
		final CharStream charStream = CharStreams
				// .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/program.st");
				.fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

		final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

		// create a buffer of tokens pulled from the lexer
		final CommonTokenStream tokens = new CommonTokenStream(lexer);

		final StructuredTextParser parser = new StructuredTextParser(tokens);

		// parse a program
//		final Program_delcarationContext root = parser.program_delcaration();
		
		// parse an interface
		final Interface_declarationContext root = parser.interface_declaration();
		
		// function block
//		Function_block_declarationContext root = parser.function_block_declaration();

//		// Create a generic parse tree walker that can trigger callbacks
		final ParseTreeWalker walker = new ParseTreeWalker();
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(new DefaultStructuredTextListener(), root);
		
		// print a \n after translation
		System.out.println();
	}

	private static void functionBlock() throws IOException {
		
		System.out.println("functionBlock()");
		
		final CharStream charStream = CharStreams
				// .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/program.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
				.fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

		final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

		// create a buffer of tokens pulled from the lexer
		final CommonTokenStream tokens = new CommonTokenStream(lexer);

		final StructuredTextParser parser = new StructuredTextParser(tokens);

		// parse a program
//		final Program_delcarationContext root = parser.program_delcaration();
		
		// parse an interface
//		final Interface_declarationContext root = parser.interface_declaration();
		
		// function block
		Function_block_declarationContext root = parser.function_block_declaration();
		
//		DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
		ASTListener listener = new ASTListener();

//		// Create a generic parse tree walker that can trigger callbacks
		final ParseTreeWalker walker = new ParseTreeWalker();
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(listener, root);
		 // print a \n after translation
		System.out.println();
	}
	
	private static void functionBlock2() throws IOException {
		
		System.out.println("functionBlock2()");
		
		final CharStream charStream = CharStreams
				// .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/program.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
				.fromFileName("src/test/resources/iec61131_structuredtext/tcunit/FB_AdjustAssertFailureMessageToMax253CharLength.TcPOU");

		final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

		// create a buffer of tokens pulled from the lexer
		final CommonTokenStream tokens = new CommonTokenStream(lexer);

		final StructuredTextParser parser = new StructuredTextParser(tokens);

		// parse a program
//		final Program_delcarationContext root = parser.program_delcaration();
		
		// parse an interface
//		final Interface_declarationContext root = parser.interface_declaration();
		
		// function block
		Function_block_declarationContext root = parser.function_block_declaration();

//		DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
		ASTListener listener = new ASTListener();

//		// Create a generic parse tree walker that can trigger callbacks
		final ParseTreeWalker walker = new ParseTreeWalker();
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(listener, root);
		// print a \n after translation
		System.out.println();
		
//		Node rootNode = listener.getRootNode();
//		rootNode.print(0);

	}
	
	private static void expressions() throws IOException {
		
		System.out.println("expressions()");
		
		final CharStream charStream = CharStreams
				// .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/program.st");
				//.fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
				.fromFileName("src/test/resources/iec61131_structuredtext/expressions.st");

		final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

		// create a buffer of tokens pulled from the lexer
		final CommonTokenStream tokens = new CommonTokenStream(lexer);

		final StructuredTextParser parser = new StructuredTextParser(tokens);

		// parse a program
		Program_delcarationContext root = parser.program_delcaration();

//		DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
		ASTListener listener = new ASTListener();

		// Create a generic parse tree walker that can trigger callbacks
		final ParseTreeWalker walker = new ParseTreeWalker();
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(listener, root);
		// print a \n after translation
		System.out.println();
		
		Node rootNode = listener.getRootNode();
		rootNode.print(0);

	}

}
