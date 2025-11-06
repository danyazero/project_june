package com.danyazero;

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

        buildTree(args[0]);

//        createTestClass();
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
        mv.visitIntInsn(BIPUSH, 12);
        mv.visitInsn(ICONST_2);
        mv.visitInsn(INEG);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ISTORE, 1);
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(ILOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        cw.visitEnd();

        byte[] bytes = cw.toByteArray();

        try (FileOutputStream fos = new FileOutputStream("Hello.class")) {
            fos.write(bytes);
        }
    }
}