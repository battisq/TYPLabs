import lab1.Generator
import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.util.FileHelper

fun main() {
    val exp = FileHelper.read()
    val generator = Generator()

//<editor-fold desc="Lab 1">
    val rpn = ArithmeticExpressionStateMachine().getRPN(exp)
    val tree = generator.generateTree(rpn)
    generator.generateUnoptimizedCode(tree)
//</editor-fold>

    FileHelper.write(
        generator.generateTable(rpn),
        tree.code,
        generator.generateOptimizedCode(tree.code)
    )

}