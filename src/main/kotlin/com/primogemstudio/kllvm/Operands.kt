package com.primogemstudio.kllvm

enum class Operands(val b: Byte) {
    OP_NONE(0),
    OP_JMP(1),
    OP_RET(2),
    OP_MOV(3)
}