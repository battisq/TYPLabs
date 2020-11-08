package lab1.state_machine

import java.util.*

data class DeltaCell(val state: State,
                     val stack: Stack<Char>? = null,
                     val action: (() -> Unit)? = null
)