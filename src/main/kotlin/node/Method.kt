package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.model.ReferenceType
import com.danyazero.model.Type
import com.danyazero.type.ArrayType
import com.danyazero.type.VoidType
import com.danyazero.utils.GenerationContext
import com.danyazero.node.Parameter
import com.danyazero.node.TypeNode
import org.objectweb.asm.Opcodes
import java.util.function.Consumer

class Method(
    val name: String,
    val parameters: List<Parameter>,
    val statementList: List<Node>,
    val returnTypes: List<TypeNode>
) : Node {

    override fun produce(ctx: GenerationContext) {
        val descriptor = getDescriptor(ctx)
        val methodVisitor = ctx.getClassWriter()
            .visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, name, descriptor, null, null)

        ctx.setMethodVisitor(methodVisitor)
        ctx.enterScope()
        parameters.forEach { param -> param.produce(ctx) }
        methodVisitor.visitCode()

        statementList.forEach(Consumer { statement: Node -> statement.produce(ctx) })
        ctx.exitScope()

        methodVisitor.visitInsn(Opcodes.RETURN)
        methodVisitor.visitMaxs(8, 8)
        methodVisitor.visitEnd()
    }

    private fun getDescriptor(ctx: GenerationContext): String {
        val parametersDescriptor = getMethodParameters(ctx)
        val returnParametersDescriptor = getMethodReturnParameters(ctx)

        return parametersDescriptor + returnParametersDescriptor
    }

    private fun getMethodReturnParameters(ctx: GenerationContext): String? {
        if (!parameters.isEmpty()) {
            val type: Type<*> = if (returnTypes.isEmpty()) VoidType() else returnTypes.first().type
            if (type is VoidType) {
                return "V"
            }
             else if (type is ReferenceType<*>) {
                return getParameter(type, ctx)
            }
        }

        return null
    }

    private fun getMethodParameters(ctx: GenerationContext): String? {
        if (!parameters.isEmpty()) {
            val parametersList = ArrayList<String?>()
            for (param in parameters) {
                parametersList.add(getParameter(param.type, ctx))
            }

            return "(" + parametersList.joinToString(", ") + ")"
        }

        return null
    }

    private fun getParameter(type: Type<*>, ctx: GenerationContext): String? {
        if (type is ArrayType) {
            return "[" + getParameter(type.child, ctx)
        } else if (type is ReferenceType<*>) {
            return "L" + type.getName() + ";"
        }

        return null
    }

    override fun toString(): String {
        return "Method(name='$name', parameters=$parameters, statementList=$statementList, returnTypes=$returnTypes)"
    }
}