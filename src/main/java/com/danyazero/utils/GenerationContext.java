package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.Variable;
import com.danyazero.model.VariableInfo;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayDeque;
import java.util.Deque;

public class GenerationContext {
    private final MethodVisitor mv;
    private final ClassWriter cw;

    private final GenerationContext parent;
    private final Deque<Scope> scopes = new ArrayDeque<>();
    private short nextLocalIndex;

    public GenerationContext(GenerationContext parent, MethodVisitor mv) {
        this.parent = parent;
        this.cw = parent.cw;
        this.mv = mv;
        scopes.push(new Scope());
    }

    public GenerationContext(MethodVisitor mv) {
        this.parent = null;
        this.cw = null;
        this.mv = mv;
        scopes.push(new Scope());
    }

    public GenerationContext(ClassWriter cw) {
        this.mv = null;
        this.parent = null;
        this.cw = cw;
        scopes.push(new Scope());
    }

    public void enterScope() {
        scopes.push(new Scope(scopes.peek()));
    }

    public void exitScope() {
        scopes.pop();
    }

    public short defineVariable(String name, Variable variable) {
        var scope = scopes.peek();
        if (scope != null) {
            var variableIndex = this.allocateLocal(variable.getType());
            scope.define(name, variable, variableIndex);

            return variableIndex;
        } else throw new IllegalStateException("Variable has not been defined (Scope is null)");
    }

    public VariableInfo resolveVariable(String name) {
        for (Scope scope : scopes) {
            var v = scope.resolve(name);
            if (v != null) return v;
        }
        if (parent != null) return parent.resolveVariable(name);
        return null;
    }

    public short allocateLocal(Type<?> type) {
        short index = nextLocalIndex;
        nextLocalIndex += type.getSize();

        return index;
    }

    public MethodVisitor getMethodVisitor() {
        return mv;
    }

    public ClassWriter getClassWriter() {
        return cw;
    }
}
