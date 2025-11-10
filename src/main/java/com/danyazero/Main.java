package com.danyazero;

import com.danyazero.model.Operation;
import com.danyazero.model.ast.Node;
import com.danyazero.utils.*;
import com.danyazero.utils.TypeNode;
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
        buildTree(args[0]);
//        createTestClass2();
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

        var imports = Map.ofEntries(
                Map.entry("System", "java.lang.System"),
                Map.entry("String", "java.lang.String"),
                Map.entry("Object", "java.lang.Object")
        );

        Node ast = visitor.visit(tree);
        var generationContext = new GenerationContext(imports);
        ast.resolveTypes(generationContext);
        ast.produce(generationContext);

        byte[] bytes = generationContext.getClassWriter().toByteArray();

        try (FileOutputStream fos = new FileOutputStream("Hello.class")) {
            fos.write(bytes);
        }
    }

    private static void createTestClass2() throws IOException {
        var imports = Map.ofEntries(
                Map.entry("System", "java.lang.System"),
                Map.entry("String", "java.lang.String"),
                Map.entry("Object", "java.lang.Object")
        );

        var generationContext = new GenerationContext(imports);

        var expression0 = new ExpressionNode(Operation.DIVISION, Value.of(38298), Value.of(6));
        var expression1 = new ExpressionNode(Operation.ADDITION, Value.of(128), expression0);
        var expression2 = new ExpressionNode(Operation.MULTIPLICATION, Value.of(3), expression1);


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
                        new TypeNode(new VoidType())
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
}