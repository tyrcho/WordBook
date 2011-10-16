package info.daviot.util.validation;

/**
 * Specifies the interface for a validator object.
 */
trait Validator {
  /**
   * Implementations of this interface will define this method to validate a
   * value.
   *
   * @param value
   *            the object to validate
   * @return None or Some(an error message) or Seq(messages)
   */
  def validate(value: Any): Iterable[String]

}
