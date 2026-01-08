package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.model.PrimitiveType
import com.danyazero.model.ReferenceType
import com.danyazero.model.Type
import com.danyazero.type.ArrayType
import com.danyazero.type.VoidType
import com.danyazero.utils.GenerationContext
import com.danyazero.node.Parameter
import com.danyazero.node.TypeNode
import com.danyazero.type.ObjectType
import org.objectweb.asm.Opcodes
import java.util.function.Consumer

class Method(
    val modifiers: Int = Opcodes.ACC_PRIVATE,
    val signature: Signature,
    val statementList: List<Node>,
) : Node {

    override fun produce(ctx: GenerationContext) {
        val descriptor = signature.getDescriptor(ctx)
        val methodVisitor = ctx.getClassWriter()
            .visitMethod(modifiers, signature.name, descriptor, null, null)

        ctx.setMethodVisitor(methodVisitor, signature)
        ctx.defineMethod(signature)
        ctx.defineVariable("this", false, ObjectType("java/lang/Object"))
        signature.produce(ctx)
        methodVisitor.visitCode()

        statementList.forEach(Consumer { statement: Node -> statement.produce(ctx) })

        methodVisitor.visitMaxs(8, 8)
        methodVisitor.visitEnd()
    }

    override fun toString(): String {
        return "Method(name='${signature.name}', signature=$signature, statementList=$statementList)"
    }
}