package info.daviot.util.validation

/**
 * Applies a group of validators to a group of objects.
 * The groups are represented by a Map where the keys
 * are the same between validators and value objects.
 */
class GroupValidator(validatorMap: Map[String, Validator] = Map.empty, reason: String = null) extends Validator {

  /**
   * Validates a map of objects by applying to each object in the map
   * to the corresponding validator (same key).
   *
   * @param value the HashMap to validate
   * @throws SeveralFailedValidationsException when the validation failed
   * @throws ClassCastException if value cannot be assigned to a Map
   */
  def validate(value: Any) = {
    value match {
      case v: Map[String, Any] => v.map(kv => validatorMap(kv._1).validate(kv._2)).flatten
      case _ => Some("Bug : expected Map[String, Any], not " + value)
    }
  }
}
