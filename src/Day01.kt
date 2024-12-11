import kotlin.math.absoluteValue

/**
 * [Historian Hysteria](https://adventofcode.com/2024/day/1)
 */
fun main() {

    fun splitLists(input: List<String>): Pair<List<Int>, List<Int>> =
        input.map { line ->
            line.split("   ").let { (left, right) -> left.toInt() to right.toInt() }
        }.unzip()

    fun part1(input: List<String>): Int {
        val (leftList, rightList) = splitLists(input)

        val totalDistance = leftList.sorted().zip(rightList.sorted())
            .sumOf { (left, right) -> kotlin.math.abs(left - right) }

        return totalDistance;
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = splitLists(input)

        return leftList.sumOf { left ->
            left * rightList.count { it == left }
        }
    }

    // Tests
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
