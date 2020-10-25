package lab1.state_machine

abstract class StateMachine {
    abstract fun getResultState(expression: String): Result
    enum class Result { HALT, ERROR }
}