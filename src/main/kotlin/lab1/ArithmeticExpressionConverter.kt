package lab1

import lab1.state_machine.*
import lab1.tree.TokenTree
import lab1.validation.Validator
import java.util.*
import kotlin.collections.HashSet

class ArithmeticExpressionConverter(private val validator: Validator) {

    fun toElementList(expression: String): List<String> {
        if (!validator.isExpression(expression))
            throw IllegalArgumentException("expression isn't arithmetic expression")

        val list = mutableListOf<String>()
        val exp = expression.replace(" ", "")
        val str = StringBuilder()
        val cond = listOf('=', '*', '(', ')')

        for ((index, el) in exp.withIndex()) {
            if (cond.contains(el)
                || (el == '+'
                        && !(logarithmicSymbol.contains(exp[index - 1])
                        && digits.contains(exp[index - 2])))
            ) {
                if (str.toString() != "")
                    list.add(str.toString())

                list.add(el.toString())
                str.clear()
            } else str.append(el)
        }

        if (str.toString() != "")
            list.add(str.toString())

        return list
    }

    // infix to reverse polish notation (postfix)
    fun toRPN(expression: List<String>): List<String> {
        val op: Stack<String> = Stack()
        val resultExp = mutableListOf<String>()

        for ((index, value) in expression.withIndex()) {
            when {
                digits.contains(value[0]) -> resultExp.add(value)
                letters.contains(value[0]) -> resultExp.add(value)
                operation.contains(value[0]) || equal.contains(value[0]) -> when {
                    op.isEmpty() -> op.push(value)
                    getPriority(op.peek()) > getPriority(value) -> resultExp.add(op.pop())
                    else -> op.push(value)
                }
                openingBracket.contains(value[0]) -> op.push(value)
                closingBracket.contains(value[0]) -> {
                    while (op.peek() != "(")
                        resultExp.add(op.pop())

                    op.pop()
                }
            }
        }

        while (!op.isEmpty())
            resultExp.add(op.pop())

        return resultExp
    }

    private fun getPriority(symbol: String): Int = when (symbol) {
        "=" -> 0
        "(", ")" -> 1
        "+" -> 2
        "*" -> 3
        else -> throw IllegalArgumentException("This symbol isn't ")
    }

    fun toTable(splitExp: List<String>): String {
        val parameterNames = splitExp.filter {
            it != "(" && it != ")" && it != "*" && it != "+" && it != "="
        }

        var i = 0

        return parameterNames.map {
            String.format(
                "%d\t%s\t%s",
                i++,
                it,
                if (letters.contains(it[0]))
                    "Переменная с плавающей точкой"
                else
                    "Константа с плавающей точкой"
            )
        }.joinToString("\n")
    }

    fun toTree(expressionRPN: List<String>): TokenTree {
        val stack = Stack<TokenTree>()
        var level = 0

        expressionRPN.forEach {
            if (digits.contains(it[0]) || letters.contains(it[0]))
                stack.push(TokenTree(oper = it, left = null, right = null))
            else if (operation.contains(it[0]) || equal.contains(it[0])) {
                val last = stack.pop()
                val preLast = stack.pop()
                val operToken = TokenTree(oper = it, level = ++level, left = preLast, right = last)
                stack.push(operToken)
            }
        }

        return stack.pop()
    }


}