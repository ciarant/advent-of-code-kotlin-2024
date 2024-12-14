const val GRID_WIDTH = 101
const val GRID_HEIGHT = 103
const val TEST_GRID_WIDTH = 11
const val TEST_GRID_HEIGHT = 7

/**
 * [Restroom Redoubt](https://adventofcode.com/2024/day/14)
 */
fun main() {
    data class Robot(val initialX: Int, val initialY: Int, val velocityX: Int, val velocityY: Int) {

        fun positionAfter(seconds: Int, width: Int, height: Int): Pair<Int, Int> {
            var x = (initialX + velocityX * seconds) % width
            var y = (initialY + velocityY * seconds) % height

            if (x < 0) x += width
            if (y < 0) y += height

            return x to y
        }
    }

    fun calculateSafetyFactor(robots: List<Robot>, seconds: Int, width: Int, height: Int): Int {
        val grid = Array(width) { IntArray(height) { 0 } }
        val newPositions = robots.map { it.positionAfter(seconds, width, height) }
        newPositions.forEach { (x, y) ->
            grid[x][y]++
        }

        val quadrantCounts = IntArray(4) { 0 }

        for (x in grid.indices) {
            for (y in grid[x].indices) {
                if (grid[x][y] > 0) {
                    val quadrant = when {
                        x < width / 2 && y < height / 2 -> 0
                        x >= width / 2 && y < height / 2 -> 1
                        x < width / 2 && y >= height / 2 -> 2
                        else -> 3
                    }

                    // Don't count robots exactly in the middle of the grid
                    if (x != width / 2 && y != height / 2) {
                        quadrantCounts[quadrant] += grid[x][y]
                    }
                }
            }
        }

        return quadrantCounts.reduce { acc, count -> acc * count }
    }

    fun parseRobots(input: List<String>): List<Robot> = input.map { line ->
        val (pos, vel) = line.split(" ").map { it.split("=")[1] }
        val (x, y) = pos.split(",").map(String::toInt)
        val (vx, vy) = vel.split(",").map(String::toInt)
        Robot(x, y, vx, vy)
    }

    fun part1(robots: List<Robot>, width: Int, height: Int) =
        calculateSafetyFactor(robots, 100, width, height) // 219150360

    fun part2(robots: List<Robot>, width: Int, height: Int) =
        MutableList(10000) {
            calculateSafetyFactor(
                robots,
                it,
                GRID_WIDTH,
                GRID_HEIGHT
            ) to it
        }.minBy { it.first }.second

    // Tests
    val testInput = readInput("Day14_test")
    val testRobots = parseRobots(testInput)
    check(part1(testRobots, TEST_GRID_WIDTH, TEST_GRID_HEIGHT) == 12)

    val input = readInput("Day14")
    val robots = parseRobots(input)
    part1(robots, GRID_WIDTH, GRID_HEIGHT).println()
    part2(robots, GRID_WIDTH, GRID_HEIGHT).println() // 6911, 8053
}
