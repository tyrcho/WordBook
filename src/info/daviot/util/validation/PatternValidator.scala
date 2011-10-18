package info.daviot.util.validation

/**
 * Validates an object by matching its String representation with a regexp pattern.
 */
class PatternValidator(pattern: String, reason: String = null, emptyAllowed: Boolean = false) extends Validator {
  val usedPattern = if (emptyAllowed) "(" + pattern + ")|()" else pattern

  /**
   * Validates value by matching its String representation with the regexp pattern.
   *
   * @param value the object to validate
   * @throws ValidationFailedException when the validation failed
   */
  def validate(value: Any) = {
    if (emptyAllowed && value == null)
      None
    if (value == null || !value.toString().trim().matches(usedPattern))
      Some(if (reason == null) value + " does not match pattern " + pattern else reason)
    else None
  }

}