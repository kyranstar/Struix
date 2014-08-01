package lang;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import script.TLLexer;
import script.TLParser;

public class ScriptInterpreter {
	private PrintStream output;
	
	public ScriptInterpreter(PrintStream output){
		this.output = output;
	}
	public Map<String, TLValue> interpret(Map<String, TLValue> variables, String code){
		final TLLexer lexer = new TLLexer(new ANTLRInputStream(code));
		final TLParser parser = new TLParser(new CommonTokenStream(lexer));
		final ParseTree tree = parser.parse();
		final EvalVisitor visitor = new EvalVisitor();
		visitor.getScope().setVariables(new HashMap<String, TLValue>(variables));
		visitor.setOutputStream(this.output);
		visitor.visit(tree);
		return visitor.getScope().getVariables();
	}
}
