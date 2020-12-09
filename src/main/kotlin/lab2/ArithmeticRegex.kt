package lab2

import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.state_machine.State
import lab1.util.end
import java.util.regex.Pattern

class ArithmeticRegex {
    private val regex: Regex
    private val stateMachine: ArithmeticExpressionStateMachine

    init {
        val regexNumber: String = "(\\d+\\.?\\d*)"
        val regexNumberLog: String = "(\\d+\\.\\d+[Ee][+-]\\d+)"
        val regexVar: String = "(\\s*[A-Za-z_][A-Za-z0-9_]*\\s*)"
        val regexOperand = "(?:$regexNumberLog|$regexNumber|$regexVar)"
        val regexOp: String = "(\\+|\\*)"
        val groups: String =
            "(?:$regexOperand" +
                    "|$regexOp" +
                    "|(\\()" +
                    "|(\\))" +
                    "|(=)" +
            ")"

        // TODO
        /**
         * Рабочий регекс (Из C#)
         * @"^((?<var>\s*[A-Za-z_][A-Za-z0-9_]*\s*))(=)(\s*(?<open>\()\s*)*(?<operand>\s*((\d+\.\d+[Ee][+-]\d+)|(\d+\.?\d*)|([A-Za-z_][A-Za-z0-9_]*))\s*)(?<rec>\s*(?<op>\+|\*)\s*(\s*(?<open>\()\s*)*(?<operand>\s*((\d+\.?\d+[Ee][+-]\d+)|(\d+\.?\d*)|([A-Za-z_][A-Za-z0-9_]*))\s*)\s*(?<-open>\)\s*)*)+(?(open)(?!))"
         */
        regex = Regex(groups)
        stateMachine = ArithmeticExpressionStateMachine()
    }

    fun isExpression(expression: String): Boolean {
        val haltExpression = "${expression}${end[0]}"

        for (el in haltExpression) {
            stateMachine.moveNextState(el)

            if (stateMachine.currentState == State.ERROR)
                return false
        }

        if (stateMachine.currentState != State.HALT || !stateMachine.stack.isEmpty())
            return false

        return true
    }

    fun getGroups(expression: String): List<String> {
        return regex.findAll(expression).let {
            val temp: MutableList<String> = mutableListOf()

            it.forEach { el ->
                el.groups.forEachIndexed { index, matchGroup ->
                    if (index != 0 && matchGroup != null)
                        temp.add(matchGroup.value)
                }
            }

            temp
        }
    }
}