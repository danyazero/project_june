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

    public static Operation fromString(String symbol) {
        for (Operation op : Operation.values()) {
            if (op.operation.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("No operator with symbol: " + symbol);
    }
}
