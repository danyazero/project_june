package com.danyazero.model

import org.objectweb.asm.MethodVisitor

interface PrimitiveType<T> : Type<T> {
    fun negate(mv: MethodVisitor)
    fun getType() : Int
}
