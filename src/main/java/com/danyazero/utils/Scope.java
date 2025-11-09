package com.danyazero.utils;

import com.danyazero.model.Variable;
import com.danyazero.model.VariableInfo;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, VariableInfo> variables = new HashMap<>();
    private final Scope parent;

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Scope() {
        this(null);
    }

    public void define(String name, Variable variable, short localIndex) {

        variables.put(
                name,
                new VariableInfo(
                        variable.getName(),
                        variable.getType(),
                        localIndex
                )
        );
    }

    public VariableInfo resolve(String name) {
        if (variables.containsKey(name)) return variables.get(name);
        if (parent != null) return parent.resolve(name);

        throw new IllegalArgumentException("Variable " + name + " not found");
    }
}
