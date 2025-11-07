package com.danyazero.model.ast;

import com.danyazero.model.Type;

public interface Expression extends Node {
    Type<?> getType();
}
