package grammar;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.st.grammar.DefaultStructuredTextListener;
import com.st.grammar.StructuredTextLexer;
import com.st.grammar.StructuredTextParser;
import com.st.grammar.StructuredTextParser.Program_delcarationContext;

public class Main {

	public static void main(final String[] args) throws IOException {

		final CharStream charStream = CharStreams
				// .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
				.fromFileName("src/test/resources/iec61131_structuredtext/program.st");

		final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

		// create a buffer of tokens pulled from the lexer
		final CommonTokenStream tokens = new CommonTokenStream(lexer);

		final StructuredTextParser parser = new StructuredTextParser(tokens);

		final Program_delcarationContext root = parser.program_delcaration();

//		// Create a generic parse tree walker that can trigger callbacks
		final ParseTreeWalker walker = new ParseTreeWalker();
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(new DefaultStructuredTextListener(), root);
		System.out.println(); // print a \n after translation

		// Walk the tree again to translate to java
		// walker.walk(new MyLangTranslator(), tree);

	}

}
