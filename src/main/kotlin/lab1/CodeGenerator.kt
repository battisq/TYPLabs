package lab1

import lab1.state_machine.digits
import lab1.state_machine.letters
import lab1.tree.TokenTree
import java.util.*
import kotlin.collections.ArrayList

class CodeGenerator {
    fun generateUnoptimizedCode(tree: TokenTree?) {
        if (tree != null) {

            generateUnoptimizedCode(tree.right)
            generateUnoptimizedCode(tree.left)

            when {
                tree.level == 0 && letters.contains(tree.oper[0]) ->
                    tree.code = tree.oper

                tree.level == 0 && digits.contains(tree.oper[0]) ->
                    tree.code = "=${tree.oper}"

                tree.level != 0 && tree.oper == "=" -> {
                    tree.code = String.format(
                        "LOAD\t%s\nSTORE\t%s",
                        tree.right!!.code,
                        tree.left!!.code,
                    )
                }

                tree.level != 0 && tree.oper == "+" -> {
                    tree.code = String.format(
                        "%s\nSTORE\t\$%d\nLOAD\t%s\nADD\t\t\$%d",
                        tree.right!!.code,
                        tree.level,
                        tree.left!!.code,
                        tree.level
                    )
                }

                tree.level != 0 && tree.oper == "*" -> {
                    tree.code = String.format(
                        "%s\nSTORE\t\$%d\nLOAD\t%s\nMPY\t\t\$%d",
                        tree.right!!.code,
                        tree.level,
                        tree.left!!.code,
                        tree.level
                    )
                }
            }
        }
    }

    fun generateUnoptimizedCode(expressionRPN: List<String>): String {


        return ""
    }

    fun generateOptimizedCode(unoptimizedCode: String): String {
        val splitCode = unoptimizedCode.split("\n")
        val code: LinkedList<MutableList<String>> = LinkedList(splitCode.map {
            it.replace("\t", " ")
                .replace("  ", " ")
                .split(" ")
                .toMutableList()
        })

        /*     for (i in code.indices) {
                 if (i < code.size - 1
                     && code[i][0].contains("LOAD")
                     && code[i + 1][0].contains("ADD")
                 ) {
                     code[i][0].replace("LOAD", "ADD")
                     code[i][0].replace("ADD", "LOAD")
                     Collections.swap(code, i, i + 1)
                 } else if (i < code.size - 1
                     && code[i][0].contains("LOAD")
                     && code[i + 1][0].contains("MPY")
                 ) {
                     code[i][0].replace("LOAD", "MPY")
                     code[i][0].replace("MPY", "LOAD")
                     Collections.swap(code, i, i + 1)
                 } else if (i < code.size - 1
                     && code[i][0] == "STORE"
                     && code[i + 1][0] == "LOAD"
                     && code[i][1] == code[i + 1][1]
                 ) {
                     code.removeAt(i)
                     code.removeAt(i + 1)
                 } else if (i < code.size - 2
                     && code[i][0] == "LOAD"
                     && code[i + 1][0] == "STORE"
                     && code[i + 2][0] == "LOAD"
                 ) {
                     code.removeAt(i)
                     code.removeAt(i + 1)
                 }

             }*/

        for (i in code.indices) {
            if (i < code.size - 1
                && code[i][0].contains("LOAD")
                && code[i + 1][0].contains("ADD")
            ) {
                code[i][0] = code[i][0].replace("LOAD", "ADD")
                code[i + 1][0] = code[i + 1][0].replace("ADD", "LOAD")
                Collections.swap(code, i, i + 1)
            }
        }

        for (i in code.indices) {
            if (i < code.size - 1
                && code[i][0].contains("LOAD")
                && code[i + 1][0].contains("MPY")
            ) {
                code[i][0] = code[i][0].replace("LOAD", "MPY")
                code[i + 1][0] = code[i + 1][0].replace("MPY", "LOAD")
                Collections.swap(code, i, i + 1)
            }
        }

        var i = 0
        while (i < code.size) {
            if (i < code.size - 1
                && code[i][0] == "STORE"
                && code[i + 1][0] == "LOAD"
                && code[i][1] == code[i + 1][1]
            ) {
                code.removeAt(i)
                code.removeAt(i)
                i--
            }

            i++
        }

        i = 0
        while (i < code.size) {
            if (i < code.size - 2
                && code[i][0] == "LOAD"
                && code[i + 1][0] == "STORE"
                && code[i + 2][0] == "LOAD"
            ) {
                val box = code[i][1]
                val box1 = code[i + 1][1]

                code.removeAt(i)
                code.removeAt(i)

                for (j in i until code.size)
                    if (code[j][1] == box1)
                        code[j][1] = box

                i--
            }

            i++
        }

        return code.map {
            if (it[0] == "ADD" || it[0] == "MPY")
                "${it[0]}\t\t${it[1]}"
            else
                "${it[0]}\t${it[1]}"
        }.joinToString("\n")
    }
}