package info.daviot.dictionary.model;

import java.util.EventListener;

trait SessionCompleteListener extends EventListener {
  def sessionComplete(e: SessionCompleteEvent)
}
