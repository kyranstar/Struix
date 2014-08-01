package lang;

import java.util.List;

import org.antlr.v4.runtime.RecognitionException;

import script.TLParser.BlockContext;

public class Function {

    private String id;
    private List<String> identifiers;
    private BlockContext code;
    private Scope scope;

    public Function(String id, List<String> paramName, BlockContext block) {
        this.id = id;
        this.identifiers = paramName;
        this.code = block;
        this.scope = new Scope();
    }

    public Function(Function original) {
        this.id = original.id;
        this.identifiers = original.identifiers;
        this.code = original.code;
        this.scope = original.scope.copy();
    }

    public TLValue invoke(List<TLValue> params, EvalVisitor visitor) {

        if(params.size() != identifiers.size()) {
            throw new IllegalArgumentException("illegal function call: " + identifiers.size() +
                    " parameters expected for function `" + id + "`, was given " + params.size());
        }

        // Assign all expression parameters to this function's identifiers
        for(int i = 0; i < identifiers.size(); i++) {
            scope.assign(identifiers.get(i), params.get(i));
        }

        try {
        	
            // Create a tree walker to evaluate this function's code block
        	Scope oldScope = visitor.getScope();
            scope.setFunctions(oldScope.getFunctions());
        	visitor.setScope(scope);
        	visitor.visit(code);
        	if(code.returnExp() != null){
        		return visitor.visit(code.returnExp().expression());
        	}
        	visitor.setScope(oldScope);
        	
            return TLValue.VOID;
        } catch (RecognitionException e) {
            // do not recover from this
            throw new RuntimeException("something went wrong, terminating", e);
        }
    }
}