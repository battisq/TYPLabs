package lab2

import lab1.state_machine.ArithmeticExpressionStateMachine
import lab1.state_machine.State
import lab1.util.end
import java.util.regex.Pattern

class ArithmeticRegex {
    private val regex: Regex
    private val groupsReg: Regex
    init {
        val regexNumber: String = "(\\d+\\.?\\d*)"
        val regexNumberLog: String = "(\\d+\\.\\d+[Ee][+-]\\d+)"
        val regexVar: String = "(\\s*[A-Za-z_][A-Za-z0-9_]*\\s*)"
        val regexOperand = "($regexNumberLog|$regexNumber|$regexVar)"
        val regexOp: String = "(\\+|\\*)"
        val groupsEl =
            "(?:$regexOperand" +
                    "|$regexOp" +
                    "|(\\()" +
                    "|(\\))" +
                    "|(=)" +
                    ")"

        /**
         * Рабочий регекс (Из C#)
         * @"^((?<var>\s*[A-Za-z_][A-Za-z0-9_]*\s*))(=)(\s*(?<open>\()\s*)*(?<operand>\s*((\d+\.\d+[Ee][+-]\d+)|(\d+\.?\d*)|([A-Za-z_][A-Za-z0-9_]*))\s*)(?<rec>\s*(?<op>\+|\*)\s*(\s*(?<open>\()\s*)*(?<operand>\s*((\d+\.?\d+[Ee][+-]\d+)|(\d+\.?\d*)|([A-Za-z_][A-Za-z0-9_]*))\s*)\s*(?<-open>\)\s*)*)+(?(open)(?!))"
         */

        groupsReg = Regex(pattern = groupsEl)
        regex =
            Regex(pattern = "^$regexVar(=)(\\s*(?<open>\\()\\s*)*\\s*$regexOperand\\s*(?:\\s*$regexOp\\s*(\\s*(?<open1>\\()\\s*)*\\s*$regexOperand\\s*\\s*(?<close>\\)\\s*)*)+")
    }

    fun isExpression(expression: String): Boolean {
        if (!regex.matches(expression))
            return false

        with(Regex(pattern = "([()])").findAll(expression)) {
            var stack: Int = 0

            forEach {
                if (it.value == "(")
                    stack++
                else
                    stack--
            }

            return stack == 0
        }
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