package lab1.tree

/**
 * Если oper – это знак операции, имеем узел дерева.
 * Если это операнд, имеем лист, в этом случае указатели left и right являются нулевыми.
 */
data class TokenTree(
    //знак операции или операнд – переменная либо константа
    var oper: String,
    var level: Int = 0,
    var code: String = "",
    var left: TokenTree?,
    var right: TokenTree?
) {
    companion object {
        fun printTree(tree: TokenTree?, level: Int = 0) {
            if (tree != null) {
                printTree(tree.right, level + 1)

                for (i in 0..level)
                    print("    ")
                println(tree.oper)

                printTree(tree.left, level + 1)
            }
        }
    }
}