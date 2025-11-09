package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.Variable;
import com.danyazero.model.VariableInfo;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Field;
import java.util.*;

public class GenerationContext {
    private final MethodVisitor mv;
    private final ClassWriter cw;

    private final GenerationContext parent;
    private final Deque<Scope> scopes = new ArrayDeque<>();
    private final Map<String, String> imports;
    private short nextLocalIndex;

    public GenerationContext(GenerationContext parent, MethodVisitor mv) {
        this.parent = parent;
        this.cw = parent.cw;
        this.mv = mv;
        scopes.push(new Scope());
        this.imports = new HashMap<>();
    }

    public GenerationContext(MethodVisitor mv, Map<String, String> imports) {
        this.parent = null;
        this.cw = null;
        this.mv = mv;
        scopes.push(new Scope());
        this.imports = imports;
    }

    public GenerationContext(ClassWriter cw, Map<String, String> imports) {
        this.mv = null;
        this.parent = null;
        this.cw = cw;
        scopes.push(new Scope());
        this.imports = imports;
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

    public void addImport(String importName) {
        var simpleName = importName.split("\\.");
        var lastToken = simpleName.length - 1;

        this.imports.put(importName, simpleName[lastToken]);
    }

    public String resolveImport(String importName) {
        if (imports.containsKey(importName)) return imports.get(importName);

        return this.resolveCoreImport(importName)
                .orElseThrow(() -> new RuntimeException("No such import: " + importName));
    }

    private Optional<String> resolveCoreImport(String importName) {
        try {
            return Optional.of(
                    Class.forName("java.lang." + importName).toString()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Field resolveClassField(String className, String fieldName) {

        try {
            return Class.forName(className).getField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to resolve " + className + "." + fieldName, e);
        }
    }

    public MethodVisitor getMethodVisitor() {
        return mv;
    }

    public ClassWriter getClassWriter() {
        return cw;
    }
}
