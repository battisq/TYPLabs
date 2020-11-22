package lab1.validation

import lab1.state_machine.State
import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.state_machine.end

class StateMachineValidator : Validator {
    override fun isExpression(expression: String): Boolean {
        val stateMachine = ArithmeticExpressionStateMachine()
        val haltExpression = "${expression}${end[0]}"

        for (el in haltExpression) {
            stateMachine.moveNextState(el)

            if (stateMachine.currentState == State.ERROR)
                break
        }

        return stateMachine.currentState == State.HALT
    }
}