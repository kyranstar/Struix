package script;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;

import lang.EvalVisitor;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import script.TLLexer;
import script.TLParser;

public abstract class GrammarTest {

	public static final String ENCODING = "UTF-8";
	public static final String FILE_PREFIX = "test/script/tests/";

	public abstract File getInputFile();

	public abstract File getOutputFile();

	public String expected() throws IOException {
		return readFile(getOutputFile());
	}

	public String got() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream output = new PrintStream(baos, true, ENCODING);

		final TLLexer lexer = new TLLexer(new ANTLRInputStream(readFile(getInputFile())));
		final TLParser parser = new TLParser(new CommonTokenStream(lexer));
		final ParseTree tree = parser.parse();
		final EvalVisitor visitor = new EvalVisitor();
		visitor.setOutputStream(output);
		visitor.visit(tree);

		return baos.toString(ENCODING);
	}

	private String readFile(File f) throws IOException {
		final byte[] bytes = Files.readAllBytes(f.toPath());
		return new String(bytes, Charset.forName(ENCODING));
	}
}
