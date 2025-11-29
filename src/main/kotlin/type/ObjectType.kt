package com.danyazero.type

import com.danyazero.model.ReferenceType
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor

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
        TODO("Not yet implemented")
    }

    override fun load(mv: MethodVisitor, index: Short) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "ObjectType(name=$name)"
    }
}