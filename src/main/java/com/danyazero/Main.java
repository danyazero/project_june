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

        createTestClass();
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
                Map.entry("String", "java.lang.String")
        );

        var generationContext = new GenerationContext(mv, imports);

        // ((38298 / 6) + 128) * 3 = 19 533
        var expression0 = new Expression(Operation.DIVISION, Value.of(38298), Value.of(6));
        var expression1 = new Expression(Operation.ADDITION, Value.of(128), expression0);
        var expression2 = new Expression(Operation.MULTIPLICATION, Value.of(3), expression1);
        var expression3 = new Variable("a", new IntegerType(), expression2);

//        var method = new MethodInvoke("System.out.println", List.of(Value.newIntValue(1)));
        var method = new MethodInvoke("System.out.printf", List.of(Value.of("Hello, %s"), Value.of("World!")));

//        method.produce(generationContext);
//        expression3.produce(generationContext);



        var slice = new Slice(List.of(
                expression0,
                Value.of(4),
                Value.of(5)
        ));
        var sliceVar = new Variable("a", new ArrayType(new IntegerType()), slice);
        var sliceElementVar = new Variable("b", new IntegerType(), new SliceElement(new Operand("a"), Value.of(0)));

        sliceVar.produce(generationContext);
        sliceElementVar.produce(generationContext);

        mv = generationContext.getMethodVisitor();

        var variableInfo = generationContext.resolveVariable("b");

        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitIntInsn(ILOAD, variableInfo.localIndex());
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);


//        mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "toString", "([I)Ljava/lang/String;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

//        mv.visitIntInsn(ISTORE, 1);
//        var variableInfo = generationContext.resolveVariable("a");
//
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitIntInsn(ILOAD, variableInfo.localIndex());
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(5, 5);
        mv.visitEnd();

        cw.visitEnd();

        byte[] bytes = cw.toByteArray();

        try (FileOutputStream fos = new FileOutputStream("Hello.class")) {
            fos.write(bytes);
        }
    }
}