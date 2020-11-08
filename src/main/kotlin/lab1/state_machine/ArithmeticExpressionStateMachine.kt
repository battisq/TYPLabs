package lab1.state_machine

import lab1.state_machine.State.*
import lab1.state_machine.StateTransition.TransitionState.*
import java.util.*

class ArithmeticExpressionStateMachine: StateMachine() {
    var stack: Stack<Char> = Stack()
        private set
    var currentState: State = _0
        private set

    override val transitions = HashMap<StateTransition, DeltaCell>().apply {
        this[StateTransition(_0, letters, TABLE)] = DeltaCell(_1, stack)
        this[StateTransition(_0, whitespace, TABLE)] = DeltaCell(_0, stack)

        this[StateTransition(_1, letters, TABLE)] = DeltaCell(_1, stack)
        this[StateTransition(_1, digits, TABLE)] = DeltaCell(_1, stack)
        this[StateTransition(_1, whitespace, TABLE)] = DeltaCell(_2, stack)
        this[StateTransition(_1, equal, TABLE)] = DeltaCell(_3, stack)

        this[StateTransition(_2, whitespace, TABLE)] = DeltaCell(_2, stack)
        this[StateTransition(_2, equal, TABLE)] = DeltaCell(_3, stack)

        this[StateTransition(_3, letters, TABLE)] = DeltaCell(_10, stack)
        this[StateTransition(_3, digits, TABLE)] = DeltaCell(_4, stack)
        this[StateTransition(_3, whitespace, TABLE)] = DeltaCell(_3, stack)
        this[StateTransition(_3, openingBracket, TABLE)] = DeltaCell(_3, stack) {
            stack.push(openingBracket[0])
        }

        this[StateTransition(_4, digits, TABLE)] = DeltaCell(_4, stack)
        this[StateTransition(_4, whitespace, TABLE)] = DeltaCell(_11, stack)
        this[StateTransition(_4, operation, TABLE)] = DeltaCell(_3, stack)
        this[StateTransition(_4, point, TABLE)] = DeltaCell(_5, stack)
        this[StateTransition(_4, end, TABLE)] = DeltaCell(HALT, stack)
        this[StateTransition(_4, closingBracket, TABLE)] = DeltaCell(_11, stack) {
            if (stack.isEmpty())
                currentState = ERROR
            else
                stack.pop()
        }

        this[StateTransition(_5, digits, TABLE)] = DeltaCell(_6, stack)

        this[StateTransition(_6, digits, TABLE)] = DeltaCell(_6, stack)
        this[StateTransition(_6, whitespace, TABLE)] = DeltaCell(_11, stack)
        this[StateTransition(_6, operation, TABLE)] = DeltaCell(_3, stack)
        this[StateTransition(_6, logarithmicSymbol, TABLE)] = DeltaCell(_7, stack)
        this[StateTransition(_6, end, TABLE)] = DeltaCell(HALT, stack)
        this[StateTransition(_6, closingBracket, TABLE)] = DeltaCell(_11, stack) {
            if (stack.isEmpty())
                currentState = ERROR
            else
                stack.pop()
        }

        this[StateTransition(_7, logarithmicSigns, TABLE)] = DeltaCell(_8, stack)

        this[StateTransition(_8, digits, TABLE)] = DeltaCell(_9, stack)

        this[StateTransition(_9, digits, TABLE)] = DeltaCell(_9, stack)
        this[StateTransition(_9, whitespace, TABLE)] = DeltaCell(_11, stack)
        this[StateTransition(_9, operation, TABLE)] = DeltaCell(_3, stack)
        this[StateTransition(_9, end, TABLE)] = DeltaCell(HALT, stack)
        this[StateTransition(_9, closingBracket, TABLE)] = DeltaCell(_11, stack) {
            if (stack.isEmpty())
                currentState = ERROR
            else
                stack.pop()
        }

        this[StateTransition(_10, letters, TABLE)] = DeltaCell(_10, stack)
        this[StateTransition(_10, digits, TABLE)] = DeltaCell(_10, stack)
        this[StateTransition(_10, whitespace, TABLE)] = DeltaCell(_11, stack)
        this[StateTransition(_10, operation, TABLE)] = DeltaCell(_3, stack)
        this[StateTransition(_10, end, TABLE)] = DeltaCell(HALT, stack)
        this[StateTransition(_10, closingBracket, TABLE)] = DeltaCell(_11, stack) {
            if (stack.isEmpty())
                currentState = ERROR
            else
                stack.pop()
        }

        this[StateTransition(_11, whitespace, TABLE)] = DeltaCell(_11, stack)
        this[StateTransition(_11, operation, TABLE)] = DeltaCell(_3, stack)
        this[StateTransition(_11, end, TABLE)] = DeltaCell(HALT, stack)
        this[StateTransition(_11, closingBracket, TABLE)] = DeltaCell(_11, stack) {
            if (stack.isEmpty())
                currentState = ERROR
            else
                stack.pop()
        }
    }

    override fun getNext(symbol: Char): State {
        return getNextDeltaCell(symbol).state
    }

    override fun getNextDeltaCell(symbol: Char): DeltaCell {
        val transition = StateTransition(currentState, listOf(symbol), INPUT)
        return transitions.getOrDefault(transition, DeltaCell(ERROR))
    }

    override fun moveNext(symbol: Char): State {
        val nextDeltaCell = getNextDeltaCell(symbol)
        currentState = nextDeltaCell.state
        nextDeltaCell.action?.invoke()
        return currentState
    }
}

