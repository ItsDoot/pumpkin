package pumpkin.event.text

import pumpkin.auth.GameProfile
import pumpkin.event.Event
import pumpkin.text.Text

interface MessageEvent : Event {

    data class Clientbound(val message: Text) : MessageEvent

    data class Serverbound(val sender: GameProfile, val message: String) : MessageEvent
}