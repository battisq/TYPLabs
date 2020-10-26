package lab1

import lab1.state.State
import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.state_machine.StateMachine
import lab1.validation.ArithmeticExpressionStateMachineValidator
import lab1.validation.Validator
import utils.digits
import utils.letters
import utils.operation
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.util.*

class ArithmeticExpressionConverter {

    private val stateMachine = ArithmeticExpressionStateMachine()

    // infix to reverse polish notation (postfix)
    fun toRPN(expression: String): String {
        if (stateMachine.getResultState(expression) != StateMachine.Result.HALT)
            throw IllegalArgumentException("Invalid arithmetic expression")

        val op: Stack<Char> = Stack()
        val resultExp = StringBuilder()
        var currentIndex = 0
        val operand = StringBuilder()

        while (currentIndex < expression.length) {

            when {
                digits.contains(expression[currentIndex]) -> {
                    var state = State._4
                    operand.setLength(0)

                    while (state >= State._4 && state <= State._9) {
                        operand.append(expression[currentIndex])
                        currentIndex++

                        if (currentIndex == expression.length)
                            break

                        state = stateMachine.getNextState(expression[currentIndex], state)
                    }

                    resultExp.append("$operand ")
                }
                letters.contains(expression[currentIndex]) -> {
                    var state = State._10
                    operand.setLength(0)

                    while (state == State._10) {
                        operand.append(expression[currentIndex])
                        currentIndex++

                        if (currentIndex == expression.length)
                            break

                        state = stateMachine.getNextState(expression[currentIndex], state)
                    }

                    resultExp.append("$operand ")
                }
                operation.contains(expression[currentIndex])
                        || expression[currentIndex] == '=' -> {
                    when {
                        op.isEmpty() -> op.push(expression[currentIndex])
                        getPriority(op.peek()) > getPriority(expression[currentIndex]) ->
                            resultExp.append("${op.pop()} ")
                        else -> op.push(expression[currentIndex])
                    }
                    currentIndex++
                }
                expression[currentIndex] == '(' -> {
                    op.push(expression[currentIndex])
                    currentIndex++
                }
                expression[currentIndex] == ')' -> {
                    while (op.peek() != '(')
                        resultExp.append("${op.pop()} ")
                    op.pop()
                    currentIndex++
                }
                else -> currentIndex++
            }
        }

        while (!op.isEmpty())
            resultExp.append("${op.pop()} ")

        return resultExp.toString()
    }

    private fun getPriority(symbol: Char): Int = when (symbol) {
        '=' -> 0
        '(', ')' -> 1
        '+' -> 2
        '*' -> 3
        else -> throw IllegalArgumentException("This symbol isn't ")
    }
}