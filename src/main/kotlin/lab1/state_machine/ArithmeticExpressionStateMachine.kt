package lab1.state_machine

import lab1.state_machine.State.*
import lab1.util.Action
import lab1.util.DeltaCell
import lab1.util.end
import java.lang.StringBuilder
import java.util.*

class ArithmeticExpressionStateMachine {
    var currentState: State = _0

    private var stack: Stack<Char> = Stack()
    private val rpn: MutableList<String> = mutableListOf()
    private val stackOp: Stack<Char> = Stack()
    private val element: StringBuilder = StringBuilder()

    private val actions: List<Action> = mutableListOf<Action>().apply {
        // (
        this.add(object : Action {
            override fun action(el: Char) {
                stackOp.push(el)
                stack.push(el)
            }
        })

        // )
        this.add(object : Action {
            override fun action(el: Char) {
                if (stack.isEmpty()) {
                    currentState = ERROR

                    return
                } else {
                    if (element.toString() != "") {
                        rpn.add(element.toString())
                        element.clear()
                    }

                    stack.pop()

                    while (stackOp.peek() != '(')
                        rpn.add(stackOp.pop().toString())

                    stackOp.pop()
                }
            }
        })

        // + || * || =
        this.add(object : Action {
            override fun action(el: Char) {
                if (element.toString() != "") {
                    rpn.add(element.toString())
                    element.clear()
                }
                when {
                    stackOp.isEmpty() -> stackOp.push(el)
                    getPriority(stackOp.peek()) >= getPriority(el) -> {
                        while (getPriority(stackOp.peek()) >= getPriority(el)) {
                            rpn.add(stackOp.pop().toString())
                        }

                        stackOp.push(el)
                    }
                    else -> stackOp.push(el)
                }
            }
        })

        // letter, digits, +, -, E, e
        this.add(object : Action {
            override fun action(el: Char) {
                element.append(el)
            }
        })
    }

    private val matrixAction: Array<Array<Action?>> = Array(12) { i ->
        Array(12) { j ->
            when {
                i == 3 && j == 4 -> actions[0]

                j == 5 && (i == 4 || i == 6 || i == 9 || i == 10 || i == 11) -> actions[1]

                j == 3 && (i == 1 || i == 2)
                        || j == 6 && (i == 4 || i == 6 || i == 9 || i == 10 || i == 11)
                        || j == 7 && (i == 4 || i == 6 || i == 9 || i == 10 || i == 11) ->
                    actions[2]


                j == 0 && (i == 0 || i == 1 || i == 3 || i == 10)
                        || j == 1 && (i == 1 || i == 3 || i == 4 || i == 5 || i == 6 || i == 8 || i == 9 || i == 10)
                        || j == 6 && i == 7
                        || j == 8 && i == 7
                        || j == 9 && i == 4
                        || j == 10 && i == 6 -> actions[3]

                else -> null
            }
        }
    }

    private val matrix = TransitionMatrix.create(matrixAction)

    fun getNextState(symbol: Char): State {
        return getNextDeltaCell(symbol).state
    }

    private fun getNextDeltaCell(symbol: Char): DeltaCell {
        return matrix[currentState, symbol]
    }

    fun moveNextState(symbol: Char): State {
        val nextDeltaCell = getNextDeltaCell(symbol)
        nextDeltaCell.action?.action(symbol)
        currentState = nextDeltaCell.state

        if (currentState == HALT) {
            if (element.toString() != "") {
                rpn.add(element.toString())
            }
            while (!stackOp.isEmpty()) {
                rpn.add(stackOp.pop().toString())
            }
        }

        return currentState
    }

    private fun getPriority(symbol: Char): Int = when (symbol) {
        '=' -> 0
        '(', ')' -> 1
        '+' -> 2
        '*' -> 3
        else -> throw IllegalArgumentException("This symbol isn't ")
    }

    fun getRPN(expression: String): List<String> {
        val haltExpression = "${expression}${end[0]}"

        for (el in haltExpression) {
            moveNextState(el)

            if (currentState == ERROR)
                break
        }

        if (currentState != HALT || !stack.isEmpty())
            throw IllegalArgumentException()

        return rpn
    }
}