package lab1

import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.tree.TokenTree
import lab1.util.*
import java.util.*
import kotlin.math.max

class Generator {
    fun generateTable(splitExp: List<String>): String {
        val parameterNames = splitExp.filter {
            it != "(" && it != ")" && it != "*" && it != "+" && it != "="
        }

        var i = 1

        return parameterNames.map {
            String.format(
                "%2d %7s\t\t%s",
                i++,
                it,
                if (letters.contains(it[0]))
                    "Переменная с плавающей точкой"
                else
                    "Константа с плавающей точкой"
            )
        }.joinToString("\n")
    }

    // infix to reverse polish notation (postfix)
    fun generateRPN(splitExp: List<String>): List<String> {
        val op: Stack<String> = Stack()
        val resultExp = mutableListOf<String>()

        for ((index, value) in splitExp.withIndex()) {
            when {
                digits.contains(value[0]) -> resultExp.add(value)
                letters.contains(value[0]) -> resultExp.add(value)
                operation.contains(value[0]) || equal.contains(value[0]) -> when {
                    op.isEmpty() -> op.push(value)
                    getPriority(op.peek()) >= getPriority(value) -> {
                        while(getPriority(op.peek()) >= getPriority(value)) {
                            resultExp.add(op.pop())
                        }

                        op.push(value)
                    }
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

    fun generateTree(expressionRPN: List<String>): TokenTree {
        val stack = Stack<TokenTree>()

        expressionRPN.forEach {
            if (digits.contains(it[0]) || letters.contains(it[0]))
                stack.push(TokenTree(oper = it, left = null, right = null))
            else if (operation.contains(it[0]) || equal.contains(it[0])) {
                val last = stack.pop()
                val preLast = stack.pop()
                val operToken = TokenTree(oper = it, left = preLast, right = last)
                stack.push(operToken)
            }
        }

        val tree = stack.pop()
        val maxLevel = setLevels(tree)
        invertLevels(tree, maxLevel + 1)

        return tree
    }

    private fun setLevels(tree: TokenTree, level: Int = 0): Int {
        if (tree.right == null && tree.left == null)
            return 0

        var max = level

        max = max(max, setLevels(tree.right!!, level + 1))
        max = max(max, setLevels(tree.left!!, level + 1))

        tree.level = level

        return max
    }

    private fun invertLevels(tree: TokenTree, maxLevel: Int) {
        if (tree.left == null && tree.right == null) {
            tree.level = 0
            return
        }

        invertLevels(tree.right!!, maxLevel)
        tree.level = maxLevel - tree.level
        invertLevels(tree.left!!, maxLevel)
    }

    fun generateUnoptimizedCode(tree: TokenTree?) {
        if (tree != null) {

            generateUnoptimizedCode(tree.right)
            generateUnoptimizedCode(tree.left)

            when {
                tree.level == 0 && letters.contains(tree.oper[0]) ->
                    tree.code = tree.oper

                tree.level == 0 && digits.contains(tree.oper[0]) ->
                    tree.code = "=${tree.oper}"

                tree.level != 0 && tree.oper == "=" -> {
                    tree.code = String.format(
                        "LOAD\t%s\nSTORE\t%s",
                        tree.right!!.code,
                        tree.left!!.code,
                    )
                }

                tree.level != 0 && tree.oper == "+" -> {
                    tree.code = String.format(
                        "%s\nSTORE\t\$%d\nLOAD\t%s\nADD\t\t\$%d",
                        tree.right!!.code,
                        tree.level,
                        tree.left!!.code,
                        tree.level
                    )
                }

                tree.level != 0 && tree.oper == "*" -> {
                    tree.code = String.format(
                        "%s\nSTORE\t\$%d\nLOAD\t%s\nMPY\t\t\$%d",
                        tree.right!!.code,
                        tree.level,
                        tree.left!!.code,
                        tree.level
                    )
                }
            }
        }
    }

    fun generateOptimizedCode(unoptimizedCode: String): String {
        val splitCode = unoptimizedCode.split("\n")
        val code: LinkedList<MutableList<String>> = LinkedList(splitCode.map {
            it.replace("\t", " ")
                .replace("  ", " ")
                .split(" ")
                .toMutableList()
        })

        for (i in code.indices) {
            if (i < code.size - 1
                && code[i][0].contains("LOAD")
                && code[i + 1][0].contains("ADD")
            ) {
                code[i][0] = code[i][0].replace("LOAD", "ADD")
                code[i + 1][0] = code[i + 1][0].replace("ADD", "LOAD")
                Collections.swap(code, i, i + 1)
            }
        }

        for (i in code.indices) {
            if (i < code.size - 1
                && code[i][0].contains("LOAD")
                && code[i + 1][0].contains("MPY")
            ) {
                code[i][0] = code[i][0].replace("LOAD", "MPY")
                code[i + 1][0] = code[i + 1][0].replace("MPY", "LOAD")
                Collections.swap(code, i, i + 1)
            }
        }

        var i = 0
        while (i < code.size) {
            if (i < code.size - 1
                && code[i][0] == "STORE"
                && code[i + 1][0] == "LOAD"
                && code[i][1] == code[i + 1][1]
            ) {
                code.removeAt(i)
                code.removeAt(i)
                i--
            }

            i++
        }

        i = 0
        while (i < code.size) {
            if (i < code.size - 2
                && code[i][0] == "LOAD"
                && code[i + 1][0] == "STORE"
                && code[i + 2][0] == "LOAD"
            ) {
                val box = code[i][1]
                val box1 = code[i + 1][1]

                code.removeAt(i)
                code.removeAt(i)

                for (j in i until code.size)
                    if (code[j][1] == box1)
                        code[j][1] = box

                i--
            }

            i++
        }

        return code.map {
            if (it[0] == "ADD" || it[0] == "MPY")
                "${it[0]}\t\t${it[1]}"
            else
                "${it[0]}\t${it[1]}"
        }.joinToString("\n")
    }

    fun getPriority(symbol: String): Int = when (symbol[0]) {
        '=' -> 0
        '(', ')' -> 1
        '+' -> 2
        '*' -> 3
        else -> throw IllegalArgumentException("This symbol isn't ")
    }
}