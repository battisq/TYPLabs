package lab1.state_machine

import lab1.state.State
import lab1.state.State.*
import java.util.*

class ArithmeticExpressionStateMachine : StateMachine() {
    private val finalStates = listOf(_4, _6, _9, _10, _11)

    private val digits = '0'..'9'
    private val op = listOf('+', '*')
    private val logarithmicSigns = listOf('+', '-')
    private val logarithmicSymbol = listOf('e', 'E')
    private val letters = mutableListOf('_').apply {
        this.addAll('a'..'z')
        this.addAll('A'..'Z')
    }

    private fun getNextState(symbol: Char, currentState: State, stack: Stack<Char>): State {
        return when (currentState) {
            _0 -> when {
                symbol == ' ' -> currentState
                letters.contains(symbol) -> _1
                else -> ERROR
            }
            _1 -> when {
                letters.contains(symbol) || digits.contains(symbol) -> currentState
                symbol == '=' -> _3
                symbol == ' ' -> _2
                else -> ERROR
            }
            _2 -> when (symbol) {
                ' ' -> currentState
                '=' -> _3
                else -> ERROR
            }
            _3 -> when {
                symbol == '(' || symbol == ' ' -> {
                    if (symbol == '(')
                        stack.push(symbol)

                    currentState
                }
                digits.contains(symbol) -> _4
                letters.contains(symbol) -> _10
                else -> ERROR
            }
            _4 -> when {
                digits.contains(symbol) -> currentState
                op.contains(symbol) -> _3
                symbol == '.' -> _5
                symbol == ')' -> if (!stack.isEmpty()) {
                    stack.pop()
                    _11
                } else
                    ERROR
                symbol == ' ' -> _11
                else -> ERROR
            }
            _5 -> when {
                digits.contains(symbol) -> _6
                else -> ERROR
            }
            _6 -> when {
                digits.contains(symbol) -> currentState
                op.contains(symbol) -> _3
                logarithmicSymbol.contains(symbol) -> _7
                symbol == ')' -> {
                    if (stack.isEmpty())
                        return ERROR

                    if (symbol == ')')
                        stack.pop()

                    _11
                }
                symbol == ' ' -> _11
                else -> ERROR
            }
            _7 -> when {
                logarithmicSigns.contains(symbol) -> _8
                else -> ERROR
            }
            _8 -> when {
                digits.contains(symbol) -> _9
                else -> ERROR
            }
            _9 -> when {
                digits.contains(symbol) -> currentState
                op.contains(symbol) -> _3
                symbol == ')' -> {
                    if (stack.isEmpty())
                        return ERROR

                    if (symbol == ')')
                        stack.pop()

                    _11
                }
                symbol == ' ' -> _11
                else -> ERROR
            }
            _10 -> when {
                digits.contains(symbol) || letters.contains(symbol) -> currentState
                symbol == ')' -> {
                    if (stack.isEmpty())
                        return ERROR

                    if (symbol == ')')
                        stack.pop()

                    _11
                }
                symbol == ' ' -> _11
                op.contains(symbol) -> _3
                else -> ERROR
            }
            _11 -> when {
                symbol == ')' -> {
                    if (stack.isEmpty())
                        return ERROR

                    if (symbol == ')')
                        stack.pop()

                    currentState
                }
                symbol == ' ' -> currentState
                op.contains(symbol) -> _3
                else -> ERROR
            }
            ERROR -> throw IllegalStateException("The state is already FAULT")
        }
    }

    override fun getResultState(expression: String): Result {
        val exp = expression.replace("\n", "")
        var resultState = Result.ERROR
        val stack: Stack<Char> = Stack()
        var currentState = _0

        for ((index, el) in exp.withIndex()) {
            currentState = getNextState(el, currentState, stack)

            if (currentState == ERROR)
                break

            if (index == exp.length - 1
                && stack.empty()
                && finalStates.contains(currentState)
            )
                resultState = Result.HALT
        }

        return resultState
    }
}