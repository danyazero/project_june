package com.danyazero.model

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor

interface Type<T> {
    fun postack(mv: MethodVisitor, value: T)
    fun store(mv: MethodVisitor, index: Short)
    fun load(mv: MethodVisitor, index: Short)
    fun yield(mv: MethodVisitor);

    fun aload(mv: MethodVisitor)
    fun astore(mv: MethodVisitor)

    fun equal(mv: MethodVisitor, jumpTarget: Label);

    fun getSize() : Short
}
