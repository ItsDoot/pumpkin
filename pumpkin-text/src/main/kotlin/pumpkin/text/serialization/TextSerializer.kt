package pumpkin.text.serialization

import pumpkin.text.Text

interface TextSerializer<T : Text, in Input, out Output> {

    fun serialize(text: T) : Output

    fun deserialize(input: Input): T
}