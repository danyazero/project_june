package com.danyazero

import com.danyazero.model.Node
import com.danyazero.utils.GenerationContext
import june.JuneLexer
import june.JuneParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.objectweb.asm.ClassWriter
import java.io.FileOutputStream

fun main() {
    val imports = HashMap<String, String>()
    imports["String"] = "java/lang/String"

    val generationContext = GenerationContext(
        imports = imports,
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