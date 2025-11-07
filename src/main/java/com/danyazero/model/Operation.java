package com.danyazero.model;

public enum Operation {
    DIVISION("/"),
    ADDITION("+"),
    SUBSTRUCTION("-"),
    MULTIPLICATION("*"),
    MOD("%"),
    LSHIFT("<<"),
    RSHIFT(">>"),
    XOR("^");

    Operation(String operation) {
        this.operation = operation;
    }

    private final String operation;

    public String getOperation() {
        return operation;
    }
}
