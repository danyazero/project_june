package com.danyazero.model.ast;

import org.objectweb.asm.MethodVisitor;

public interface Node {
    void produce(MethodVisitor mv);
}
