package lab1

import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.state_machine.StateMachine
import validation.Validator

class ArithmeticExpressionStateMachineValidator : Validator {
    override fun validationExpression(expression: String): Boolean {
        val stateMachine: StateMachine = ArithmeticExpressionStateMachine()
        return stateMachine.getResultState(expression) == StateMachine.Result.HALT
    }
}