package lab1.validation

import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.state_machine.StateMachine
import lab1.validation.Validator

class ArithmeticExpressionStateMachineValidator : Validator {
    override fun validationExpression(expression: String): Boolean {
        val stateMachine: StateMachine = ArithmeticExpressionStateMachine()
        return stateMachine.getResultState(expression) == StateMachine.Result.HALT
    }
}