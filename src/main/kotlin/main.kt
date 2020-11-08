import lab1.validation.StateMachineValidator

fun main() {
/*
    val validator: Validator = ArithmeticExpressionStateMachineValidator()
    val converter = ArithmeticExpressionConverter()
    val exp = "   a = b + 3.25E+3  *(4+2)  "
    val exp1 = "a = (b + (c +1))"
    val polExp = converter.toRPN(exp)
    println(
        polExp.replace(" +", "")
            .replace(" *", "")
            .replace(" = ", "")
            .split(" ")
    )

*/

    /*println(validator.validationExpression("a = b + c"))
    println(validator.validationExpression("a = b1 + c"))
    println(validator.validationExpression("a = 2 + c"))
    println(validator.validationExpression("a = 2.85 + c"))
    println(validator.validationExpression("   a = b + 3.25E+3    "))
    println(validator.validationExpression("a = b + (c+ 1)"))
    println(validator.validationExpression("a = (b + (c +1)) )"))*/

    /*var stack = Stack<Char>().apply {
        this.push('(')
        this.push('0')
        this.push(')')
    }

    println(stack)
    test(stack)
    println(stack)*/


    val validator = StateMachineValidator()
    println(validator.isExpression("a = b1 + c"))
    println(validator.isExpression("a = 2 + c"))
    println(validator.isExpression("a = 2.85 + c"))
    println(validator.isExpression("a=b+3.25E+3"))
    println(validator.isExpression("a = b + (c+ 1)"))
    println(validator.isExpression("a = (b + (c +1)) )"))
}