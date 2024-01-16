package com.primogemstudio.kllvm

import com.primogemstudio.kllvm.Constrants.nullptr
import java.nio.ByteBuffer

class VMInstance(mem: Int) {
    private var memoryPool: ByteBuffer? = null
    private var register_prg: Int = nullptr
    private var register_stk: Int = nullptr
    private val vmmemMax: Int
    fun execFrom(addr: Int) {
        register_prg = addr
        stkPush(addr)
        while (register_stk != vmmemMax - 1) {
            val operand = memoryPool?.get(register_prg)
            when (operand) {
                Operands.OP_NONE.b -> register_prg++
                Operands.OP_JMP.b -> with(register_prg) {
                    stkPush(this + 5)
                    register_prg = memoryPool?.getInt(this + 1)!!
                }
                Operands.OP_RET.b -> with(stkPop()) {
                    if (this == nullptr) segf(SegmentationFaultTypes.NULL_POINTER)
                    register_prg = this
                }
                Operands.OP_MOV.b -> register_prg++
                else -> segf(SegmentationFaultTypes.UNKNOWN_OPERAND)
            }
        }
        println("Executing ended")
    }
    fun writeTo(addr: Int, data: ByteArray) {
        for (i in data.indices) memoryPool?.put(addr + i, data[i])
    }
    fun stkPush(addr: Int) {
        register_stk -= 4
        checkStkReg()
        memoryPool?.putInt(register_stk, addr)
    }
    fun stkPop(): Int {
        if (register_stk > vmmemMax) return nullptr
        return memoryPool?.getInt(register_stk).let {
            register_stk += 4
            return@let it!!
        }
    }
    fun checkStkReg() {
        if (register_stk < 0) segf(SegmentationFaultTypes.STACK_OVERFLOW)
    }
    fun segf(s: SegmentationFaultTypes) {
        throw SegmentationFaultTypes.VMException(s, register_prg)
    }
    init {
        vmmemMax = mem
        memoryPool = ByteBuffer.allocate(vmmemMax)
        register_stk = vmmemMax - 1
    }

    companion object {
        fun create(mem: Int): VMInstance {
            return VMInstance(mem)
        }
    }
}