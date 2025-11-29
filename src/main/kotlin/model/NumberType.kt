package com.danyazero.model

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor

interface NumberType<T> : PrimitiveType<T> {
    fun add(mv: MethodVisitor)
    fun sub(mv: MethodVisitor)
    fun mul(mv: MethodVisitor)
    fun div(mv: MethodVisitor)

    fun xor(mv: MethodVisitor)
    fun mod(mv: MethodVisitor)
    fun ushr(mv: MethodVisitor)
    fun shr(mv: MethodVisitor)
    fun shl(mv: MethodVisitor)

    fun greater(mv: MethodVisitor, jumpTarget: Label)
    fun less(mv: MethodVisitor, jumpTarget: Label)

    fun greaterOrEqual(mv: MethodVisitor, jumpTarget: Label)
    fun lessOrEqual(mv: MethodVisitor, jumpTarget: Label)
}
