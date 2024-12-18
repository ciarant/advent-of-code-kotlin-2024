import java.util.*

/**
 * [RAM Run](https://adventofcode.com/2024/day/18)
 */
fun main() {
    data class Point(val x: Int, val y: Int)

    val directions = listOf(
        Point(0, 1),
        Point(1, 0),
        Point(0, -1),
        Point(-1, 0)
    )

    fun findShortestPath(grid: Array<BooleanArray>, start: Point, end: Point): Int {
        val queue: Queue<Pair<Point, Int>> = LinkedList()
        val visited = Array(grid.size) { BooleanArray(grid[0].size) }

        queue.add(Pair(start, 0))
        visited[start.y][start.x] = true

        while (queue.isNotEmpty()) {
            val (current, stepCount) = queue.poll()
            if (current == end) return stepCount

            for (dir in directions) {
                val nextX = current.x + dir.x
                val nextY = current.y + dir.y

                if (nextX in grid[0].indices && nextY in grid.indices && !grid[nextY][nextX] && !visited[nextY][nextX]) {
                    visited[nextY][nextX] = true
                    queue.add(Pair(Point(nextX, nextY), stepCount + 1))
                }
            }
        }

        return -1
    }

    fun generateGrid(size: Int, corruptPoints: List<Point> = emptyList()): Array<BooleanArray> =
        Array(size) { BooleanArray(size) }.apply {
            corruptPoints.forEach { (x, y) -> this[y][x] = true }
        }

    fun parseInput(input: List<String>) = input.map { line ->
        line.split(",").map(String::trim).let { (x, y) -> Point(x.toInt(), y.toInt()) }
    }

    fun part1(input: List<String>, gridSize: Int, corruptCount: Int): Int {
        val points = parseInput(input).take(corruptCount)
        val grid = generateGrid(gridSize, points)

        return findShortestPath(grid, Point(0, 0), Point(gridSize - 1, gridSize - 1))
    }

    fun part2(input: List<String>, gridSize: Int): String {
        val points = parseInput(input)
        val grid = generateGrid(gridSize)
        val start = Point(0, 0)
        val end = Point(gridSize - 1, gridSize - 1)

        points.forEach { point ->
            grid[point.y][point.x] = true
            if (findShortestPath(grid, start, end) == -1) return "${point.x},${point.y}"
        }
        return "No solution found"
    }

    // Tests
    val testInput = readInput("Day18_test")
    check(part1(testInput, 7, 12) == 22)
    check(part2(testInput, 7) == "6,1")

    val input = readInput("Day18")
    part1(input, 71, 1024).println()
    part2(input, 71).println()
}