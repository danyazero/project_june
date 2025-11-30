package com.danyazero.type

import com.danyazero.model.PrimitiveType
import com.danyazero.model.ReferenceType
import com.danyazero.model.Type
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ArrayType(
    val child: Type<*>,
    val length: Int? = null
) : Type<Int> {

    override fun postack(mv: MethodVisitor, value: Int) {
        IntegerType().postack(mv, value)

        if (child is PrimitiveType<*>) {
            mv.visitIntInsn(Opcodes.NEWARRAY, child.getType())
        } else if (child is ReferenceType){
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String")
        }
    }

    fun length(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.ARRAYLENGTH)
    }

    override fun store(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ASTORE, index.toInt())
    }

    override fun load(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ALOAD, index.toInt())
    }

    override fun yield(mv: MethodVisitor) {
    }

    override fun aload(mv: MethodVisitor) {
        throw RuntimeException("Not implemented")
    }

    override fun astore(mv: MethodVisitor) {
        throw RuntimeException("Not implemented")
    }

    override fun equal(mv: MethodVisitor, jumpTarget: Label) {
        //TODO ArrayType equals operation
    }


    fun loadElement(mv: MethodVisitor, index: Int) {
        if (child is PrimitiveType<*>) {
            IntegerType().postack(mv, index)
            child.aload(mv)
        }

        throw RuntimeException("This type is not acceptable.")
    }

    override fun getSize(): Short {
        return 1
    }

    override fun toString(): String {
        return "ArrayType(child=$child)"
    }
}