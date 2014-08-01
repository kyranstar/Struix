package lang;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;
import lang.exceptions.CannotApplyExpressionException;
import lang.exceptions.UndefinedFunctionException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

import script.TLBaseVisitor;
import script.TLParser;
import script.TLParser.AndExpressionContext;
import script.TLParser.AssertFunctionCallContext;
import script.TLParser.BlockContext;
import script.TLParser.DivideExpressionContext;
import script.TLParser.ExpressionContext;
import script.TLParser.ExpressionExpressionContext;
import script.TLParser.ForStatementContext;
import script.TLParser.FunctionDeclContext;
import script.TLParser.GtEqExpressionContext;
import script.TLParser.GtExpressionContext;
import script.TLParser.IdContext;
import script.TLParser.IdentifierFunctionCallContext;
import script.TLParser.InExpressionContext;
import script.TLParser.LtEqExpressionContext;
import script.TLParser.LtExpressionContext;
import script.TLParser.ModulusExpressionContext;
import script.TLParser.NotExpressionContext;
import script.TLParser.OrExpressionContext;
import script.TLParser.PowerExpressionContext;
import script.TLParser.SubtractExpressionContext;
import script.TLParser.UnaryMinusExpressionContext;
import script.TLParser.WhileStatementContext;

import com.google.common.base.Optional;

public class EvalVisitor extends TLBaseVisitor<TLValue> {

	private Scope scope = new Scope();

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	private PrintStream output = System.out;

	private static final int MAX_PRECISION = 50;

	// '-' expression #unaryMinusExpression
	@Override
	public TLValue visitUnaryMinusExpression(UnaryMinusExpressionContext ctx) {
		final TLValue expr = visit(ctx.expression());
		
		if(expr.isNumber()){return new TLValue(expr.asNumber().negate());}
		
		throw new CannotApplyExpressionException(getLine(ctx) + "'-' cannot be used by type (" + expr.getType() + ")");
	}
	
	// '!' expression #notExpression
	@Override
	public TLValue visitNotExpression(NotExpressionContext ctx) {
		final TLValue expr = visit(ctx.expression());

		if (expr.isBoolean()) { return new TLValue(!expr.asBoolean()); }

		throw new CannotApplyExpressionException(getLine(ctx) + "'" + ctx.excl().getText() + "' cannot be used by type (" + expr.getType() + ")");
	}


	// expression '^' expression #powerExpression
	@Override
	public TLValue visitPowerExpression(@NotNull PowerExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}

		if(lhs.isNumber() && rhs.isNumber()){
			int exponent = Math.min(rhs.asNumber().intValue(), 999999999);
			return new TLValue(lhs.asNumber().pow(exponent, new MathContext(MAX_PRECISION)));
		}

		throw new CannotApplyExpressionException(getLine(ctx) + "'^' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '*' expression #multiplyExpression
	@Override
	public TLValue visitMultiplyExpression(@NotNull TLParser.MultiplyExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if (lhs.isNumber() && rhs.isNumber()) { return new TLValue(lhs.asNumber().multiply(rhs.asNumber())); }
		
		// concatination operator
		if (lhs.isString() && rhs.isNumber()) { return new TLValue(repeatString(lhs.asString(), rhs.asNumber())); }
		if (lhs.isNumber() && rhs.isString()) { return new TLValue(repeatString(rhs.asString(), lhs.asNumber())); }

		throw new CannotApplyExpressionException(getLine(ctx) + "'*' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}
	private String repeatString(String repeat, BigDecimal times) {
		final StringBuilder sb = new StringBuilder();
		// while greater than or equal to one
		while (times.compareTo(BigDecimal.ONE) >= 0) {
			times = times.subtract(BigDecimal.ONE);
			sb.append(repeat);
		}
		if (times.compareTo(BigDecimal.ZERO) > 0) {
			sb.append(repeat.substring(0, (int) (repeat.length() * times.doubleValue())));
		}
		return sb.toString();
	}

	// expression '/' expression #divideExpression

	@Override
	public TLValue visitDivideExpression(DivideExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}

		if (lhs.isNumber() && rhs.isNumber()) { 
			if(rhs.asNumber().signum() == 0){
				return new TLValue(BigDecimal.ZERO);
			}
			return new TLValue(lhs.asNumber().divide(rhs.asNumber(),
				MAX_PRECISION, RoundingMode.HALF_UP)); 
			}

		throw new CannotApplyExpressionException(getLine(ctx) + "'/' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '%' expression #modulusExpression
	@Override
	public TLValue visitModulusExpression(ModulusExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}

		if (lhs.isNumber() && rhs.isNumber()) { return new TLValue(lhs.asNumber().remainder(rhs.asNumber())); }

		throw new CannotApplyExpressionException(getLine(ctx) + "'%' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '+' expression #addExpression
	@Override
	public TLValue visitAddExpression(@NotNull TLParser.AddExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if (lhs.isNumber() && rhs.isNumber()) { return new TLValue(lhs.asNumber().add(rhs.asNumber())); }
		
		//string concatenation
		if (lhs.isString() && rhs.isString()) { return new TLValue(lhs.asString() + rhs.asString()); }
		if (lhs.isString()) { return new TLValue(lhs.asString() + rhs.toString()); }
		if (rhs.isString()) { return new TLValue(lhs.toString() + rhs.asString()); }

		throw new CannotApplyExpressionException(getLine(ctx) + "'+' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '-' expression #subtractExpression
	@Override
	public TLValue visitSubtractExpression(SubtractExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if (lhs.isNumber() && rhs.isNumber()) { return new TLValue(lhs.asNumber().subtract(rhs.asNumber())); }

		throw new CannotApplyExpressionException(getLine(ctx) + "'-' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '>=' expression #gtEqExpression
	@Override
	public TLValue visitGtEqExpression(GtEqExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if (lhs.isNumber() && rhs.isNumber()) {return new TLValue(lhs.asNumber().compareTo(rhs.asNumber()) >= 0);}
		if (lhs.isString() && rhs.isString()) {return new TLValue(lhs.asString().compareTo(rhs.asString()) >= 0);}
		
		throw new CannotApplyExpressionException(getLine(ctx) + "'>=' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '<=' expression #ltEqExpression
	@Override
	public TLValue visitLtEqExpression(LtEqExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if (lhs.isNumber() && rhs.isNumber()) {return new TLValue(lhs.asNumber().compareTo(rhs.asNumber()) <= 0);}
		if (lhs.isString() && rhs.isString()) {return new TLValue(lhs.asString().compareTo(rhs.asString()) <= 0);}
		
		throw new CannotApplyExpressionException(getLine(ctx) + "'<=' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '>' expression #gtExpression
	@Override
	public TLValue visitGtExpression(GtExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if (lhs.isNumber() && rhs.isNumber()) {return new TLValue(lhs.asNumber().compareTo(rhs.asNumber()) > 0);}
		if (lhs.isString() && rhs.isString()) {return new TLValue(lhs.asString().compareTo(rhs.asString()) > 0);}
		
		throw new CannotApplyExpressionException(getLine(ctx) + "'>' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '<' expression #ltExpression
	@Override
	public TLValue visitLtExpression(LtExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if (lhs.isNumber() && rhs.isNumber()) {return new TLValue(lhs.asNumber().compareTo(rhs.asNumber()) < 0);}
		if (lhs.isString() && rhs.isString()) {return new TLValue(lhs.asString().compareTo(rhs.asString()) < 0);}
		
		throw new CannotApplyExpressionException(getLine(ctx) + "'<' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression 'equals' expression #eqExpression
	@Override
	public TLValue visitEqExpression(@NotNull TLParser.EqExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		return new TLValue(lhs.equals(rhs));
	}

	// expression '!=' expression #notEqExpression
	@Override
	public TLValue visitNotEqExpression(@NotNull TLParser.NotEqExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		return new TLValue(!lhs.equals(rhs));
	}

	// expression '&&' expression #andExpression
	@Override
	public TLValue visitAndExpression(AndExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if(lhs.isBoolean() && rhs.isBoolean()){return new TLValue(lhs.asBoolean() && rhs.asBoolean());}
		
		throw new CannotApplyExpressionException(getLine(ctx) + "'" + ctx.and().getText() + "' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression '||' expression #orExpression
	@Override
	public TLValue visitOrExpression(OrExpressionContext ctx) {
		final TLValue lhs = visit(ctx.expression(0));
		final TLValue rhs = visit(ctx.expression(1));
		
		if(lhs == null || rhs == null || lhs.isNull() || rhs.isNull()){return TLValue.NULL;}
		
		if(lhs.isBoolean() && rhs.isBoolean()){return new TLValue(lhs.asBoolean() || rhs.asBoolean());}
		
		throw new CannotApplyExpressionException(getLine(ctx) + "'" + ctx.or().getText() + "' cannot be used by types (" + lhs.getType() + ", " + rhs.getType() + ")");
	}

	// expression In expression #inExpression
	@Override
	public TLValue visitInExpression(InExpressionContext ctx) {
		TLValue lhs = visit(ctx.expression(0));
		TLValue rhs = visit(ctx.expression(1));

		if(rhs.isString()){
			return new TLValue(rhs.asString().contains(lhs.toString()));
		}
		throw new CannotApplyExpressionException("'In' expression cannot apply to types (" +lhs.getType() + ", " + rhs.getType() + ")");
	}

	// Number #numberExpression
	@Override
	public TLValue visitNumberExpression(@NotNull TLParser.NumberExpressionContext ctx) {
		return new TLValue(new BigDecimal(ctx.getText()));
	}

	// Bool #boolExpression
	@Override
	public TLValue visitBoolExpression(@NotNull TLParser.BoolExpressionContext ctx) {
		return new TLValue(Boolean.valueOf(ctx.getText()));
	}

	// Null #nullExpression
	@Override
	public TLValue visitNullExpression(@NotNull TLParser.NullExpressionContext ctx) {
		return TLValue.NULL;
	}

	// functionCall indexes? #functionCallExpression
	//TODO

	//functionDecl Def id '(' idList? ')' block End 
	@Override
	public TLValue visitFunctionDecl(FunctionDeclContext ctx) {
		String id = ctx.id().getText();
		
		List<IdContext> param = null;
		if(ctx.idList() != null){
			param = ctx.idList().id();
		}else{
			//no params
			param = new ArrayList<>();
		}
		List<String> paramNames = new ArrayList<String>(param.size());
		for(IdContext context : param){
			paramNames.add(context.getText());
		}
		
		BlockContext block = ctx.block();
		
		Function func = new Function(id, paramNames, block);
		scope.defineFunction(id, func);
		
		return TLValue.VOID;
	}

	private boolean isNumeric(String s) {
		final int len = s.length();
		for (int i = 0; i < len; ++i) {
			if (!Character.isDigit(s.charAt(i))) { return false; }
		}
		return true;
	}

	// Identifier #identifierExpression
	@Override
	public TLValue visitIdentifierExpression(@NotNull TLParser.IdentifierExpressionContext ctx) {
		final String id = ctx.id().getText();
		return scope.resolve(id);
	}
	// String indexes? #stringExpression
	@Override
	public TLValue visitStringExpression(@NotNull TLParser.StringExpressionContext ctx) {
		final String text = ctx.getText();
		final String stripped = text.substring(1, text.length() - 1).replaceAll("\\\\(.)", "$1");
		return new TLValue(stripped);
	}

	// '(' expression ')' #expressionExpression
	@Override
	public TLValue visitExpressionExpression(ExpressionExpressionContext ctx) {
		return visit(ctx.expression());
		
	}

	// Input '(' String? ')' #inputExpression
	// TODO

//	assignment
//	 : id indexes? assign expression
//	 ;
	@Override
	public TLValue visitAssignment(@NotNull TLParser.AssignmentContext ctx) {
		final String id = ctx.id().getText();
		final TLValue value = visit(ctx.expression());

		scope.assign(id, value);
		return TLValue.VOID;
	}

	// Identifier '(' exprList? ')' #identifierFunctionCall
	@Override
	public TLValue visitIdentifierFunctionCall(IdentifierFunctionCallContext ctx) {
		Function func = scope.getFunction(ctx.id().getText());
		
		if(func == null){
			throw new UndefinedFunctionException(getLine(ctx) + "Function " + ctx.id().getText() + " has not been defined (yet?)");
		}
		
		List<ExpressionContext> exprList = null;
		if( ctx.exprList() == null){
			exprList = new ArrayList<>();
		}else{
			exprList = ctx.exprList().expression();
		}
		List<TLValue> params = new ArrayList<TLValue>(exprList.size());
		for(ExpressionContext context : exprList){
			params.add(visit(context));
		}
		
		return func.invoke(params, this);
	}
	//Assert '(' expression ')' #assertFunctionCall
	@Override
	public TLValue visitAssertFunctionCall(AssertFunctionCallContext ctx) {
		if(!visit(ctx.expression()).asBoolean()){
			throw new AssertionFailedError(getLine(ctx) + "Expression " + ctx.expression().getText() + " returned false");
		}
		return TLValue.VOID;
	}

	// Println '(' expression? ')' #printlnFunctionCall
	@Override
	public TLValue visitPrintlnFunctionCall(@NotNull TLParser.PrintlnFunctionCallContext ctx) {
		output.println(visit(ctx.expression()));
		return TLValue.VOID;
	}

	// Print '(' expression ')' #printFunctionCall
	@Override
	public TLValue visitPrintFunctionCall(@NotNull TLParser.PrintFunctionCallContext ctx) {
		output.print(visit(ctx.expression()));
		return TLValue.VOID;
	}
	// Size '(' expression ')' #sizeFunctionCall
	// TODO

	// forStatement
	// : For id assign expression To expression Then block End
	// ;
	@Override
	public TLValue visitForStatement(ForStatementContext ctx) {
		String id = ctx.id().getText();
		BigDecimal init = visit(ctx.expression(0)).asNumber();
		BigDecimal finalVal = visit(ctx.expression(1)).asNumber();
		
	//	System.out.println(ctx.equals().toString())
		
		Optional<Boolean> goingUp = Optional.absent();
		boolean alreadyEqual = false;
		
		while(true){
			if(finalVal.compareTo(init) > 0){
				//we lower than the value
				if(!goingUp.isPresent()){					
					goingUp = Optional.of(true);
				}else{
					if (!goingUp.get()){
						//if we were going down and we are currently lower, we exit
						return TLValue.VOID;
					}
				}
				
				scope = new Scope(scope);
				scope.assign(id, new TLValue(init));
				visit(ctx.block());
				scope = scope.parent();
				init = init.add(BigDecimal.ONE);
			}else if(finalVal.compareTo(init) < 0){
				//we are above the value
				if(!goingUp.isPresent()){					
					goingUp = Optional.of(false);
				}else{
					if (goingUp.get()){
						//if we were going up and we are currently above, we exit
						return TLValue.VOID;
					}
				}
				
				scope = new Scope(scope);
				scope.assign(id, new TLValue(init));
				visit(ctx.block());
				scope = scope.parent();
				init = init.subtract(BigDecimal.ONE);
			}else{
				//They're equal, we exit
				if(!alreadyEqual){
					//we want to make sure the range is inclusive
					scope = new Scope(scope);
					scope.assign(id, new TLValue(init));
					visit(ctx.block());
					scope = scope.parent();
					alreadyEqual = true;
				}
				else
					return TLValue.VOID;
			}
		}
	}
	
	//whileStatement
	// : While expression Then block End
	// ;
	@Override
	public TLValue visitWhileStatement(WhileStatementContext ctx) {
		while(visit(ctx.expression()).asBoolean()){
			scope = new Scope(scope);
			visit(ctx.block());
			scope = scope.parent();
		}
		return TLValue.VOID;
	}

	// ifStatement
	// : ifStat elseIfStat* elseStat? End
	// ;
	//
	// ifStat
	// : If expression Do block
	// ;
	//
	// elseIfStat
	// : Else If expression Do block
	// ;
	//
	// elseStat
	// : Else Do block
	// ;
	@Override
	public TLValue visitIfStatement(@NotNull TLParser.IfStatementContext ctx) {

		// if ...
		if (visit(ctx.ifStat().expression()).asBoolean()) {
			scope = new Scope(scope);
			final TLValue v = visit(ctx.ifStat().block());
			scope = scope.parent();
			return v;
		}

		// else if ...
		for (int i = 0; i < ctx.elseIfStat().size(); i++) {
			if (visit(ctx.elseIfStat(i).expression()).asBoolean()) {
				scope = new Scope(scope);
				final TLValue v = visit(ctx.elseIfStat(i).block());
				scope = scope.parent();
				return v;
			}
		}

		// else ...
		if (ctx.elseStat() != null) {
			scope = new Scope(scope);
			final TLValue v = visit(ctx.elseStat().block());
			scope = scope.parent();
			return v;
		}

		return TLValue.VOID;
	}

	private String getLine(ParserRuleContext ctx){
		int start = ctx.getStart().getLine();
		int end = ctx.getStop().getLine();
		if(start == end){
			return "Line " + ctx.getStart().getLine() + ": ";
		}
		return "Lines " + ctx.getStart().getLine() + "-" + ctx.getStop().getLine() + ": ";
	}
	
	public void setOutputStream(PrintStream output) {
		this.output = output;
	}
}