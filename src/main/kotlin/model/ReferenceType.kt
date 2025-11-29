package com.danyazero.model

interface ReferenceType<T> : Type<T> {
    fun getName() : String
}
