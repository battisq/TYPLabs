package lab1.state_machine

import lab1.state_machine.StateTransition.TransitionState.*

class StateTransition(
    val state: State,
    val symbols: List<Char>,
    private val transitionState: TransitionState,
) {
    override fun equals(other: Any?): Boolean {
        return if (other != null
            && transitionState == TABLE
            && (other as StateTransition).transitionState == TABLE
        )
            state == other.state && symbols == other.symbols
        else if (other != null
            && transitionState == INPUT
            && (other as StateTransition).transitionState == TABLE
        )
            state == other.state && other.symbols.contains(symbols[0])
        else
            false
    }

    override fun hashCode(): Int {
        return 17 + 31 * state.hashCode() + 31
    }

    enum class TransitionState { TABLE, INPUT }
}