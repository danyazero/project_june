package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.type.IntegerType
import com.danyazero.type.StringType
import com.danyazero.utils.GenerationContext

import org.objectweb.asm.Opcodes.GETSTATIC;
import org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

class PrintlnMacros(
    val value: Expression
) : Node {

    override fun produce(ctx: GenerationContext) {
        ctx.getMethodVisitor().visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        value.produce(ctx)
        ctx.getMethodVisitor().visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", this.getDescriptor(ctx), false)
    }

    fun getDescriptor(ctx: GenerationContext) : String {
        return when (value.getType(ctx)) {
            is IntegerType -> "(I)V"
            else -> "(Ljava/lang/String;)V"
        }
    }

    override fun toString(): String {
        return "PrintlnMacros(value=$value)"
    }

}