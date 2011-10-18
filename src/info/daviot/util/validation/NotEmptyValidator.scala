package info.daviot.util.validation

/**
 * Validates that an object is not null and its string representation is not empty.
 */
class NotEmptyValidator(reason: String) extends PatternValidator(".+", reason, false)
