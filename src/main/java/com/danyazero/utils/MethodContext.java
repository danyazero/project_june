package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.VariableInfo;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayDeque;
import java.util.Deque;

public class MethodContext {

    private MethodVisitor mv;
    private final Deque<Scope> scopes = new ArrayDeque<>();
    private short nextLocalIndex;

    public MethodContext(MethodVisitor mv) {
        this.mv = mv;
        scopes.add(new Scope());
    }

    public MethodContext() {
        scopes.add(new Scope());
    }

    public void enterScope() {
        scopes.push(new Scope(scopes.peek()));
    }

    public void exitScope() {
        scopes.pop();
    }

    public short defineVariable(String name, Type<?> type) {
        var scope = scopes.peek();
        if (scope != null) {
            var variableIndex = this.allocateLocal(type);
            scope.define(name, type, variableIndex);

            return variableIndex;
        } else throw new IllegalStateException("Variable has not been defined (Scope is null)");
    }

    public VariableInfo resolveVariable(String name) {
        for (Scope scope : scopes) {
            var v = scope.resolve(name);
            if (v != null) return v;
        }
        return null;
    }

    public short allocateLocal(Type<?> type) {
        short index = nextLocalIndex;
        nextLocalIndex += type.getSize();

        return index;
    }


    public MethodVisitor getMethodVisitor() {
        if (mv != null) {
            return mv;
        }

        throw new IllegalStateException("MethodVisitor has not been defined");
    }

    public void setMethodVisitor(MethodVisitor mv) {
        this.mv = mv;
    }
}
