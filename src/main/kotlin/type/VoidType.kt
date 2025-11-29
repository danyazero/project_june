package com.danyazero.type

import com.danyazero.model.Type
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor

class VoidType : Type<Void> {

    override fun postack(mv: MethodVisitor, value: Void) {
        throw UnsupportedOperationException()
    }

    override fun store(mv: MethodVisitor, index: Short) {
        throw UnsupportedOperationException()
    }

    override fun load(mv: MethodVisitor, index: Short) {
        throw UnsupportedOperationException()
    }

    override fun yield(mv: MethodVisitor) {
        throw UnsupportedOperationException()
    }

    override fun aload(mv: MethodVisitor) {
        throw UnsupportedOperationException()
    }

    override fun astore(mv: MethodVisitor) {
        throw UnsupportedOperationException()
    }

    override fun equal(mv: MethodVisitor, jumpTarget: Label) {
        //TODO VoidType equals operation
    }

    override fun getSize(): Short {
        return 1
    }
}