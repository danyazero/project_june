package com.danyazero.utils

import com.danyazero.model.Type
import com.danyazero.model.VariableInfo
import com.danyazero.node.Signature
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import java.lang.reflect.Field

class GenerationContext(
    var className: String? = null,
    private var methodVisitor: MethodVisitor? = null,
    private val classWriter: ClassWriter? = null,
    private val imports: MutableMap<String, String> = mutableMapOf(),
    private var scopeContext: ScopeContext? = null,
    private val classContext: MutableMap<String, Signature> = HashMap()
) {
    fun resolveMethod(owner: String, signature: Signature) : Signature {
        val methodSignature = methodSignature(owner, signature)
        return classContext[methodSignature] ?: throw RuntimeException("method with signature - $methodSignature not found")
    }

    fun defineMethod(signature: Signature) : String {
        val methodSignature = methodSignature(className ?: throw RuntimeException("Class name not defined"), signature)
        println("Definition of method with signature - $methodSignature")
        classContext[methodSignature] = signature

        return methodSignature
    }

    private fun methodSignature(owner: String, signature: Signature) : String = """$owner.${signature.getSignature(this)}"""

    fun enterScope() {
        scopeContext?.enterScope() ?: throw RuntimeException("Failed enter scope: Scope context is null")
    }

    fun exitScope() {
        scopeContext?.exitScope() ?: throw RuntimeException("Failed exit scope: Scope context is null")
    }

    fun defineVariable(name: String, isConstant: Boolean = false, type: Type<*>): Short {
        return scopeContext?.defineVariable(name, isConstant, type)
            ?: throw RuntimeException("Failed define variable: Scope context is null")
    }

    fun resolveVariable(name: String): VariableInfo? {
        return scopeContext?.resolveVariable(name)
            ?: throw RuntimeException("Failed resolve variable: Scope context is null")
    }

    fun resolveLocalVariable(name: String): VariableInfo? {
        return scopeContext?.resolveLocalVariable(name)
            ?: throw RuntimeException("Failed resolve variable: Scope context is null")
    }


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

    fun setMethodVisitor(methodVisitor: MethodVisitor, signature: Signature) {
        this.methodVisitor = methodVisitor
        this.scopeContext = ScopeContext()
        this.classContext[signature.getSignature(this)] = signature
    }

    fun getClassWriter(): ClassWriter {
        return classWriter ?: throw RuntimeException("An error occurred while getting class writer visitor.")
    }
}