package com.danyazero.utils

import com.danyazero.model.Type
import com.danyazero.model.VariableInfo

class Scope(
    private val parent: Scope? = null,
) {
    private val variables = mutableMapOf<String, VariableInfo>()

    fun define(name: String, type: Type<*>, localIndex: Short) {
        variables[name] = VariableInfo(
            name,
            type,
            localIndex
        )
    }

    fun resolve(name: String?): VariableInfo? {
        if (variables.containsKey(name)) return variables[name]
        if (parent != null) return parent.resolve(name)

        throw IllegalArgumentException("Variable $name not found")
    }
}