/**
 * [Plutonian Pebbles](https://adventofcode.com/2024/day/11)
 */
fun main() {

    fun parseInput(input: List<String>): List<Long> =
        input[0].split(" ".toRegex()).map(String::toLong)

    fun blink(stone: Long): List<Long> {
        val stoneStr = stone.toString()
        return when {
            stone == 0L -> listOf(1L)
            stoneStr.length % 2 == 0 -> {
                val splitLoc = stoneStr.length / 2
                listOf(
                    stoneStr.substring(0, splitLoc).toLong(),
                    stoneStr.substring(splitLoc).toLong()
                )
            }

            else -> listOf(stone * 2024)
        }
    }

    fun solve(stones: List<Long>, blinks: Int): Long {
        var count = stones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

        repeat(blinks) {
            count = count.flatMap { (stone, freq) ->
                blink(stone).map { it to freq }
            }.groupingBy { it.first }.fold(0L) { acc, (_, freq) -> acc + freq }
        }

        return count.values.sum()
    }

    // Tests
    val testInput = readInput("Day11_test")
    var stones = parseInput(testInput)
    check(solve(stones, 25) == 55312L)

    val input = readInput("Day11")
    stones = parseInput(input)
    solve(stones, 25).println()
    solve(stones, 75).println()
}