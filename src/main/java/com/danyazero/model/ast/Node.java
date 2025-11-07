package com.danyazero.model.ast;

import com.danyazero.utils.GenerationContext;
import org.objectweb.asm.MethodVisitor;

public interface Node {
    void produce(GenerationContext ctx);
}
