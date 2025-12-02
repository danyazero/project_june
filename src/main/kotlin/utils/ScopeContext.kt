package com.danyazero.utils

import com.danyazero.model.Type
import com.danyazero.model.VariableInfo
import java.util.ArrayDeque
import java.util.Deque

open class ScopeContext {
    private val scopes: Deque<Scope> = ArrayDeque()
    private var nextLocalIndex: Short = 0

    init {
        scopes.push(Scope())
    }
    fun enterScope() {
        scopes.push(Scope(scopes.peek()))
    }

    fun exitScope() {
        scopes.pop()
    }

    fun defineVariable(name: String, type: Type<*>): Short {
        scopes.peek()?.let {
            val variableIndex = this.allocateLocal(type)
            it.define(name, type, variableIndex)

            return variableIndex
        }

        throw IllegalStateException("Variable has not been defined (Scope is null)")
    }

    fun resolveVariable(name: String): VariableInfo? {
        if (name == "_") throw IllegalStateException("Variable can't be resolved")
        for (scope in scopes) {
            return scope.resolve(name) ?: continue
        }

        return null
    }

    fun resolveLocalVariable(name: String): VariableInfo? {
        scopes.peek()?.let {
            return it.resolve(name)
        }

        return null
    }

    fun allocateLocal(type: Type<*>): Short {
        val index = nextLocalIndex
        nextLocalIndex = (nextLocalIndex + type.getSize()).toShort()

        return index
    }
}