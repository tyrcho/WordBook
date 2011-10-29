package info.daviot.dictionary.model

import java.util.EventObject

class SessionCompleteEvent(source: Any, val score: String) extends EventObject(source)