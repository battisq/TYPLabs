package validation

interface Validator {
    fun validationExpression(expression: String) : Boolean
}