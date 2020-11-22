package lab1.state_machine

import java.util.HashMap

abstract class StateMachine {
    protected abstract val transitions: HashMap<StateTransition, DeltaCell>

    abstract fun getNextState(symbol: Char): State
    protected abstract fun getNextDeltaCell(symbol: Char): DeltaCell
    abstract fun moveNextState(symbol: Char): State
}