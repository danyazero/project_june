package com.danyazero;

import com.danyazero.model.Operation;
import com.danyazero.utils.*;
import june.GoLexer;
import june.GoParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class Main {
    public static void main(String[] args) throws IOException {
//        buildTree(args[0]);
//        createTestClass();
        createTestClass2();


    }

    private static void buildTree(String file) throws IOException {
        var input = CharStreams.fromFileName(file);

        GoLexer lexer = new GoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GoParser parser = new GoParser(tokens);
        ParseTree tree = parser.sourceFile();

        System.out.println(tree.toStringTree(parser));
        System.out.println();

        var visitor = new JuneVisitor();
        String res = visitor.visit(tree);

        System.out.println(res);
    }

    private static void createTestClass2() throws IOException {
        var imports = Map.ofEntries(
                Map.entry("System", "java.lang.System"),
                Map.entry("String", "java.lang.String"),
                Map.entry("Object", "java.lang.Object")
        );

        var generationContext = new GenerationContext(new ClassWriter(0), imports);

        var expression0 = new Expression(Operation.DIVISION, Value.of(38298), Value.of(6));
        var expression1 = new Expression(Operation.ADDITION, Value.of(128), expression0);
        var expression2 = new Expression(Operation.MULTIPLICATION, Value.of(3), expression1);


        var slice = new Slice(List.of(
                expression2,
                Value.of(2),
                Value.of(3)
        ));
        var sliceVar = new Variable("a", new ArrayType(new IntegerType()), slice);
        var sliceElementVar = new Variable("b", new IntegerType(), new SliceElement(new Operand("a"), Value.of(0)));
        var method = new MethodInvoke("System.out.println", List.of(new Operand("b")));

        var mainMethod = new Method(
                "main",
                List.of(
                        new MethodParameter("args", new ArrayType(new StringType()))
                ),
                List.of(
                        sliceVar,
                        sliceElementVar,
                        method
                ),
                List.of(
                        new ResultType(new VoidType())
                )
        );

        var newClass = new JuneClass("Hello", List.of(
                mainMethod
        ));
        newClass.produce(generationContext);

        byte[] bytes = generationContext.getClassWriter().toByteArray();

        try (FileOutputStream fos = new FileOutputStream("Hello.class")) {
            fos.write(bytes);
        }
    }

    private static void createTestClass() throws IOException {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V17, ACC_PUBLIC, "Hello", null, "java/lang/Object", null);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();

        var imports = Map.ofEntries(
                Map.entry("System", "java.lang.System"),
                Map.entry("String", "java.lang.String"),
                Map.entry("Object", "java.lang.Object")
        );

        var generationContext = new GenerationContext(mv, imports);

        // ((38298 / 6) + 128) * 3 = 19 533
        var expression0 = new Expression(Operation.DIVISION, Value.of(38298), Value.of(6));
        var expression1 = new Expression(Operation.ADDITION, Value.of(128), expression0);
        var expression2 = new Expression(Operation.MULTIPLICATION, Value.of(3), expression1);


        var slice = new Slice(List.of(
                expression2,
                Value.of(2),
                Value.of(3)
        ));
        var sliceVar = new Variable("a", new ArrayType(new IntegerType()), slice);
        var sliceElementVar = new Variable("b", new IntegerType(), new SliceElement(new Operand("a"), Value.of(0)));
        var method = new MethodInvoke("System.out.println", List.of(new Operand("b")));

        sliceVar.produce(generationContext);
        sliceElementVar.produce(generationContext);
        method.produce(generationContext);

        mv = generationContext.getMethodVisitor();

        mv.visitInsn(RETURN);
        mv.visitMaxs(5, 3);
        mv.visitEnd();

        cw.visitEnd();

        byte[] bytes = cw.toByteArray();

        try (FileOutputStream fos = new FileOutputStream("Hello.class")) {
            fos.write(bytes);
        }
    }
}