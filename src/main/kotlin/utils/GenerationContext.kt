package com.danyazero.utils

import com.danyazero.node.Class
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import java.lang.reflect.Field

class GenerationContext(
    private var methodVisitor: MethodVisitor? = null,
    private val classWriter: ClassWriter? = null,
    private val imports: MutableMap<String, String> = mutableMapOf(),
) : ScopeContext() {

    fun addImport(importName: String) {
        val simpleName = importName.split("\\.".toRegex()).toTypedArray().last()

        imports[simpleName] = importName
    }

    fun resolveImport(importName: String): String {
        if (imports.containsKey(importName)) {
            return imports[importName] ?: throw RuntimeException("No such import: $importName")
        }

        return this.resolveCoreImport(importName) ?: throw RuntimeException("No such import: $importName")
    }

    private fun resolveCoreImport(importName: String): String? {
        return try {
            java.lang.Class.forName("java.lang.$importName").name
        } catch (_: Exception) {
            null
        }
    }

    fun resolveClassField(className: String?, fieldName: String): Field {
        try {
            return java.lang.Class.forName(className).getField(fieldName)
        } catch (e: Exception) {
            throw RuntimeException("An error occurred while trying to resolve $className.$fieldName", e)
        }
    }

    fun getMethodVisitor(): MethodVisitor {
        return methodVisitor ?: throw RuntimeException("An error occurred while getting method visitor.")
    }

    fun setMethodVisitor(methodVisitor: MethodVisitor) {
        this.methodVisitor = methodVisitor
    }

    fun getClassWriter(): ClassWriter {
        return classWriter ?: throw RuntimeException("An error occurred while getting class writer visitor.")
    }
}