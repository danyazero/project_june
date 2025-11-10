package com.danyazero;

import com.danyazero.model.Operation;
import com.danyazero.model.ast.*;
import com.danyazero.utils.*;
import june.GoParser;
import june.GoParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JuneVisitor extends GoParserBaseVisitor<Node> {
    @Override
    public Node visitSourceFile(GoParser.SourceFileContext ctx) {
        return visitObjectDeclaration(ctx.objectDeclaration());
    }

    @Override
    public NodeList visitBlock(GoParser.BlockContext ctx) {
        var statementList = new ArrayList<Node>();
        for (var s : ctx.statementList().statement()) {
            statementList.add(visit(s));
        }

        return new NodeList(statementList);
    }

    @Override
    public Node visitClassBodyDeclaration(GoParser.ClassBodyDeclarationContext ctx) {
        if (ctx.functionDecl() != null) {
            return visitFunctionDecl(ctx.functionDecl());
        } else if (ctx.methodDecl() != null) {
            return visit(ctx.methodDecl());
        } else if (ctx.classFields() != null) {
            return visit(ctx.classFields());
        }

        throw new RuntimeException("Illegal ClassBodyDeclaration");
    }

    @Override
    public JuneClass visitObjectDeclaration(GoParser.ObjectDeclarationContext ctx) {
        String name = ctx.classDeclaration().identifier().getText();


        var members = new ArrayList<Node>();
        for (var el : ctx.classDeclaration().classBody().classBodyDeclaration()) {
            members.add(visitClassBodyDeclaration(el));
        }

//        var implementsTypes = new ArrayList<Node>();
        if (ctx.classDeclaration().IMPLEMENTS() != null) {
//            for (var typeCtx : ctx.classDeclaration().typeList().type_()) {
//                implementsTypes.add(visitType_(typeCtx));
//            }

            return new JuneClass(name, members);

//            visitAnnotationList(ctx.annotationList()), // Way to get annotation list
//            Arrays.toString(implementsTypes.toArray()), // Way to get types which this class implements
        } else if (ctx.classDeclaration().EXTENDS() != null) {
            return new JuneClass(name, members);
//            ctx.classDeclaration().type_().typeName().getText(), //Way to get type which this class extends
        }


        return new JuneClass(name, members);
    }

    @Override
    public Node visitModifier(GoParser.ModifierContext ctx) {
        if (ctx.PUBLIC() != null) {
            return null; //TODO Provide modifier support
        }

        throw new RuntimeException("Illegal Modifier");
    }

    @Override
    public Node visitAnnotation(GoParser.AnnotationContext ctx) {

        var params = new ArrayList<String>();
        if (ctx.normalAnnotation().elementValuePairList() != null) {
            for (var param : ctx.normalAnnotation().elementValuePairList().elementValuePair()) {
                params.add(String.format("Pair { name=%s, value=%s }", param.identifier(), visit(param.elementValue().basicLit())));
            }
        }

//        return String.format("Annotation { name=%s, values=%s }", ctx.normalAnnotation().typeName().IDENTIFIER(), Arrays.toString(params.toArray()));
        throw new RuntimeException("Not implemented yet."); //TODO Annotation visitor
    }

    @Override
    public Node visitAnnotationList(GoParser.AnnotationListContext ctx) {
        throw new RuntimeException("Not implemented yet."); //TODO Annotation list

//        var annotations = new ArrayList<Node>();
//        for (var annotation : ctx.annotation()) {
//            annotations.add(visitAnnotation(annotation));
//        }

//        return Arrays.toString(annotations.toArray());
    }

    @Override
    public TypeNode visitType_(GoParser.Type_Context ctx) {
        if (ctx.typeName() != null) {
            var typeObj = getType(ctx.typeName().getText());

            return new TypeNode(typeObj);
        } else if (ctx.typeLit() != null) {
            return visitTypeLit(ctx.typeLit());
        }

        throw new RuntimeException("Illegal Type");
    }

    @Override
    public TypeNode visitTypeLit(GoParser.TypeLitContext ctx) {
        if (ctx.arrayType() != null) {
            var type = visitType_(ctx.arrayType().elementType().type_());
            if (type instanceof TypeNode typeNode) {
                return new TypeNode(new ArrayType(typeNode.getType()));
            }

            throw new RuntimeException("Incorrect type format");
        }

        throw new RuntimeException("Illegal Lit Type");
    }

    @Override
    public ReturnStatement visitReturnStmt(GoParser.ReturnStmtContext ctx) {
        return new ReturnStatement(visit(ctx.expressionList()));
    }

    @Override
    public TypeList visitSignature(GoParser.SignatureContext ctx) {
        var resultParameters = new ArrayList<TypeNode>();
        if (ctx.result() != null) {
            if (ctx.result().type_() != null) {
                var type = visitType_(ctx.result().type_());
                resultParameters.add(type);
            } else if (ctx.result().parameters() != null) {
                for (var param : ctx.result().parameters().parameterDecl()) {
                    var type = visitTypeLit(param.type_().typeLit());
                    resultParameters.add(type);
                }
            }
        }

        return new TypeList(resultParameters);
    }

    @Override
    public Method visitFunctionDecl(GoParser.FunctionDeclContext ctx) {
        List<MethodParameter> params = new ArrayList<>();
        if (ctx.signature() != null) {
            var paramCtx = ctx.signature().parameters();
            if (paramCtx != null) {
                for (var p : paramCtx.parameterDecl()) {
                    String paramName = p.identifierList().getText();
//                    String type = p.type_() != null ?  : ;
                    var parameter = new MethodParameter(paramName, visitType_(p.type_()).getType());
                    params.add(parameter);
                }
            }
        }

        return new Method(
                ctx.IDENTIFIER().getText(),
                params,
                visitBlock(ctx.block()).getStatements(),
                visitSignature(ctx.signature()).getStatements()
        );
    }

    @Override
    public Node visitStatementList(GoParser.StatementListContext ctx) {
        var blocks = new ArrayList<Node>();
        for (var el : ctx.statement()) {
            blocks.add(visit(el));
        }

//        return Arrays.toString(blocks.toArray());
        return null;
    }

    //TODO Fix visitIfStatement method
//    @Override
//    public Node visitIfStmt(GoParser.IfStmtContext ctx) {
//        var statementExpression = visitExpression(ctx.expression());
//
//        var statementBlock = new ArrayList<String>();
//        for (var block : ctx.block()) {
//            statementBlock.add(visit(block.statementList()));
//        }
//
//
//        return String.format("IfStmt { expression=%s , block=%s }", statementExpression, Arrays.toString(statementBlock.toArray()));
//    }


    @Override
    public Node visitShortVarDecl(GoParser.ShortVarDeclContext ctx) {
        var expression = visitExpression(ctx.expressionList().expression(0));

        return new Variable(
                ctx.identifierList().getText(),
                expression.getType(),
                expression
        );
    }

    @Override
    public Node visitExpressionStmt(GoParser.ExpressionStmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Node visitConstSpec(GoParser.ConstSpecContext ctx) {
        throw new RuntimeException("Not implemented yet."); //TODO Implement constant variable
//        return String.format(
//                "Const { name=%s, type=%s, value=%s }",
//                ctx.identifierList().getText(),
//                visitType_(ctx.type_()),
//                visitExpression(ctx.expressionList().expression(0))
//        );
    }

    @Override
    public Node visitVarSpec(GoParser.VarSpecContext ctx) {

        return new Variable(
                ctx.identifierList().getText(),
                visitType_(ctx.type_()).getType(),
                visitExpression(ctx.expressionList().expression(0))
        );
    }

    @Override
    public Expression visitOperand(GoParser.OperandContext ctx) {
        if (ctx.literal() != null) {
            return visitLiteral(ctx.literal());
        } else if (ctx.operandName() != null) {
            if (ctx.operandName().getText().equals("true") || ctx.operandName().getText().equals("false")) {
                return new Value<>(Boolean.parseBoolean(ctx.operandName().getText()), new BooleanType());
            }

            return new Operand(ctx.operandName().getText());
        } else if (ctx.expression() != null) {
            return visitExpression(ctx.expression());
        }

        throw new RuntimeException("Provided wrong expression or operand");
    }

    @Override
    public Expression visitExpression(GoParser.ExpressionContext ctx) {
        if (ctx.expression().size() == 2) {
            var left = visitExpression(ctx.expression(0));
            var right = visitExpression(ctx.expression(1));
            Operation op = null;

            for (ParseTree child : ctx.children) {
                String text = child.getText();
                if (!text.isBlank() && !text.equals("(") && !text.equals(")")) {
                    if (!(child instanceof GoParser.ExpressionContext)) {
                        op = Operation.fromString(text);
                        break;
                    }
                }
            }

            return new ExpressionNode(op, left, right);
        }

        if (ctx.unary_op != null) {
            String op = ctx.unary_op.getText();
            var inner = visitExpression(ctx.expression(0));

            throw new RuntimeException("Not implemented yet."); //TODO Implement Unary operation processing
//            return String.format("UnaryOperation { operation=%s, inner=%s }", op, inner);
        } else if (ctx.primaryExpr() != null) {
//            throw new RuntimeException("Not implemented yet."); //TODO Implement primaryExpression
            return visitPrimaryExpr(ctx.primaryExpr());
        } else if (ctx.shortSliceDecl() != null) {
            return visitShortSliceDecl(ctx.shortSliceDecl());
        }

        throw new RuntimeException("Unhandled expression: " + ctx.getText());
    }

    @Override
    public Node visitAssignment(GoParser.AssignmentContext ctx) {
        var operation = ctx.assign_op();
        throw new RuntimeException("Not implemented yet."); //TODO Implement assignment

//        if (operation.ASSIGN() != null) {
//            var left = visit(ctx.expressionList(0).expression(0));
//            var right = visit(ctx.expressionList(1).expression(0));
//
//            if (operation.PLUS() != null) {
//                var increment = String.format("Increment { operand=%s, value=%s }", left, right);
//
//                return String.format("Assignment { operand=%s, expr=%s }", left, increment);
//            } else if (operation.MINUS() != null) {
//                var increment = String.format("Increment { operand=%s, value=Negate { value=%s } }", left, right);
//
//                return String.format("Assignment { operand=%s, expr=%s }", left, increment);
//            } else if (operation.STAR() != null) {
//                var multiplication = String.format("Expression { left=%s, op=*, right=%s}", left, right);
//
//                return String.format("Assignment { operand=%s, expr=%s }", left, multiplication);
//            } else if (operation.DIV() != null) {
//                var division = String.format("Expression { left=%s, op=/, right=%s}", left, right);
//
//                return String.format("Assignment { operand=%s, expr=%s }", left, division);
//            } else if (operation.MOD() != null) {
//                var mod = String.format("Expression { left=%s, op=%%, right=%s}", left, right);
//
//                return String.format("Assignment { operand=%s, expr=%s }", left, mod);
//            } else if (operation.RSHIFT() != null) {
//                var rightShift = String.format("Expression { left=%s, op=>>, right=%s}", left, right);
//
//                return String.format("Assignment { operand=%s, expr=%s }", left, rightShift);
//            } else if (operation.LSHIFT() != null) {
//                var leftShift = String.format("Expression { left=%s, op=<<, right=%s}", left, right);
//
//                return String.format("LShiftAssignment { operand=%s, expr=%s }", left, leftShift);
//            } else {
//                return String.format("Assignment { operand=%s, expr=%s }", left, right);
//            }
//        }
//
//        throw new RuntimeException("Unhandled assignment: " + ctx.getText());
    }

    @Override
    public ExpressionList visitArguments(GoParser.ArgumentsContext ctx) {
        var arguments = new ArrayList<Expression>();
        for (var el : ctx.expressionList().expression()) {
            arguments.add(visitExpression(el));
        }
        return new ExpressionList(arguments);
    }

    @Override
    public Expression visitPrimaryExpr(GoParser.PrimaryExprContext ctx) {
        if (ctx.methodExpr() != null) {
            var object = ctx.methodExpr().type_().typeName().getText();
            var method = ctx.methodExpr().IDENTIFIER().getText();

            var arguments = new ArrayList<ExpressionList>();
            for (var argument : ctx.arguments()) {
                arguments.add(visitArguments(argument));
            }
            System.out.println("Method arguments: " + arguments);

//            throw new RuntimeException("Method expression not implemented yet");

            return new MethodInvoke(
                    object + "." + method,
                    arguments.getFirst().getExpressions()
            );
//            return String.format(
//                    "MethodInvoke { method=%s, params=%s }",
//                    object + "." + method,
//                    Arrays.toString(arguments.toArray())
//            );
        } else if (ctx.operand() != null && ctx.index() != null && !ctx.index().isEmpty()) {
            return new SliceElement(
                    visitOperand(ctx.operand()),
                    visitOperand(ctx.index(0).expression().primaryExpr().operand())
            );
        }

        return (Expression) visitChildren(ctx);
//        throw new RuntimeException("This kind of primary expression is not supported");
    }

    @Override
    public Value<?> visitLiteral(GoParser.LiteralContext ctx) {
        if (ctx.basicLit() != null) {
            if (ctx.basicLit().string_() != null) {
                String raw = ctx.basicLit().string_().getText();
                String value = raw.substring(1, raw.length() - 1);

                return new Value<>(value, new StringType());
            } else if (ctx.basicLit().integer() != null) {
                return new Value<>(Integer.parseInt(ctx.basicLit().integer().getText()), new IntegerType());
            } else if (ctx.basicLit().getText().contains(".")) {
                return new Value<>(Double.parseDouble(ctx.basicLit().getText()), new DoubleType());
            }
        }

        throw new RuntimeException("Unhandled literal: " + ctx.getText());
    }

    @Override
    public Expression visitShortSliceDecl(GoParser.ShortSliceDeclContext ctx) {
        var items = new ArrayList<Expression>();
        for (var item : ctx.literalList().expression()) {
            items.add(visitExpression(item));
        }

        return new Slice(items);
    }

    private static com.danyazero.model.Type<?> getType(String type) {
        System.out.println("getType(" + type + ");");
        var res = switch (type) {
            case "int" -> new IntegerType();
            case "bool" -> new BooleanType();
            case "string" -> new StringType();
            default -> new ObjectType(null); //TODO Provide correct object name
        };
        System.out.println("return " + res.getClass());

        return res;
    }
}
