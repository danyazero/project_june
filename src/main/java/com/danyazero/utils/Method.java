package com.danyazero.utils;

import com.danyazero.model.Array;
import com.danyazero.model.ReferenceType;
import com.danyazero.model.Type;
import com.danyazero.model.ast.Node;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class Method implements Node {
    private final String name;
    private final List<MethodParameter> parameters;
    private final List<Node> statementList;
    private final List<ResultType> returnTypes;

    public Method(String name, List<MethodParameter> parameters, List<Node> statementList, List<ResultType> returnTypes) {
        this.name = name;
        this.parameters = parameters;
        this.statementList = statementList;
        this.returnTypes = returnTypes;
    }

    @Override
    public void produce(GenerationContext ctx) {


        var parametersDescriptor = getMethodParameters(ctx);
        var returnParametersDescriptor = getMethodReturnParameters(ctx);
        var descriptor = parametersDescriptor + returnParametersDescriptor;
        System.out.println(descriptor);

        var methodVisitor = ctx.getClassWriter()
                .visitMethod(ACC_PUBLIC + ACC_STATIC, name, descriptor, null, null);
        var statementContext = new GenerationContext(ctx, methodVisitor);
        methodVisitor.visitCode();

        statementList.forEach(statement -> statement.produce(statementContext));

        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(5, 3);
        methodVisitor.visitEnd();
    }

    private String getMethodReturnParameters(GenerationContext ctx) {
        if (!parameters.isEmpty()) {
            var type = returnTypes.getFirst().getType();
            if (type instanceof VoidType) {
              return "V";
            } if (type instanceof Array<?> arrayType) {
                return getParameter(arrayType, ctx);
            } else if (type instanceof ReferenceType<?> referenceType) {
                return getParameter(referenceType, ctx);
            }
        }

        return null;
    }

    private String getMethodParameters(GenerationContext ctx) {
        if (!parameters.isEmpty()) {
            var parametersList = new ArrayList<String>();
            for (var param : parameters) {
                var parameterType = param.getType();
                parametersList.add(getParameter(parameterType, ctx));
            }

            return "(" + String.join("", parametersList) + ")";
        }

        return null;
    }

    private String getParameter(Type<?> type, GenerationContext ctx) {
        System.out.println(type.getClass());
        if (type instanceof ArrayType arrayType) {
            return "[" + getParameter(arrayType.getChild(), ctx);
        } else if (type instanceof ReferenceType<?> referenceType) {
            return "L" + ctx.resolveImport(referenceType.getName()).replaceAll("\\.", "/") + ";";
        }

        return null;
    }
}
