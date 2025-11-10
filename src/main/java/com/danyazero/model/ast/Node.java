package com.danyazero.model.ast;

import com.danyazero.utils.GenerationContext;

public interface Node {
    void produce(GenerationContext ctx);
    void resolveTypes(GenerationContext ctx);
}
