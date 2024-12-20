import java.util.*
import kotlin.math.absoluteValue

/**
 * [Race Condition](https://adventofcode.com/2024/day/20)
 */
fun main() {
    data class Point(val x: Int, val y: Int)

    val directions = listOf(
        Point(0, 1),
        Point(1, 0),
        Point(0, -1),
        Point(-1, 0)
    )

    fun findShortestPath(grid: Array<CharArray>, start: Point, end: Point): List<Point> {
        val queue: Queue<Pair<Point, Int>> = LinkedList()
        val visited = Array(grid.size) { BooleanArray(grid[0].size) }
        val path = mutableListOf<Point>()

        queue.add(Pair(start, 0))
        visited[start.y][start.x] = true

        while (queue.isNotEmpty()) {
            var (current, stepCount) = queue.poll()
            if (current == end) {
                path.add(current)
                return path
            }

            for (dir in directions) {
                val nextX = current.x + dir.x
                val nextY = current.y + dir.y

                if (nextX in grid[0].indices && nextY in grid.indices && grid[nextY][nextX] != '#' && !visited[nextY][nextX]) {
                    visited[nextY][nextX] = true
                    queue.add(Pair(Point(nextX, nextY), stepCount + 1))
                    path.add(current)
                }
            }
        }

        return path
    }

    fun manhattanDistance(p1: Point, p2: Point) = (p1.x - p2.x).absoluteValue + (p1.y - p2.y).absoluteValue

    fun findShortcuts(path: List<Point>, cheatDuration: Int, minimumSavedTime: Int) =
        path.indices.flatMap { i ->
            (i + 1 until path.size).mapNotNull { j ->
                val distance = manhattanDistance(path[i], path[j])
                val savedTime = (j - i) - distance
                if (distance <= cheatDuration && savedTime >= minimumSavedTime) 1 else null
            }
        }.count()

    fun parseInput(input: List<String>): Triple<Array<CharArray>, Point, Point> {
        var start: Point? = null
        var end: Point? = null

        val grid = input.mapIndexed { row, line ->
            line.mapIndexed { col, char ->
                when (char) {
                    'S' -> start = Point(col, row)
                    'E' -> end = Point(col, row)
                }
                char
            }.toCharArray()
        }.toTypedArray()

        return Triple(grid, start!!, end!!)
    }

    fun solve(input: List<String>, cheatDuration: Int, minimumSavedTime: Int): Int {
        val (grid, start, end) = parseInput(input)
        val path = findShortestPath(grid, start, end)
        return findShortcuts(path, cheatDuration, minimumSavedTime)
    }

    // Tests
    val testInput = readInput("Day20_test")
    check(solve(testInput, 2,2) == 44) // Part 1
    check(solve(testInput, 20, 50) == 285) // Part 2

    val input = readInput("Day20")
    solve(input, 2,100).println() // Part 1
    solve(input, 20,100).println() // Part 2
}