package lab1.util

import lab1.state_machine.State

data class DeltaCell(
    var state: State,
    var action: Action? = null
)

interface Action {
    fun action(el: Char)
}