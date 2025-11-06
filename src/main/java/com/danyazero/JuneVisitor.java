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
        return visit(ctx.objectDeclaration());
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
        String name = ctx.identifier().getText();

        var members = new ArrayList<String>();
        for (var el : ctx.classBody().classBodyDeclaration()) {
            members.add(visit(el));
        }
        var membersString = "[\n" + String.join(",\n", members) + "\n]";

        List<String> implementsTypes = new ArrayList<>();
        if (ctx.IMPLEMENTS() != null) {
            for (var typeCtx : ctx.typeList().type_()) {
                    implementsTypes.add(visitType_(typeCtx));
            }

            return String.format("Object {name=%s , implements=%s, extends=null, members=%s}", name, Arrays.toString(implementsTypes.toArray()), membersString);
        } else if (ctx.EXTENDS() != null) {
            System.out.println();
            return String.format("Object {name=%s , implements=[], extends=%s, members=%s }", name, ctx.type_().typeName().getText(), membersString);
        }

        return String.format("Object {name=%s }", name);
    }


    @Override
    public String visitType_(GoParser.Type_Context ctx) {
        if (ctx.typeName() != null) {
            var typeObj = getType(ctx.typeName().getText());
            return String.format("Type { name=%s }", typeObj);
        } else if (ctx.typeLit() != null) {
            return visitTypeLit(ctx.typeLit());
        }

        throw new RuntimeException("Illegal Type");
    }

    @Override
    public String visitTypeLit(GoParser.TypeLitContext ctx) {
        if (ctx.arrayType() != null) {
            return String.format("Array { value=%s }", visitType_(ctx.arrayType().elementType().type_()));
        }
        throw new RuntimeException("Illegal Lit Type");
    }

    @Override
    public String visitReturnStmt(GoParser.ReturnStmtContext ctx) {
        return String.format("ReturnStmt { expression=%s }", visit(ctx.expressionList()));
    }

    @Override
    public String visitFunctionDecl(GoParser.FunctionDeclContext ctx) {
        List<String> params = new ArrayList<>();
        var resultParameters = new ArrayList<String>();
        if (ctx.signature() != null) {
            var paramCtx = ctx.signature().parameters();
            if (paramCtx != null) {
                for (var p : paramCtx.parameterDecl()) {
                    String paramName = p.identifierList().getText();
                    String type = p.type_() != null ? p.type_().getText() : "any";
                    params.add(String.format("Param { name=%s, type=%s }", paramName, getType(type)));
                }
            }
            var resultCtx = ctx.signature().result();
            if (resultCtx != null) {
                if (resultCtx.type_() != null) {
                    var type = visitTypeLit(resultCtx.type_().typeLit());
                    resultParameters.add(String.format("Param { type=%s }", type));
                } else if (resultCtx.parameters() != null) {
                    for (var param : resultCtx.parameters().parameterDecl()) {
                        var type = visitTypeLit(param.type_().typeLit());
                        resultParameters.add(String.format("Param { type=%s }", type));
                    }
                }
            }
        }

        return String.format(
                "Function { name=%s, params=%s block=%s, result=%s }",
                ctx.IDENTIFIER().getText(),
                Arrays.toString(params.toArray()),
                visit(ctx.block()),
                resultParameters
        );
    }

    @Override
    public String visitStatementList(GoParser.StatementListContext ctx) {
        var blocks = new ArrayList<String>();
        for (var el : ctx.statement()) {
            blocks.add(visit(el));
        }

        return Arrays.toString(blocks.toArray());
    }

    @Override
    public String visitIfStmt(GoParser.IfStmtContext ctx) {
        var statementExpression = visitExpression(ctx.expression());

        var statementBlock = new ArrayList<String>();
        for (var block : ctx.block()) {
            statementBlock.add(visit(block.statementList()));
        }


        return String.format("IfStmt { expression=%s , block=%s }", statementExpression, Arrays.toString(statementBlock.toArray()));
    }



    @Override
    public String visitShortVarDecl(GoParser.ShortVarDeclContext ctx) {
        return String.format(
                "Variable { name=%s, value=%s }",
                ctx.identifierList().getText(),
                visitExpression(ctx.expressionList().expression(0))
        );
    }

    @Override
    public String visitExpressionStmt(GoParser.ExpressionStmtContext ctx) {
        return String.format("Statement { text=%s }", ctx.getText().replace("\"", ""));
    }

    @Override
    public String visitConstSpec(GoParser.ConstSpecContext ctx) {
        return String.format(
                "Const { name=%s, type=%s, value=%s }",
                ctx.identifierList().getText(),
                visitType_(ctx.type_()),
                visitExpression(ctx.expressionList().expression(0))
        );
    }

    @Override
    public String visitOperand(GoParser.OperandContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        } else if (ctx.operandName() != null) {
            if (ctx.operandName().getText().equals("true") || ctx.operandName().getText().equals("false")) {
                return String.format("Value { value=%s, type=Bool {  } }", ctx.operandName().getText());
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

        if (ctx.unary_op != null) {
            String op = ctx.unary_op.getText();
            String inner = visitExpression(ctx.expression(0));
            return String.format("UnaryOperation { operation=%s, inner=%s }", op, inner);
        }

        if (ctx.primaryExpr() != null) {
            return visitPrimaryExpr(ctx.primaryExpr());
        }

        throw new RuntimeException("Unhandled expression: " + ctx.getText());
    }

    @Override
    public String visitAssignment(GoParser.AssignmentContext ctx) {
        var operation = ctx.assign_op();
        System.out.println(operation.ASSIGN() + " " + operation.PLUS() + " " + operation.MINUS());
        if (operation.ASSIGN() != null) {
            var left = visit(ctx.expressionList(0).expression(0));
            var right = visit(ctx.expressionList(1).expression(0));

            if (operation.PLUS() != null) {
                var increment = String.format("Increment { operand=%s, value=%s }", left, right);

                return String.format("Assignment { operand=%s, expr=%s }", left, increment);
            } else if (operation.MINUS() != null) {
                var increment = String.format("Increment { operand=%s, value=Negate { value=%s } }", left, right);

                return String.format("Assignment { operand=%s, expr=%s }", left, increment);
            } else if (operation.STAR() != null) {
                var multiplication = String.format("Expression { left=%s, op=*, right=%s}", left, right);

                return String.format("Assignment { operand=%s, expr=%s }", left, multiplication);
            } else if (operation.DIV() != null) {
                var division = String.format("Expression { left=%s, op=/, right=%s}", left, right);

                return String.format("Assignment { operand=%s, expr=%s }", left, division);
            } else if (operation.MOD() != null) {
                var mod = String.format("Expression { left=%s, op=%%, right=%s}", left, right);

                return String.format("Assignment { operand=%s, expr=%s }", left, mod);
            } else if (operation.RSHIFT() != null) {
                var rightShift = String.format("Expression { left=%s, op=>>, right=%s}", left, right);

                return String.format("Assignment { operand=%s, expr=%s }", left, rightShift);
            } else if (operation.LSHIFT() != null) {
                var leftShift = String.format("Expression { left=%s, op=<<, right=%s}", left, right);

                return String.format("LShiftAssignment { operand=%s, expr=%s }", left, leftShift);
            } else {
                return String.format("Assignment { operand=%s, expr=%s }", left, right);
            }
        }

        throw new RuntimeException("Unhandled assignment: " + ctx.getText());
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

                return String.format("Value { value=%s, type=String {  } }", value);
            } else if (ctx.basicLit().integer() != null) {
                return String.format("Value { value=%s, type=Integer {  } }", ctx.basicLit().integer().getText());
            } else if (ctx.basicLit().getText().contains(".")) {
                return String.format("Value { value=%s, type=Double {  } }", ctx.basicLit().getText());
            }
        }

        System.out.println("Unhandled literal: " + ctx.getText());
        return null;
    }

    private static String getType(String type) {
        return switch (type) {
            case "int" -> "Integer {  }";
            case "bool" -> "Bool {  }";
            case "string" -> "String {  }";
            default -> "Object {  }";
        };
    }
}
