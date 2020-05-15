// I decided to take some inspiration from Quark's ASM.
// Hopefully this simplifies things and I can
// put everything in the same spot.

package thedarkcolour.futuremc.asm

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

typealias MethodAction = (MethodNode) -> Boolean
typealias NodeFilter = (AbstractInsnNode) -> Boolean
typealias NodeAction = (MethodNode, AbstractInsnNode) -> Boolean
typealias TransformerAction = (ClassNode) -> Boolean

fun patchBlockPistonBase(basicClass: ByteArray) {}

class MethodSignature(private val mappedName: String, private val srgName: String, private val desc: String) {
    fun matches(name: String, desc: String): Boolean {
        return (mappedName == name || srgName == name) && this.desc == desc
    }

    fun matches(node: MethodNode) = matches(node.name, node.desc)

    fun matches(node: MethodInsnNode) = matches(node.name, node.desc)
}
