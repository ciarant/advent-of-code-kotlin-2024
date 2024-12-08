data class Antenna(val frequency: Char, val x: Int, val y: Int)

class AntennaGrid(input: List<String>) {
    private val width = input[0].length
    private val height = input.size
    private val antennas: List<Antenna> = input.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char ->
            if (char != '.') Antenna(char, x, y) else null
        }
    }
    private val frequencyGroups = antennas.groupBy { it.frequency }

    fun findAntinodes(radius: Int): Set<Pair<Int, Int>> {
        return frequencyGroups.values.flatMap { group ->
            group.flatMap { a1 ->
                group.filter { it != a1 }
                    .flatMap { a2 -> findAntinodes(a1, a2, radius) }
            }
        }.toSet()
    }

    private fun findAntinodes(a1: Antenna, a2: Antenna, n: Int): Set<Pair<Int, Int>> {
        val dx = a2.x - a1.x
        val dy = a2.y - a1.y

        return setOf(
            (a1.x + n * dx) to (a1.y + n * dy),
            (a2.x - n * dx) to (a2.y - n * dy)
        ).filterTo(mutableSetOf()) { (x, y) -> isValidPosition(x, y) } // Exclude off-map antinodes
    }

    private fun isValidPosition(x: Int, y: Int): Boolean =
        x in 0 until width && y in 0 until height
}

fun main() {

    fun part1(input: List<String>): Int {
        val antennaGrid = AntennaGrid(input)

        return antennaGrid.findAntinodes(2).size
    }

    fun part2(input: List<String>): Int {
        val antennaGrid = AntennaGrid(input)

        return generateSequence(1) { it + 1 }
            .map { radius -> antennaGrid.findAntinodes(radius) }
            .takeWhile { it.isNotEmpty() }
            .flatten()
            .toSet()
            .size
    }

    // Tests
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}