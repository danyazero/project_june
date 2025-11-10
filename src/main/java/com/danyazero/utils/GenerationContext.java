package com.danyazero.utils;

import com.danyazero.model.MethodSign;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Field;
import java.util.*;

public class GenerationContext {
    private final MethodSign currentMethodSign;
    private final ClassWriter cw;
    private final Map<String, String> imports;
    private final Map<MethodSign, MethodContext> methodSigns;

    public GenerationContext(Map<String, String> imports) {
        this.currentMethodSign = null;
        this.cw = new ClassWriter(0);
        this.imports = imports;
        this.methodSigns = new HashMap<>();
    }

    public GenerationContext(MethodSign currentMethodSign, GenerationContext generationContext) {
        this.currentMethodSign = currentMethodSign;
        this.cw = generationContext.getClassWriter();
        this.imports = generationContext.imports;
        this.methodSigns = generationContext.methodSigns;
    }

    public ClassWriter getClassWriter() {
        return cw;
    }

    public GenerationContext newMethodContext(MethodSign methodSign, MethodVisitor methodVisitor) {
        methodSigns.put(methodSign, new MethodContext(methodVisitor));

        return new GenerationContext(methodSign, this);
    }

    public GenerationContext newMethodContext(MethodSign methodSign) {
        methodSigns.put(methodSign, new MethodContext());

        return new GenerationContext(methodSign, this);
    }

    public MethodVisitor getMethodVisitor(MethodSign methodSign) {
        if (methodSigns.containsKey(methodSign)) {
            return methodSigns.get(methodSign).getMethodVisitor();
        }
        throw new RuntimeException("Method Context Not Found: " + methodSign);
    }

    public MethodVisitor getMethodVisitor() {
        if (methodSigns.containsKey(this.currentMethodSign)) {
            return methodSigns.get(this.currentMethodSign).getMethodVisitor();
        }
        throw new RuntimeException("Method Context Not Found: " + this.currentMethodSign);
    }

    public MethodContext getMethodContext() {
        if (methodSigns.containsKey(currentMethodSign)) {
            return methodSigns.get(currentMethodSign);
        }
        throw new RuntimeException("Method Context Not Found: " + currentMethodSign);
    }

    public GenerationContext getGenerationContext(MethodSign methodSign) {
        return new GenerationContext(methodSign, this);
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
                    Class.forName("java.lang." + importName).getName()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Field resolveClassField(String className, String fieldName) {

        try {
            return Class.forName(className).getField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to resolve " + className + "->" + fieldName, e);
        }
    }

    public MethodSign getCurrentMethodSign() {
        return currentMethodSign;
    }
}
