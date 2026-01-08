package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.model.PrimitiveType
import com.danyazero.model.ReferenceType
import com.danyazero.model.Type
import com.danyazero.type.ArrayType
import com.danyazero.type.VoidType
import com.danyazero.utils.GenerationContext

class Signature(
    val name: String,
    val parameters: List<Expression>,
    val returnType: TypeNode = TypeNode(VoidType()),
) : Node {
    override fun produce(ctx: GenerationContext) {
        parameters.forEach { it.produce(ctx) }
    }

    fun getSignature(ctx: GenerationContext) : String {
        if (parameters.isEmpty()) return "$name()"

        val parametersDescriptor = getMethodParameters(ctx)

        return "$name$parametersDescriptor"
    }

    fun getDescriptor(ctx: GenerationContext): String {
        if (parameters.isEmpty() && returnType.type is VoidType) return "()V"

        val parametersDescriptor = getMethodParameters(ctx)
        val returnParametersDescriptor = getMethodReturnParameters()

        return parametersDescriptor + returnParametersDescriptor
    }

    private fun getMethodReturnParameters(): String? {
        return getParameter(returnType.type)
    }

    private fun getMethodParameters(ctx: GenerationContext): String? {
        val parametersList = ArrayList<String?>()
        for (param in parameters) {
            parametersList.add(getParameter(param.getType(ctx)))
        }

        return "(" + parametersList.joinToString("") + ")"
    }

    private fun getParameter(type: Type<*>): String {
        return when (type) {
            is VoidType -> "V"
            is PrimitiveType -> type.getDescriptor()
            is ArrayType -> "[" + getParameter(type.child)
            is ReferenceType<*> -> "L" + type.getName() + ";"
            else -> ""
        }
    }

    override fun toString(): String {
        return "Signature(parameters=$parameters, returnTypes=$returnType)"
    }
}