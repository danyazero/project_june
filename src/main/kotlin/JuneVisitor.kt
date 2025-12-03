package com.danyazero

import com.danyazero.expression.*
import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.model.ToRange
import com.danyazero.model.Type
import com.danyazero.model.UntilRange
import com.danyazero.node.*
import com.danyazero.node.Array
import com.danyazero.type.*
import june.JuneParser
import june.JuneParserBaseVisitor
import org.objectweb.asm.Opcodes

class JuneVisitor : JuneParserBaseVisitor<Node>() {
    override fun visitSourceFile(ctx: JuneParser.SourceFileContext): Node {
        return visitClassDeclaration(ctx.classDeclaration())
    }

    override fun visitClassDeclaration(ctx: JuneParser.ClassDeclarationContext): Node {
        return Class(
            name = ctx.identifier().text,
            members = visitClassBody(ctx.classBody()).nodes
        )
    }

    override fun visitClassBody(ctx: JuneParser.ClassBodyContext): NodeList {
        return NodeList(ctx.classBodyDeclaration().map { visitClassBodyDeclaration(it) })
    }

    override fun visitClassBodyDeclaration(ctx: JuneParser.ClassBodyDeclarationContext): Node {
        if (ctx.functionDecl() != null) {
            return visitFunctionDecl(ctx.functionDecl())
        }

        throw RuntimeException("Unsupported class member")
    }

    override fun visitFunctionDecl(ctx: JuneParser.FunctionDeclContext): Node {
        val resultType = if (ctx.signature().result() != null) {
            visitResult(ctx.signature().result()).types
        } else {
            listOf()
        }
        var modifiers = ctx.modifier().map {
            when  {
                it.PUBLIC() != null -> Opcodes.ACC_PUBLIC
                else -> Opcodes.ACC_PRIVATE
            }
        }.sum()

        if (ctx.STAR() != null) {
            modifiers += Opcodes.ACC_STATIC
        }

        return Method(
            name = ctx.IDENTIFIER().text,
            modifiers = modifiers,
            returnTypes = resultType,
            parameters = visitParameters(ctx.signature().parameters()).parameters,
            statementList = visitBlock(ctx.block()).nodes
        )
    }

    override fun visitBlock(ctx: JuneParser.BlockContext): NodeList {
        return NodeList(ctx.statementList().statement().map { visit(it) })
    }

    override fun visitResult(ctx: JuneParser.ResultContext): TypeList {
        return if (ctx.type_() != null) {
            TypeList(listOf(visitType_(ctx.type_())))
        } else if (ctx.results() != null) {
            TypeList(ctx.results().type_().map { visitType_(it) })
        } else throw RuntimeException("Unexpected signature")
    }

    override fun visitParameters(ctx: JuneParser.ParametersContext): ParameterList {
        return ParameterList(ctx.parameterDecl().map { visitParameterDecl(it) })
    }

    override fun visitParameterDecl(ctx: JuneParser.ParameterDeclContext): Parameter {
        return Parameter(name = ctx.IDENTIFIER().text, type = visitType_(ctx.type_()).type)
    }

    override fun visitVarSpec(ctx: JuneParser.VarSpecContext): Node {
        return Variable(
            name = ctx.identifierList().getText(),
            type = if (ctx.type_() != null) visitType_(ctx.type_()).type else null,
            value = visitExpression(ctx.expressionList().expression(0)),
        )
    }

    override fun visitConstSpec(ctx: JuneParser.ConstSpecContext): Node {
        return Variable(
            name = ctx.identifierList().getText(),
            type = if (ctx.type_() != null) visitType_(ctx.type_()).type else null,
            value = visitExpression(ctx.expressionList().expression(0)),
            isConstant = true
        )
    }

    override fun visitShortVarDecl(ctx: JuneParser.ShortVarDeclContext): Node {
        return Variable(
            name = ctx.identifierList().getText(),
            value = visitExpression(ctx.expressionList().expression(0))
        )
    }

    override fun visitType_(ctx: JuneParser.Type_Context): TypeNode {
        if (ctx.IDENTIFIER() != null) {
            return TypeNode(getType(ctx.IDENTIFIER().text))
        } else if (ctx.arrayType() != null) {
            return TypeNode(ArrayType(visitType_(ctx.arrayType().type_()).type))
        }

        throw RuntimeException("Illegal Type")
    }

    override fun visitMacroStmt(ctx: JuneParser.MacroStmtContext): Node {
        return if (ctx.IDENTIFIER().text == "print") {
            PrintMacros(visitExpression(ctx.arguments().expressionList().expression(0)))
        } else throw RuntimeException("Unsupported macros")
    }

    override fun visitExpressionList(ctx: JuneParser.ExpressionListContext): ExpressionList {
        return ExpressionList(ctx.expression().map { visitExpression(it) })
    }

    override fun visitExpression(ctx: JuneParser.ExpressionContext): Expression {
        if (ctx.expression().size == 2) {
            val left = visitExpression(ctx.expression(0))
            val right = visitExpression(ctx.expression(1))

            val expression = when {
                ctx.PLUS() != null -> ::AdditionExpression
                ctx.STAR() != null -> ::MultiplicationExpression
                ctx.DIV() != null -> ::DivisionExpression
                ctx.MINUS() != null -> ::SubstructionExpression
                ctx.LSHIFT() != null -> ::ShiftLeftExpression
                ctx.RSHIFT() != null -> ::ShiftRightExpression
                ctx.LRSHIFT() != null -> ::LogicalShiftRightExpression
                ctx.MOD() != null -> ::RemainderExpression
                ctx.EQUALS() != null -> ::EqualExpression
                ctx.LESS() != null -> ::LessExpression
                ctx.GREATER() != null -> ::GreaterExpression
                ctx.LESS_OR_EQUALS() != null -> ::LessOrEqualExpression
                ctx.GREATER_OR_EQUALS() != null -> ::GreaterOrEqualExpression
                else -> throw RuntimeException("Unsupported expression operand")
            }

            return expression(left, right)
        } else if (ctx.arrayDecl() != null) {
            return visitArrayDecl(ctx.arrayDecl())
        } else if (ctx.primaryExpr() != null) {
            return visitPrimaryExpr(ctx.primaryExpr())
        }

        throw RuntimeException("Unsupported expression")
    }

    override fun visitAssignment(ctx: JuneParser.AssignmentContext): AssignExpression {
        val operation = ctx.assign_op()

        if (operation.ASSIGN() != null) {
            val operand = visitExpression(ctx.expression(0))
            if (operand is Operand) {
                val right = visitExpression(ctx.expression(1))


                val expression = when {
                    operation.PLUS() != null -> ::AdditionExpression
                    operation.MINUS() != null -> ::SubstructionExpression
                    operation.STAR() != null -> ::MultiplicationExpression
                    operation.DIV() != null -> ::DivisionExpression
                    operation.MOD() != null -> ::RemainderExpression
                    else -> null
                }

                if (expression != null) {
                    return AssignExpression(operand = operand, expression = expression(operand, right))
                }

                return AssignExpression(operand = operand, expression = right)
            }

            throw RuntimeException("Left expression should be Operand { }: " + ctx.getText())
        }


        throw RuntimeException("Unhandled assignment: " + ctx.getText())
    }

    override fun visitLoopStmt(ctx: JuneParser.LoopStmtContext): Node {
        if (ctx.expression() != null && ctx.expression().rel_op != null) {
            val conditionExpression = visitExpression(ctx.expression())

            if (conditionExpression is ConditionExpression) {
                return Loop(condition = conditionExpression, statements = visitBlock(ctx.block()).nodes)
            } else {
                throw RuntimeException("Unhandled loop condition expression")
            }
        }

        return Loop(statements = visitBlock(ctx.block()).nodes)
    }

    override fun visitRangeLoopStmt(ctx: JuneParser.RangeLoopStmtContext): Node {


        val left = visitExpression(ctx.range().expression(0))
        val right = visitExpression(ctx.range().expression(1))

        val range = when {
            ctx.range().UNTIL() != null -> ::UntilRange
            ctx.range().TO() != null -> ::ToRange
            else -> throw RuntimeException("Unhandled range expression")
        }

        return RangeLoop(
            index = ctx.IDENTIFIER().text,
            range = range(left, right),
            statements = visitBlock(ctx.block()).nodes
        )
    }

    override fun visitOperandLoopStmt(ctx: JuneParser.OperandLoopStmtContext): Node {
        if (ctx.expression() != null) {
            val operand = visitExpression(ctx.expression())

            return if (ctx.loopParameters() == null) {
                OperandLoop(operand = operand, statements = visitBlock(ctx.block()).nodes)
            } else if (ctx.loopParameters().IDENTIFIER().size == 2) {
                OperandLoop(
                    operand = operand,
                    index = ctx.loopParameters().IDENTIFIER(0).text,
                    item = ctx.loopParameters().IDENTIFIER(1).text,
                    statements = visitBlock(ctx.block()).nodes
                )
            } else {
                OperandLoop(operand = operand, item = ctx.loopParameters().IDENTIFIER(0).text, statements = visitBlock(ctx.block()).nodes)
            }
        }

        throw RuntimeException("Unsupported loop")
    }

    override fun visitIncDecStmt(ctx: JuneParser.IncDecStmtContext): Node {
        val operand = visitExpression(ctx.expression())
        if (operand !is Operand) throw RuntimeException("Unhandled operand type.")

        return when {
            ctx.PLUS_PLUS() != null -> AssignExpression(
                operand = operand,
                expression = AdditionExpression(left = operand, right = ValueExpression.of(1))
            )

            ctx.MINUS_MINUS() != null -> AssignExpression(
                operand = operand,
                expression = SubstructionExpression(left = operand, right = ValueExpression.of(1))
            )

            else -> throw RuntimeException("Unsupported expression operand")
        }
    }

    override fun visitArrayDecl(ctx: JuneParser.ArrayDeclContext): Array {
        val expressionList = visitExpressionList(ctx.expressionList())

        return Array(
            items = expressionList.items
        )
    }

    override fun visitPrimaryExpr(ctx: JuneParser.PrimaryExprContext): Expression {
        if (ctx.operand() != null && ctx.index() != null && !ctx.index().isEmpty()) {
            return ArrayValue(
                visitOperand(ctx.operand()),
                visitExpression(ctx.index(0).expression())
            )
        } else if (ctx.operand() != null) {
            return visitOperand(ctx.operand())
        }

        throw RuntimeException("Unsupported primary expression")
    }

    override fun visitOperand(ctx: JuneParser.OperandContext): Expression {
        if (ctx.literal() != null) {
            return visitLiteral(ctx.literal())
        } else if (ctx.operandName() != null) {
            if (ctx.operandName().text == "true" || ctx.operandName().text == "false") {
                return ValueExpression(
                    ctx.operandName()?.text.toBoolean(),
                    BooleanType()
                )
            }

            return Operand(ctx.operandName().getText())
        } else if (ctx.expression() != null) {
            return visitExpression(ctx.expression())
        }

        throw RuntimeException("Provided wrong expression or operand");
    }

    override fun visitLiteral(ctx: JuneParser.LiteralContext): Expression {
        if (ctx.basicLit() != null) {
            if (ctx.basicLit().string_() != null) {
                val raw = ctx.basicLit().string_().getText()
                val value = raw.substring(1, raw.length - 1)

                return ValueExpression(value, StringType())
            } else if (ctx.basicLit().integer() != null) {
                return ValueExpression(Integer.parseInt(ctx.basicLit().integer().text), IntegerType())
            } else if (ctx.basicLit().getText().contains(".")) {
                return ValueExpression(ctx.basicLit().text.toDouble(), DoubleType())
            }
        }

        throw RuntimeException("Unhandled literal: " + ctx.getText())
    }

    private fun getType(type: String): Type<*> {
        return when (type) {
            "int" -> IntegerType()
            "bool" -> BooleanType()
            "string" -> StringType()
            else -> ObjectType(null)
        }
    }

}