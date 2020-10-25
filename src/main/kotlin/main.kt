import lab1.ArithmeticExpressionStateMachineValidator
import validation.Validator
import java.util.*

fun main(args: Array<String>) {
    val validator: Validator = ArithmeticExpressionStateMachineValidator()

    println(validator.validationExpression("a = b + c"))
    println(validator.validationExpression("a = b1 + c"))
    println(validator.validationExpression("a = 2 + c"))
    println(validator.validationExpression("a = 2.85 + c"))
    println(validator.validationExpression("   a = b + 3.25E+3    "))
    println(validator.validationExpression("a = b + (c+ 1)"))
    println(validator.validationExpression("a = (b + (c +1)) )"))

/*    var stack = Stack<Char>().apply {
        this.push('(')
        this.push('0')
        this.push(')')
    }

    println(stack)
    test(stack)
    println(stack)*/

}

fun test (stack: Stack<Char>) {
    stack.pop()
}