import java.io.File

fun main() {

    fun parseEquations(input: List<String>): List<Pair<Long, List<Int>>> =
        input.map { line ->
            val parts = line.split(":")
            val target = parts[0].trim().toLong()
            val numbers = parts[1].trim().split(" ").map { it.toInt() }
            Pair(target, numbers)
        }

    fun tryOperators(numbers: List<Int>, target: Long, currentValue: Long): Boolean {
        if (numbers.isEmpty()) return currentValue == target

        val first = numbers.first().toLong()
        val remaining = numbers.drop(1)

        return when {
            currentValue == 0L -> tryOperators(remaining, target, first)
            tryOperators(remaining, target, currentValue + first) -> true
            tryOperators(remaining, target, currentValue * first) -> true
            else -> false
        }
    }

    fun tryOperators2(numbers: List<Int>, target: Long, currentValue: Long): Boolean {
        if (numbers.isEmpty()) return currentValue == target

        val first = numbers.first().toLong()
        val remaining = numbers.drop(1)

        return when {
            currentValue == 0L -> tryOperators2(remaining, target, first)
            tryOperators2(remaining, target, currentValue + first) -> true
            tryOperators2(remaining, target, currentValue * first) -> true
            tryOperators2(remaining, target, "$currentValue$first".toLong()) -> true
            else -> false
        }
    }

    fun part1(input: List<String>): Long {
        var equations = parseEquations(input)

        return equations.sumOf { (target, numbers) ->
            if (tryOperators(numbers, target, 0)) target else 0
        }
    }

    fun part2(input: List<String>): Long {
        var equations = parseEquations(input)

        return equations.sumOf { (target, numbers) ->
            if (tryOperators2(numbers, target, 0)) target else 0
        }
    }

    // Tests
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
