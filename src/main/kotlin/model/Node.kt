package com.danyazero.model

import com.danyazero.utils.GenerationContext

interface Node {
    fun produce(ctx: GenerationContext)
}