package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.NumberType
import com.danyazero.type.StringType
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes

class AdditionExpression(
    left: Expression,
    right: Expression
) : BinaryExpression(left, right) {

    override fun produce(ctx: GenerationContext) {
        this.apply(ctx) {
            when (it) {
                is NumberType<*> -> {
                    it.add(ctx.getMethodVisitor())
                }

                is StringType -> {
                    val bsm = getStringConcatStaticRef()
                    ctx.getMethodVisitor().visitInvokeDynamicInsn(
                        "makeConcat",
                        "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
                        bsm
                    )
                }
                else -> {
                    throw RuntimeException("Unsupported expression type.")
                }
            }
        }
    }

    companion object {
        fun getStringConcatStaticRef(): Handle {
            return Handle(
                Opcodes.H_INVOKESTATIC,
                "java/lang/invoke/StringConcatFactory",
                "makeConcat",
                ($$"(Ljava/lang/invoke/MethodHandles$Lookup;" +
                        "Ljava/lang/String;" +
                        "Ljava/lang/invoke/MethodType;)" +
                        "Ljava/lang/invoke/CallSite;"),
                false
            )
        }
    }
}