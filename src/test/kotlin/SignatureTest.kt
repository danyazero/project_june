import com.danyazero.node.Method
import com.danyazero.node.Parameter
import com.danyazero.node.Signature
import com.danyazero.node.TypeNode
import com.danyazero.type.IntegerType
import com.danyazero.type.StringType
import com.danyazero.type.VoidType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SignatureTest {


    @Test
    fun testGetStartMethodDescriptor() {
        val parameters = listOf(Parameter("args", StringType()))
        val returnTypes = listOf<TypeNode>(TypeNode(VoidType()))

        val result = Signature(parameters, returnTypes).getDescriptor()
        assertEquals("(Ljava/lang/String;)V", result)
    }

    @Test
    fun testGetMethodDescriptor_2() {
        val parameters = listOf(Parameter("a", IntegerType()), Parameter("b", IntegerType()))
        val returnTypes = listOf(TypeNode(IntegerType()))

        val result = Signature(parameters, returnTypes).getDescriptor()
        assertEquals("(II)I", result)
    }

    @Test
    fun testGetMethodDescriptor_3() {
        val parameters = listOf(Parameter("a", StringType()), Parameter("b", StringType()))
        val returnTypes = listOf(TypeNode(StringType()))

        val result = Signature(parameters, returnTypes).getDescriptor()
        assertEquals("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", result)
    }

    @Test
    fun testGetMethodDescriptor_4() {
        val parameters = listOf(Parameter("a", IntegerType()), Parameter("b", StringType()))
        val returnTypes = listOf(TypeNode(StringType()))

        val result = Signature(parameters, returnTypes).getDescriptor()
        assertEquals("(ILjava/lang/String;)Ljava/lang/String;", result)
    }

    @Test
    fun testGetMethodDescriptor_5() {
        val parameters = listOf<Parameter>()
        val returnTypes = listOf<TypeNode>()

        val result = Signature(parameters, returnTypes).getDescriptor()
        assertEquals("()V", result)
    }
}