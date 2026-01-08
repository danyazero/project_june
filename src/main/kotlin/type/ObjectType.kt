package com.danyazero.type

import com.danyazero.model.ReferenceType
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ObjectType(
    private val name: String?,
) : ReferenceType<Any> {
    override fun getName(): String {
        return name ?: throw NullPointerException()
    }

    override fun postack(mv: MethodVisitor, value: Any) {
        TODO("Not yet implemented")
    }

    override fun store(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ASTORE, index.toInt())
    }

    override fun load(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ALOAD, index.toInt())
    }

    override fun yield(mv: MethodVisitor) {
        TODO("Not yet implemented")
    }

    override fun aload(mv: MethodVisitor) {
        TODO("Not yet implemented")
    }

    override fun astore(mv: MethodVisitor) {
        TODO("Not yet implemented")
    }

    override fun equal(mv: MethodVisitor, jumpTarget: Label) {
        TODO("Not yet implemented")
    }

    override fun getSize(): Short {
        return 1
    }

    override fun toString(): String {
        return "ObjectType(name=$name)"
    }
}