import lab1.ArithmeticExpressionConverter
import lab1.CodeGenerator
import lab1.tree.TokenTree
import lab1.validation.StateMachineValidator

fun main() {
    /*
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
    val converter = ArithmeticExpressionConverter(validator)
    val exp = "   a = b + 3.25E+3  *(4+2)  "
    val exp1 = "a = (b + (c +1))"
    println(converter.toRPN(converter.toElementList(exp)))

//    println(converter.toElementList(exp))
//    println(converter.toElementList(exp1))

//    println(validator.isExpression("a = b1 + c"))
//    println(validator.isExpression("a = 2 + c"))
//    println(validator.isExpression("a = 2.85 + c"))
//    println(validator.isExpression("a=b+3.25E+3"))
//    println(validator.isExpression("a = b + (c+ 1)"))
//    println(validator.isExpression("a = (b + (c +1)) )"))
    */

    val exp = "COST = (PRICE+TAX)*0.98 + (1+2)*3"
    val validator = StateMachineValidator()

    if (validator.isExpression(exp)) {
        val converter = ArithmeticExpressionConverter(validator)

        val splitExp = converter.toElementList(exp)
        val rpn = converter.toRPN(splitExp)
        val tree = converter.toTree(rpn)
        val generator = CodeGenerator()
        generator.generateUnoptimizedCode(tree)

//        println(converter.toTable(splitExp))
//        println()

        println(tree.code)
        println()

        println(generator.generateOptimizedCode(tree.code))
    }
}