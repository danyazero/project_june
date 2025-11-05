package com.danyazero;

import june.GoParser;
import june.GoParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JuneVisitor extends GoParserBaseVisitor<String> {
    @Override
    public String visitSourceFile(GoParser.SourceFileContext ctx) {
        return visitClassDeclaration(ctx.classDeclaration());
    }

    @Override
    public String visitBlock(GoParser.BlockContext ctx) {
        var arr = new ArrayList<String>();
        for (var s : ctx.statementList().statement()) {
            arr.add(visit(s));
        }
        return Arrays.toString(arr.toArray());
    }

    @Override
    public String visitClassBodyDeclaration(GoParser.ClassBodyDeclarationContext ctx) {
        if (ctx.functionDecl() != null) {
            return visit(ctx.functionDecl());
        } else if (ctx.methodDecl() != null) {
            return visit(ctx.methodDecl());
        } else if (ctx.declaration() != null) {
            return visit(ctx.declaration());
        }

        throw new RuntimeException("Illegal ClassBodyDeclaration");
    }

    @Override
    public String visitClassDeclaration(GoParser.ClassDeclarationContext ctx) {
        // 1️⃣ Class name
        String name = ctx.identifier().getText();

        var members = new ArrayList<String>();
        for (var el : ctx.classBody().classBodyDeclaration()) {
            members.add(visit(el));
        }
        var membersString = "[\n" + String.join(",\n", members) + "\n]";
        // 2️⃣ Super types (implements / extends)
        List<String> implementsTypes = new ArrayList<>();
        if (ctx.IMPLEMENTS() != null) {
            for (var typeCtx : ctx.typeList(0).type_()) {
                    String typeName = typeCtx.typeName().getText();
                    implementsTypes.add(String.format("Type { name=%s }", typeName));
            }

            return String.format("Object {name=%s , implements=%s, extends=null, members=%s}", name, Arrays.toString(implementsTypes.toArray()), membersString);
        } else if (ctx.EXTENDS() != null) {
            System.out.println();
            return String.format("Object {name=%s , implements=[], extends=%s, members=%s }", name, ctx.type_().typeName().getText(), membersString);
        }

        return String.format("Object {name=%s }", name);
    }


    @Override
    public String visitFunctionDecl(GoParser.FunctionDeclContext ctx) {
        List<String> params = new ArrayList<>();
        if (ctx.signature() != null) {
            var paramCtx = ctx.signature().parameters();
            if (paramCtx != null) {
                for (var p : paramCtx.parameterDecl()) {
                    String paramName = p.identifierList().getText();
                    String type = p.type_() != null ? p.type_().getText() : "any";
                    params.add(String.format("Param { name=%s, type=%s }", paramName, type));
                }
            }
        }


        var res = String.format("Function { name=%s, params=%s block=%s }", ctx.IDENTIFIER().getText(), Arrays.toString(params.toArray()), visit(ctx.block()));
//        System.out.println(res);

        return res;
    }

    @Override
    public String visitShortVarDecl(GoParser.ShortVarDeclContext ctx) {
        String name = ctx.identifierList().getText();
        String value = ctx.expressionList().getText();

//        var value = "";
//        if (value.charAt(0) == '"') {
//            return String.format("Short Var { name=%s, value=String { value=%s } }", name, value.replace("\"", ""));
//        }

//        System.out.println();
        return String.format("Variable { name=%s, value=%s }", name, visitExpression(ctx.expressionList().expression(0)));
    }

    @Override
    public String visitExpressionStmt(GoParser.ExpressionStmtContext ctx) {
        return String.format("Statement { text=%s }", ctx.getText().replace("\"", ""));
    }

    @Override
    public String visitOperand(GoParser.OperandContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        } else if (ctx.operandName() != null) {
            if (ctx.operandName().getText().equals("true") || ctx.operandName().getText().equals("false")) {
                return String.format("Value { value=%s, type=bool }", ctx.operandName().getText());
            }
            return String.format("Operand { name=%s }", ctx.operandName().getText());
        } else if (ctx.expression() != null) {
            return visitExpression(ctx.expression());
        };

        return null;
    }

    @Override
    public String visitExpression(GoParser.ExpressionContext ctx) {
        if (ctx.expression().size() == 2) {
            String left = visitExpression(ctx.expression(0));
            String right = visitExpression(ctx.expression(1));
            String op = null;

            for (ParseTree child : ctx.children) {
                String text = child.getText();
                if (!text.isBlank() && !text.equals("(") && !text.equals(")")) {
                    if (!(child instanceof GoParser.ExpressionContext)) {
                        op = text;
                        break;
                    }
                }
            }

            return String.format("Expression { left=%s, op=%s, right=%s }", left, op, right);
        }

        // Case 2: Unary expression (like -x)
        if (ctx.unary_op != null) {
            String op = ctx.unary_op.getText();
            String inner = visitExpression(ctx.expression(0));
            return String.format("UnaryOperation { operation=%s, inner=%s }", op, inner);
        }

        // Case 3: Simple primary expression (identifier, literal, call, etc.)
        if (ctx.primaryExpr() != null) {
            return visitPrimaryExpr(ctx.primaryExpr());
        }

        throw new RuntimeException("Unhandled expression: " + ctx.getText());
    }

    @Override
    public String visitPrimaryExpr(GoParser.PrimaryExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public String visitInteger(GoParser.IntegerContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitBasicLit(GoParser.BasicLitContext ctx) {
        return String.format("BasicLiteral { value=%s }", visitChildren(ctx));
    }

    @Override
    public String visitLiteral(GoParser.LiteralContext ctx) {
        if (ctx.basicLit() != null) {
            if (ctx.basicLit().string_() != null) {
                String raw = ctx.basicLit().string_().getText();
                String value = raw.substring(1, raw.length() - 1);

                return String.format("Value { value=%s, type=string }", value);
            } else if (ctx.basicLit().integer() != null) {
                return String.format("Value { value=%s, type=int }", ctx.basicLit().integer().getText());
            }
        }

        System.out.println("Unhandled literal: " + ctx.getText());
        return null;
    }
}
