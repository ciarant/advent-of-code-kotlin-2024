import kotlin.math.absoluteValue

/**
 * [Historian Hysteria](https://adventofcode.com/2024/day/1)
 */
fun main() {

    fun splitLists(input: List<String>): Pair<List<Int>, List<Int>> =
        input.mapNotNull { line ->
            line.split(' ').filter { it.isNotEmpty() }.takeIf { it.size == 2 }
                ?.let { it[0].toInt() to it[1].toInt() }
        }.unzip()

    fun part1(input: List<String>): Int {
        val (leftList, rightList) = splitLists(input)

        val sortedLeftList = leftList.sorted()
        val sortedRightList = rightList.sorted()

        val totalDistance = sortedLeftList.zip(sortedRightList)
            .sumOf { (left, right) -> kotlin.math.abs(left - right) }

        return totalDistance;
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = splitLists(input)

        // Count occurrences of each number in the right list
        val rightCounts = rightList.groupingBy { it }.eachCount()

        // Calculate the similarity score
        return leftList.sumOf { it * (rightCounts[it] ?: 0) }
    }

    // Tests
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
