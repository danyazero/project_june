package com.danyazero.expression

import com.danyazero.model.Expression
import org.objectweb.asm.Label

abstract class ConditionExpression(
    private var target: Label? = null,
    left: Expression,
    right: Expression
) : BinaryExpression(left, right) {

    fun target(target: Label) {
        this.target = target
    }

    fun target(): Label = target ?: throw RuntimeException("GOTO target is null on compilation step")
}