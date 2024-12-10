data class Position(val x: Int, val y: Int) {
    companion object {
        val directions = listOf(
            Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)
        )
    }
    fun neighbors(): List<Position> =
        directions.map { (dx, dy) -> Position(x + dx, y + dy) }

    fun isInBounds(rows: Int, cols: Int): Boolean =
        x in 0 until rows && y in 0 until cols
}

/**
 * [Hoof It](https://adventofcode.com/2024/day/10)
 */
fun main() {

    fun scoreTrails(start: Position, grid: List<List<Int>>): Int {
        val rows = grid.size
        val cols = grid[0].size
        val visited = mutableSetOf<Position>()
        val reachableNines = mutableSetOf<Position>()

        val queue = ArrayDeque<Position>().apply { add(start) }
        visited.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            current.neighbors()
                .filter { it.isInBounds(rows, cols) && it !in visited }
                .filter { grid[it.x][it.y] - grid[current.x][current.y] == 1 }
                .forEach {
                    visited.add(it)
                    queue.add(it)
                    if (grid[it.x][it.y] == 9) reachableNines.add(it)
                }
        }

        return reachableNines.size
    }

    fun countDistinctTrails(
        current: Position,
        grid: List<List<Int>>,
        path: MutableSet<Position>
    ): Int {
        val rows = grid.size
        val cols = grid[0].size

        if (grid[current.x][current.y] == 9) return 1

        var trails = 0

        current.neighbors()
            .filter { it.isInBounds(rows, cols) }
            .filter { it !in path } // Don't revisit positions in the current path
            .filter { grid[it.x][it.y] - grid[current.x][current.y] == 1 } // Uphill
            .forEach {
                path.add(it) // Add to path before exploring
                trails += countDistinctTrails(it, grid, path)
                path.remove(it) // Remove after backtracking
            }

        return trails
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { row -> row.map { it.digitToInt() } }

        return grid.indices.flatMap { x ->
            grid[0].indices.mapNotNull { y ->
                if (grid[x][y] == 0) scoreTrails(Position(x, y), grid) else null
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { row -> row.map { it.digitToInt() } }

        return grid.indices.flatMap { x ->
            grid[0].indices.mapNotNull { y ->
                if (grid[x][y] == 0) {
                    val path = mutableSetOf(Position(x, y))
                    countDistinctTrails(Position(x, y), grid, path)
                } else null
            }
        }.sum()
    }

    // Tests
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}