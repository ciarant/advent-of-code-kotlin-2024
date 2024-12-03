import java.io.File

fun main() {

    fun part1(input: String): Int =
        """mul\((\d+),(\d+)\)""".toRegex().findAll(input)
            .map { match ->
                val (first, second) = match.destructured
                first.toInt() * second.toInt()
            }
            .sum()

    fun part2(input: String): Int =
        """(do\(\)|don't\(\)|mul\((\d+),(\d+)\))""".toRegex().findAll(input)
            .fold(true to 0) { (enabled, sum), match ->
                when {
                    match.value == "do()" -> true to sum
                    match.value == "don't()" -> false to sum
                    enabled -> enabled to sum + match.groupValues[2].toInt() * match.groupValues[3].toInt()
                    else -> enabled to sum
                }
            }.second

    // Tests
    val testInput1 = File("src/Day03_test1.txt").readText()
    check(part1(testInput1) == 161)
    val testInput2 = File("src/Day03_test2.txt").readText()
    check(part2(testInput2) == 48)

    val input = File("src/Day03.txt").readText()
    part1(input).println()
    part2(input).println()
}
