package lab1.state_machine

import lab1.state_machine.State.*
import lab1.util.*

object TransitionMatrix {
    private val matrixTransition: HashMap<State, Array<State>> = HashMap<State, Array<State>>(12).apply {
        this[_0] = arrayOf(_1, ERROR, _0, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR)
        this[_1] = arrayOf(_1, _1, _2, _3, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR)
        this[_2] = arrayOf(ERROR, ERROR, _2, _3, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR)
        this[_3] = arrayOf(_10, _4, _3, ERROR, _3, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR)
        this[_4] = arrayOf(ERROR, _4, _11, ERROR, ERROR, _11, _3, _3, ERROR, _5, ERROR, HALT)
        this[_5] = arrayOf(ERROR, _6, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR)
        this[_6] = arrayOf(ERROR, _6, _11, ERROR, ERROR, _11, _3, _3, ERROR, ERROR, _7, HALT)
        this[_7] = arrayOf(ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, _8, ERROR, _8, ERROR, ERROR, ERROR)
        this[_8] = arrayOf(ERROR, _9, _11, ERROR, ERROR, _11, _3, _3, ERROR, ERROR, ERROR, HALT)
        this[_9] = arrayOf(ERROR, _9, _11, ERROR, ERROR, _11, _3, _3, ERROR, ERROR, ERROR, HALT)
        this[_10] = arrayOf(_10, _10, _11, ERROR, ERROR, _11, _3, _3, ERROR, ERROR, ERROR, HALT)
        this[_11] = arrayOf(ERROR, ERROR, _11, ERROR, ERROR, _11, _3, _3, ERROR, ERROR, ERROR, HALT)
    }

    internal lateinit var delta: HashMap<State, MutableList<DeltaCell>>

    fun create(matrixAction: Array<Array<Action?>>): HashMap<State, MutableList<DeltaCell>> {
        delta = HashMap(12)
        val values = State.values()

        for (i: Int in matrixAction.indices) {
            delta[values[i]] = mutableListOf()

            for (j: Int in matrixAction[0].indices) {
                delta[values[i]]!!.add(
                    DeltaCell(
                        matrixTransition[values[i]]!![j],
                        matrixAction[i][j]
                    )
                )
            }
        }

        return delta
    }

    fun getIndex(symbol: Char): Int = when {
        letters.contains(symbol) -> 0
        digits.contains(symbol) -> 1
        ' ' == symbol -> 2
        '=' == symbol -> 3
        '(' == symbol -> 4
        ')' == symbol -> 5
        '+' == symbol -> 6
        '*' == symbol -> 7
        '-' == symbol -> 8
        '.' == symbol -> 9
        logarithmicSymbol.contains(symbol) -> 10
        end.contains(symbol) -> 11
        else -> -1
    }
}

operator fun HashMap<State, MutableList<DeltaCell>>.get(state: State, symbol: Char): DeltaCell {
    return TransitionMatrix.delta[state]!![TransitionMatrix.getIndex(symbol)]
}
