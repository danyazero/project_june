package com.danyazero.model

import com.danyazero.utils.GenerationContext

interface IVariable {
    fun getName() : String
    fun getType(ctx: GenerationContext) : Type<*>
}
