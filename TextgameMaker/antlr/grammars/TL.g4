grammar TL;

@header {
package script;
}

parse
 : block
 ;

block
 : (statement | functionDecl)* (returnExp)?
 ;

statement
 : assignment ';'
 | functionCall ';'
 | ifStatement
 | forStatement
 | whileStatement
 ;

assignment
 : id assign expression
 ;

functionCall
 : id '(' exprList? ')' #identifierFunctionCall
 | Assert '(' expression ')' #assertFunctionCall
 | Println '(' expression? ')'  #printlnFunctionCall
 | Print '(' expression ')'     #printFunctionCall
 | Size '(' expression ')'      #sizeFunctionCall
 ;

ifStatement
 : ifStat elseIfStat* elseStat? End
 ;

ifStat
 : If expression then block
 ;

elseIfStat
 : Else If expression then block
 ;

elseStat
 : Else then block
 ;

functionDecl
 : Def id '(' idList? ')' block End
 ;

forStatement
 : For id assign expression To expression then block End
 ;

whileStatement
 : While expression then block End
 ;

idList
 : id (',' id)*
 ;

exprList
 : expression (',' expression)*
 ;

expression
 : Subtract expression                           #unaryMinusExpression
 | excl expression                           #notExpression
 | expression Pow expression                #powerExpression
 | expression Multiply expression                #multiplyExpression
 | expression Divide expression                #divideExpression
 | expression Modulus expression                #modulusExpression
 | expression Add expression                #addExpression
 | expression Subtract expression                #subtractExpression
 | expression GTEquals expression               #gtEqExpression
 | expression LTEquals expression               #ltEqExpression
 | expression GT expression                #gtExpression
 | expression LT expression                #ltExpression
 | expression equals expression               #eqExpression
 | expression nequals expression               #notEqExpression
 | expression and expression               #andExpression
 | expression or expression               #orExpression
 | expression In expression                 #inExpression
 | Number                                   #numberExpression
 | Bool                                     #boolExpression
 | Null                                     #nullExpression
 | functionCall                    #functionCallExpression
 | id                      #identifierExpression
 | String                          #stringExpression
 | '(' expression ')'              #expressionExpression
 | Input '(' String? ')'                    #inputExpression
 ;
equals: Is Equal To? | Is Equivalent To? | Equals | '==';


returnExp : Return expression ';';

Assert   : 'assert';
Println  : 'println';
Print    : 'print';
Input    : 'input';
Size     : 'size';
Def      : 'def';
If       : 'if';
In       : 'in';
Else     : 'else';
Return   : 'return';
For      : 'for';
While    : 'while';
To       : 'to';
then     : Then|Do;
Then     : 'then';
Do       : 'do';
End      : 'end';
Null     : 'null';

or       : '||' | Or;
and      : '&&' | And;

Or       : 'or';
And      : 'and';
Equals   : 'equals';
Equal    : 'equal';
Equivalent: 'equivalent';
nequals  : (Does|Is)?Not Equal To?;
Does     : 'does';
NEquals  : '!=';
GTEquals : '>=';
LTEquals : '<=';
Pow      : '^';
excl     : '!' | Not;
Not      :'not';
GT       : '>';
LT       : '<';
Add      : '+';
Subtract : '-';
Multiply : '*';
Divide   : '/';
Modulus  : '%';
OBrace   : '{';
CBrace   : '}';
OBracket : '[';
CBracket : ']';
OParen   : '(';
CParen   : ')';
SColon   : ';';
assign   : (Is (A|An|The)?) | '=';
Comma    : ',';

A:'a';
Is:'is';
The:'the';
An:'a';

Bool
 : 'true' 
 | 'false'
 ;

Number
 : Int ('.' Digit*)?
 ;

id:( Identifier | A | An | And |Or| Is | The | Equals | Equal | Equivalent | To | Not | Input | Do| Then|Does);

Identifier
 : [a-zA-Z_] [a-zA-Z_0-9]*
 ;

String
 : ["] (~["\r\n] | '\\\\' | '\\"')* ["]
 | ['] (~['\r\n] | '\\\\' | '\\\'')* [']
 ;

Comment
 : (('//'|'#') ~[\r\n]* | '/*' .*? '*/') -> skip
 ;

Space
 : [ \t\r\n\u000C] -> skip
 ;

fragment Int
 : [1-9] Digit*
 | '0'
 ;
  
fragment Digit 
 : [0-9]
 ;