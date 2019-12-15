package pumpkin.protocol.modern.type

enum class DimensionType(val id: Int) {
    NETHER(-1),
    OVERWORLD(0),
    END(1);

    companion object {
        val FROM_ID: Map<Int, DimensionType> = values().associateBy(DimensionType::id)
    }
}