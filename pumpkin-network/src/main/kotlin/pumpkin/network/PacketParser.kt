package pumpkin.network

import io.netty.buffer.ByteBuf
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.parsetools.RecordParser

class PacketParser(val consume: (ByteBuf) -> Unit) : Handler<Buffer> {

    private val parser: RecordParser = RecordParser.newFixed(1)

    private var curState: ParserState = ParserState.SIZE_NEXT
    private var curSize: PartialVarInt = PartialVarInt()

    init {
        parser.setOutput {
            when (curState) {
                ParserState.SIZE_NEXT -> {
                    val next = it.getByte(0)
                    this.curSize.write(next)

                    if (this.curSize.isFinished) {
                        this.curState = ParserState.BODY_NEXT
                        this.parser.fixedSizeMode(curSize.value)
                    }
                }
                ParserState.BODY_NEXT -> {
                    this.consume(it.byteBuf)
                    this.curSize = PartialVarInt()
                    this.curState = ParserState.SIZE_NEXT
                    this.parser.fixedSizeMode(1)
                }
            }
        }
    }

    override fun handle(event: Buffer) {
        parser.handle(event)
    }

    private enum class ParserState { SIZE_NEXT, BODY_NEXT }

    private class PartialVarInt {

        private var current: Int = 0
        private var size: Int = 0

        var value: Int = 0
            private set

        val isFinished: Boolean get() = current and 0x80 != 0x80

        fun write(value: Byte) {
            this.current = value.toInt()
            this.value = this.value or ((this.current and 0x7F) shl (7 * size))
            check(++this.size <= 5) { "VarInt max size exceeded: $size > 5" }
        }
    }
}