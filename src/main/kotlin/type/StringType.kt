package com.danyazero.type

import com.danyazero.model.ReferenceType
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class StringType : ReferenceType<String> {

    override fun postack(mv: MethodVisitor, value: String) {
        mv.visitLdcInsn(value)
    }

    override fun store(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ASTORE, index.toInt())
    }

    override fun load(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ALOAD, index.toInt())
    }

    override fun yield(mv: MethodVisitor) {
        //TODO StringType yield operation
    }

    override fun aload(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.AALOAD)
    }

    override fun astore(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.AASTORE)
    }

    override fun equal(mv: MethodVisitor, jumpTarget: Label) {
        //TODO StringType equals operation
    }

    override fun getSize(): Short {
        return 1
    }

    override fun getName(): String {
        return "String"
    }
}