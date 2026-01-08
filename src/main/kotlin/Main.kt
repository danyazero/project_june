package com.danyazero

import com.danyazero.model.Node
import com.danyazero.utils.GenerationContext
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.objectweb.asm.ClassWriter
import java.io.FileOutputStream

fun main(args: Array<String>) {
    val imports = HashMap<String, String>()
    imports["String"] = "java/lang/String"

    val generationContext = GenerationContext(
        imports = imports,
        classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
    )

    val tree = parseTree(args[0])
    tree.produce(generationContext)
    val bytes = generationContext.getClassWriter().toByteArray()

    FileOutputStream(generationContext.className + ".class").use { fos ->
        fos.write(bytes)
    }

}

fun parseTree(filename: String) : Node {
    val input = CharStreams.fromFileName(filename)

    val lexer = JuneLexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = JuneParser(tokens)
    val tree = parser.sourceFile()


    return JuneVisitor().visit(tree)
}