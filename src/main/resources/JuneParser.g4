parser grammar JuneParser;

options {
    tokenVocab = JuneLexer;
    superClass = JuneParserBase;
}
identifier : IDENTIFIER ;

sourceFile
    : classDeclaration EOF
    ;

classDeclaration
    : modifier* CLASS identifier classBody
    ;

modifier
    : PUBLIC
    ;

classBody
    : L_CURLY classBodyDeclaration* R_CURLY
    ;

classBodyDeclaration
    : SEMI
    | (functionDecl) eos
    ;

functionDecl
    : FUNC IDENTIFIER signature block?
    ;

block
    : L_CURLY statementList R_CURLY
    ;

statementList
    : (statement eos)*
    ;

statement
    : declaration
    | macroStmt
    | assignment
    | expression
    | forLoopStmt
    | returnStmt
    ;

assignment
    : expression assign_op expression
    ;

assign_op
    : (PLUS | MINUS | STAR | DIV | MOD)? ASSIGN
    ;

returnStmt
    : RETURN expressionList?
    ;

declaration
    : constDecl
    | varDecl
    | shortVarDecl
    ;

arrayDecl
    : type_ L_BRACKET expressionList R_BRACKET
    ;

varDecl
    : VAR (varSpec | L_PAREN (varSpec eos)* R_PAREN)
    ;

shortVarDecl
    : identifierList DECLARE_ASSIGN expressionList
    ;

constDecl
    : CONST (constSpec | L_PAREN (constSpec eos)* R_PAREN)
    ;

constSpec
    : identifierList (type_? ASSIGN expressionList)?
    ;

varSpec
    : identifierList (type_ (ASSIGN expressionList)? | ASSIGN expressionList)
    ;

expressionList
    : expression (COMMA expression)*
    ;

expression
    : primaryExpr
    | arrayDecl
    | unary_op = (PLUS | MINUS | EXCLAMATION | CARET | STAR | AMPERSAND) expression
    | expression mul_op = (STAR | DIV | MOD | LSHIFT | RSHIFT | LRSHIFT | AMPERSAND) expression
    | expression add_op = (PLUS | MINUS | OR | CARET) expression
    | expression rel_op = (
        EQUALS
        | NOT_EQUALS
        | LESS
        | LESS_OR_EQUALS
        | GREATER
        | GREATER_OR_EQUALS
    ) expression
    | expression LOGICAL_AND expression
    | expression LOGICAL_OR expression
    ;

primaryExpr
    : ( {this.isOperand()}? operand
    | {this.isConversion()}? conversion )
    ( DOT IDENTIFIER | index)*
    ;

index
    : L_BRACKET expression R_BRACKET
    ;

macroStmt
    : IDENTIFIER EXCLAMATION arguments
    ;

methodExpr
    : type_ DOT IDENTIFIER
    ;

conversion
    : type_ L_PAREN expression COMMA? R_PAREN
    ;

operand
    : literal
    | operandName
    | L_PAREN expression R_PAREN
    ;

literal
    : basicLit
    ;

basicLit
    : NIL_LIT
    | integer
    | string_
    | FLOAT_LIT
    ;

string_
    : RAW_STRING_LIT
    | INTERPRETED_STRING_LIT
    ;

integer
    : DECIMAL_LIT
    | BINARY_LIT
    | OCTAL_LIT
    | HEX_LIT
    | IMAGINARY_LIT
    | RUNE_LIT
    ;

operandName
    : IDENTIFIER
    | qualifiedIdent
    ;

qualifiedIdent
    : IDENTIFIER DOT IDENTIFIER
    ;

signature
    : parameters result?
    ;

result
    : type_
    | results
    ;

results
    : L_PAREN (type_ (COMMA type_)*)? R_PAREN
    ;

parameters
    : L_PAREN (parameterDecl (COMMA parameterDecl)*)? R_PAREN
    ;

parameterDecl
    : IDENTIFIER type_
    ;

identifierList
    : IDENTIFIER (COMMA IDENTIFIER)*
    ;

type_
    : IDENTIFIER typeArgs?
    | arrayType
    ;

arrayType
    : L_BRACKET R_BRACKET type_
    ;

typeArgs
    : L_BRACKET typeList COMMA? R_BRACKET
    ;

typeList
    : type_ (COMMA type_)*
    ;

arguments
    : L_PAREN ((expressionList | type_ (COMMA expressionList)?) ELLIPSIS? COMMA?)? R_PAREN
    ;

forLoopStmt
    : FOR loopParameters IN (expression | range) block
    ;

loopParameters
    : IDENTIFIER | L_PAREN IDENTIFIER COMMA IDENTIFIER R_PAREN
    ;

range
    : expression (TO | UNTIL) expression
    ;

eos
    : SEMI
    | EOS
    | {this.closingBracket()}?
    ;
