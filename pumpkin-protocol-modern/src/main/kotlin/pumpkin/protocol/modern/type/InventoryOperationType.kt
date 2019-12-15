package pumpkin.protocol.modern.type

enum class InventoryOperationType(val mode: Int, val button: Int, val isNormalSlot: Boolean) {
    LEFT_MOUSE_CLICK(mode = 0, button = 0, isNormalSlot = true),
    RIGHT_MOUSE_CLICK(mode = 0, button = 1, isNormalSlot = true),

    SHIFT_LEFT_MOUSE_CLICK(mode = 1, button = 0, isNormalSlot = true),
    SHIFT_RIGHT_MOUSE_CLICK(mode = 1, button = 1, isNormalSlot = true),

    NUMBER_KEY_1(mode = 2, button = 0, isNormalSlot = true),
    NUMBER_KEY_2(mode = 2, button = 1, isNormalSlot = true),
    NUMBER_KEY_3(mode = 2, button = 2, isNormalSlot = true),
    NUMBER_KEY_4(mode = 2, button = 3, isNormalSlot = true),
    NUMBER_KEY_5(mode = 2, button = 4, isNormalSlot = true),
    NUMBER_KEY_6(mode = 2, button = 5, isNormalSlot = true),
    NUMBER_KEY_7(mode = 2, button = 6, isNormalSlot = true),
    NUMBER_KEY_8(mode = 2, button = 7, isNormalSlot = true),
    NUMBER_KEY_9(mode = 2, button = 8, isNormalSlot = true),
    MIDDLE_CLICK(mode = 3, button = 2, isNormalSlot = true),

    DROP_KEY(mode = 4, button = 0, isNormalSlot = true),
    CTRL_DROP_KEY(mode = 4, button = 1, isNormalSlot = true),

    LEFT_CLICK_OUTSIDE_NONE_HELD(mode = 4, button = 0, isNormalSlot = false),
    RIGHT_CLICK_OUTSIDE_NONE_HELD(mode = 4, button = 1, isNormalSlot = false),

    START_LEFT_MOUSE_DRAG(mode = 5, button = 0, isNormalSlot = false),
    START_RIGHT_MOUSE_DRAG(mode = 5, button = 4, isNormalSlot = false),
    START_MIDDLE_MOUSE_DRAG(mode = 5, button = 8, isNormalSlot = false),

    ADD_SLOT_LEFT_MOUSE_DRAG(mode = 5, button = 1, isNormalSlot = true),
    ADD_SLOT_RIGHT_MOUSE_DRAG(mode = 5, button = 5, isNormalSlot = true),
    ADD_SLOT_MIDDLE_MOUSE_DRAG(mode = 5, button = 9, isNormalSlot = true),

    END_LEFT_MOUSE_DRAG(mode = 5, button = 2, isNormalSlot = false),
    END_RIGHT_MOUSE_DRAG(mode = 5, button = 6, isNormalSlot = false),
    END_MIDDLE_MOUSE_DRAG(mode = 5, button = 10, isNormalSlot = false),

    DOUBLE_CLICK(mode = 6, button = 0, isNormalSlot = true);

    companion object {
        private val MAP = HashMap<Int, HashMap<Int, InventoryOperationType>>()

        init {
            for (type in values()) {
                @Suppress("ReplacePutWithAssignment")
                MAP.computeIfAbsent(type.mode) { HashMap() }.put(type.button, type)
            }
        }

        operator fun get(mode: Int, button: Int): InventoryOperationType =
            MAP[mode]?.get(button)
                ?: throw IllegalArgumentException("Unknown inventory operation type; mode: $mode button: $button")
    }
}