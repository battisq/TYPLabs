package lab1.util

import java.io.File

object FileHelper {
    private const val inputFile: String = "D:\\Projects\\Java Project\\Labs\\src\\main\\kotlin\\input.txt"
    private const val outputFile: String = "D:\\Projects\\Java Project\\Labs\\src\\main\\kotlin\\output.txt"

    fun read(): String {
        return File(inputFile).readText()
    }

    fun write(table: String, unoptimizedCode: String, optimizedCode: String) {
        val result = String.format(
            "Таблица имён:\n%s\n\n" +
                    "Неоптимизированный код:\n%s\n\n" +
                    "Оптимизированный код:\n%s",
            table,
            unoptimizedCode,
            optimizedCode
        )

        File(outputFile).writeText(result)
    }
}