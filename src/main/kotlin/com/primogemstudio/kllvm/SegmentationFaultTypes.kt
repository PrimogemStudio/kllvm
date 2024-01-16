package com.primogemstudio.kllvm

enum class SegmentationFaultTypes(val id: Byte, val toExc: (Int) -> Throwable) {

    STACK_OVERFLOW(0x01, { StackOverflowError("Detected stack overflow at 0x%08X, halting VM...".format(it)) }),
    NULL_POINTER(0x02, { NullPointerException("Referenced NULL at 0x%08X, halting VM...".format(it)) }),
    UNKNOWN_OPERAND(0x03, { IllegalStateException("Unknown operand called at 0x%08X, halting VM...".format(it)) });

    @OptIn(ExperimentalStdlibApi::class)
    class VMException(e: SegmentationFaultTypes, addr: Int): RuntimeException("SIG ${e.id.toHexString()}", e.toExc(addr))
}