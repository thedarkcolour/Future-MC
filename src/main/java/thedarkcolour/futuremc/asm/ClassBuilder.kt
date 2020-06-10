package thedarkcolour.futuremc.asm

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import java.util.function.Consumer

/**
 * Builds a new class.
 *
 * @author TheDarkColour
 */
class ClassBuilder(superClass: Class<*>) {
    private val writer = ClassWriter(0)
    private lateinit var methodVistor: MethodVisitor

    fun constructor(privacy: Int, constructor: Consumer<Constructor>) {
        method(privacy, "<init>", "()V", constructor)
    }

    fun method(privacy: Int, name: String, desc: String, method: Consumer<out Closure>) {

    }

    fun build(): Class<*> {
        TODO()
    }

    /**
     * Loads in the new classes
     */
    private object Loader : ClassLoader()

    /**
     * Add lines of code!
     */
    interface Closure {
        fun loadVariable(variable: Int)
        fun invokeMethod(type: Int, caller: String, name: String, desc: String)
    }

    class Constructor(val mv: MethodVisitor, builder: ClassBuilder) : Closure {
        override fun loadVariable(variable: Int) {
            TODO("not implemented")
        }

        fun invokeSuperConstructor() {
            //mv.visitMethodInsn()
        }

        override fun invokeMethod(type: Int, caller: String, name: String, desc: String) {
            TODO("not implemented")
        }
    }
}