import java.io.File

fun main() {

    fun parseEquations(input: List<String>): List<Pair<Long, List<Int>>> =
        input.map { line ->
            val (target, numbers) = line.split(":").map(String::trim)
            target.toLong() to numbers.split(" ").map(String::toInt)
        }

    fun tryOperators(
        numbers: List<Int>,
        target: Long,
        currentValue: Long = 0,
        customOp: ((Long, Long) -> Long)? = null
    ): Boolean {
        if (numbers.isEmpty()) return currentValue == target

        val first = numbers.first().toLong()
        val remaining = numbers.drop(1)

        val newValue = customOp?.invoke(currentValue, first) ?: "$currentValue$first".toLong()

        return when {
            currentValue == 0L -> tryOperators(remaining, target, first, customOp)
            tryOperators(remaining, target, currentValue + first, customOp) -> true
            tryOperators(remaining, target, currentValue * first, customOp) -> true
            customOp != null && tryOperators(remaining, target, customOp(currentValue, first), customOp) -> true
            else -> false
        }
    }

    fun part1(input: List<String>): Long =
        parseEquations(input).sumOf { (target, numbers) ->
            if (tryOperators(numbers, target)) target else 0
        }

    fun part2(input: List<String>): Long =
        parseEquations(input).sumOf { (target, numbers) ->
            if (tryOperators(numbers, target) { a, b -> "$a$b".toLong() }) target else 0
        }

    // Tests
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
