package com.danyazero.utils;

import com.danyazero.model.ast.Node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

public class MethodInvoke implements Node {
    private final String method;
    private final List<Node> arguments;

    public MethodInvoke(String method, List<Node> arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public void produce(GenerationContext ctx) {
        var methodParts = method.split("\\.");
        var targetClass = ctx.resolveImport(methodParts[0]);
        System.out.println(targetClass);
        var fields = new ArrayList<Field>();
        for (var i = 1; i < methodParts.length - 1; i++) {
            fields.add(ctx.resolveClassField(targetClass, methodParts[i]));
        }
        System.out.println(Arrays.toString(fields.toArray()));
        String descriptor;
        if (!fields.getFirst().getType().isPrimitive()) {
            descriptor = "L" + fields.getFirst().getType().getTypeName() + ";";
        } else {
            throw new RuntimeException("Unknown method invoke descriptor");
        }
        ctx.getMethodVisitor().visitFieldInsn(GETSTATIC, targetClass.replaceAll("\\.", "/"), methodParts[1], descriptor);
        arguments.forEach(argument -> argument.produce(ctx));
        ctx.getMethodVisitor().visitMethodInsn(INVOKEVIRTUAL, fields.getFirst().getType().getTypeName().replaceAll("\\.", "/"), methodParts[methodParts.length-1], "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream;", false);
    }
}
