package com.danyazero

import com.danyazero.expression.AdditionExpression
import com.danyazero.expression.ShiftRightExpression
import com.danyazero.expression.ValueExpression
import com.danyazero.model.Node
import com.danyazero.node.*
import com.danyazero.type.ArrayType
import com.danyazero.type.IntegerType
import com.danyazero.type.StringType
import com.danyazero.utils.GenerationContext
import com.danyazero.node.Parameter
import june.JuneLexer
import june.JuneParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.objectweb.asm.ClassWriter
import java.io.FileOutputStream

fun main() {

    val generationContext = GenerationContext(
        imports = mutableMapOf(),
        classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
    )

    val tree = parseTree()
    println(tree)
    tree.produce(generationContext)
    saveClass(generationContext)
}

fun saveClass(ctx: GenerationContext) {
    val bytes = ctx.getClassWriter().toByteArray()

    FileOutputStream("Main.class").use { fos ->
        fos.write(bytes)
    }
}

fun parseTree() : Node {
    val fileName = "/Users/daniilmozzhukhin/IdeaProjects/project_june/src/main/resources/main.ju"
    val input = CharStreams.fromFileName(fileName)

    val lexer = JuneLexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = JuneParser(tokens)
    val tree = parser.sourceFile()

    println(tree.toStringTree(parser))
    val visitor = JuneVisitor()


    return visitor.visit(tree)
}