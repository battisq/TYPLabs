package lab1.util

val operation: List<Char> = listOf('+', '*')
val logarithmicSigns = listOf('+', '-')
val logarithmicSymbol = listOf('e', 'E')
val whitespace: List<Char> = listOf(' ')
val openingBracket: List<Char> = listOf('(')
val closingBracket: List<Char> = listOf(')')
val point: List<Char> = listOf('.')
val equal: List<Char> = listOf('=')
val end: List<Char> = listOf('Ɵ')
val digits: List<Char> = mutableListOf<Char>().apply { this.addAll('0'..'9') }
val letters = mutableListOf('_').apply {
    this.addAll('a'..'z')
    this.addAll('A'..'Z')
}
