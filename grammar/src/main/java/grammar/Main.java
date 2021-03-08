package grammar;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.st.grammar.DefaultStructuredTextListener;
import com.st.grammar.StructuredTextLexer;
import com.st.grammar.StructuredTextParser;
import com.st.grammar.StructuredTextParser.Function_block_declarationContext;
import com.st.grammar.StructuredTextParser.Interface_declarationContext;
import com.st.grammar.StructuredTextParser.Program_delcarationContext;

public class Main {

	public static void main(final String[] args) throws IOException {

		functionProgram();
		functionInterface();
		functionBlock();

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
		System.out.println(); // print a \n after translation

		// Walk the tree again to translate to java
		// walker.walk(new MyLangTranslator(), tree);
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
		System.out.println(); // print a \n after translation

		// Walk the tree again to translate to java
		// walker.walk(new MyLangTranslator(), tree);
	}

	private static void functionBlock() throws IOException {
		
		System.out.println("Test Block");
		
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

//		// Create a generic parse tree walker that can trigger callbacks
		final ParseTreeWalker walker = new ParseTreeWalker();
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(new DefaultStructuredTextListener(), root);
		System.out.println(); // print a \n after translation

		// Walk the tree again to translate to java
		// walker.walk(new MyLangTranslator(), tree);
	}

}
