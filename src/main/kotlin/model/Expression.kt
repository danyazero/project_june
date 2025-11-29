package com.danyazero.model

import com.danyazero.utils.GenerationContext

interface Expression : Node {
    fun getType(ctx: GenerationContext) : Type<*>
}