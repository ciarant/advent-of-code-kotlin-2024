import java.io.File

/**
 * [Code Chronicle](https://adventofcode.com/2024/day/25)
 */
fun main() {
    fun parseInput(blocks: List<String>): Pair<List<IntArray>, List<IntArray>> {
        val locks = mutableListOf<IntArray>()
        val keys = mutableListOf<IntArray>()

        blocks.forEach { block ->
            val lines = block.lines()
            val isLock = lines[0][0] == '#'
            val relevantLines = if (isLock) lines.drop(1) else lines.dropLast(1)

            val heights = (0 until lines[0].length).map { x ->
                relevantLines.count { it[x] == '#' }
            }.toIntArray()

            if (isLock) locks.add(heights) else keys.add(heights)
        }

        return locks to keys
    }

    fun part1(input: List<String>): Int {
        val (locks, keys) = parseInput(input)

        return locks.sumOf { lock ->
            keys.count { key ->
                lock.zip(key).all { (l, k) -> l + k <= 5 }
            }
        }
    }

    // Tests
    val testInput = File("src/Day25_test.txt").readText().trim().split("\n\n")
    check(part1(testInput) == 3)

    val input = File("src/Day25.txt").readText().trim().split("\n\n")
    part1(input).println()
}