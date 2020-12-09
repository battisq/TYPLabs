import lab1.Generator
import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.util.FileHelper
import lab2.ArithmeticRegex
import java.util.*

fun main() {
    val variants = mutableListOf<String>(
        "cost=q*(price+4*(x+y)*tax)",
        "COST = (PRICE+TAX)*0.98",
        "a=s+d+d+(d+(d+(d+d)))",
        "a=(c+f9+(f+d+d*1.25E+485+1.266)+f+(f+f+ff))",
        "a=(a+d+s+d+(f+g))+c",
        "cost=PRICE*TAX*q+v"
    )

    val exp = FileHelper.read()
    val generator = Generator()

//<editor-fold desc="Lab 1">
//    val rpn = ArithmeticExpressionStateMachine().getRPN(exp)
//</editor-fold>

//<editor-fold desc="Lab 2">
    val regex: ArithmeticRegex = ArithmeticRegex()

    if (!regex.isExpression(exp))
        throw IllegalArgumentException()

    val splitExp: List<String> = regex.getGroups(exp)
    val rpn = generator.generateRPN(splitExp)
//</editor-fold>

    val tree = generator.generateTree(rpn)
    generator.generateUnoptimizedCode(tree)


    FileHelper.write(
        generator.generateTable(rpn),
        tree.code,
        generator.generateOptimizedCode(tree.code)
    )
}