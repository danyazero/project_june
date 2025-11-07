package com.danyazero;

import com.danyazero.model.Operation;
import com.danyazero.model.ast.ExpressionImpl;
import com.danyazero.model.ast.Value;
import june.GoLexer;
import june.GoParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class Main {
    public static void main(String[] args) throws IOException {
//        System.out.println(Arrays.toString(args));

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

        // ((38298 / 6) + 128) * 3 = 19 533
        var expression0 = new ExpressionImpl(Operation.DIVISION, Value.newIntegerValue(38298), Value.newIntegerValue(6));
        var expression1 = new ExpressionImpl(Operation.ADDITION, Value.newIntegerValue(128), expression0);
        var expression = new ExpressionImpl(Operation.MULTIPLICATION, Value.newIntegerValue(3), expression1);

        mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
        expression.produce(mv);

        mv.visitIntInsn(ISTORE, 1);

        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitIntInsn(ILOAD, 1);

        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(4, 2);
        mv.visitEnd();

        cw.visitEnd();

        byte[] bytes = cw.toByteArray();

        try (FileOutputStream fos = new FileOutputStream("Hello.class")) {
            fos.write(bytes);
        }
    }
}