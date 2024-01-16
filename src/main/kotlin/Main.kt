import com.primogemstudio.kllvm.Operands
import com.primogemstudio.kllvm.VMInstance

fun main(args: Array<String>) {
    val vm = VMInstance.create(512)
    vm.writeTo(0x0, byteArrayOf(
        Operands.OP_JMP.b, 0x0, 0x0, 0x0, 0x0
    ))
    vm.execFrom(0x0)
}