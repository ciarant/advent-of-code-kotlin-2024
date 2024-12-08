const val GUARD_UP = '^'

sealed class Result {
    data object Success : Result()
    data object OutOfBounds : Result()
    data object StuckInLoop : Result()
}

fun main() {

    class Grid(private val input: List<String>) {
        private val grid: Array<CharArray> = input.map { it.toCharArray() }.toTypedArray()
        private val directions = listOf(
            Pair(-1, 0),  // up
            Pair(0, 1),   // right
            Pair(1, 0),   // down
            Pair(0, -1)   // left
        )
        val height = grid.size
        val width = grid[0].size
        private var directionIndex = 0
        private val startPosition = findGuardLocation()
        private var position = startPosition
        private val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
        private val path: MutableSet<Pair<Pair<Int, Int>, Int>> = mutableSetOf()

        private fun findGuardLocation(): Pair<Int, Int> =
            grid.withIndex()
                .first { (_, row) -> GUARD_UP in row }
                .let { (rowIndex, row) -> rowIndex to row.indexOf(GUARD_UP) }

        private fun isObstacle(location: Pair<Int, Int>) = grid[location.first][location.second] == '#'

        private fun turnRight() {
            directionIndex = (directionIndex + 1) % directions.size
        }

        fun reset() {
            position = startPosition
            directionIndex = 0
            visited.clear()
            path.clear()
        }

        fun removeGuard() {
            grid[startPosition.first][startPosition.second] = '.'
        }

        fun placeObstacle(location: Pair<Int, Int>): Boolean {
            val (row,col) = location
            return if (location != startPosition && grid[row][col] == '.') {
                grid[row][col] = '#'
                true
            } else {
                false
            }
        }

        fun removeObstacle(location: Pair<Int, Int>): Boolean {
            val (row,col) = location
            return if (grid[row][col] == '#') {
                grid[row][col] = '.'
                true
            } else {
                false
            }
        }

        fun stepCount(): Int = visited.size

        // Return false if movement will cause guard to exit grid or of the guard is tracing
        // their path (i.e. landing on a block while moving in the same direction as they
        // were on a previous visit to that block)
        fun guardStep(): Result {
            val (dr, dc) = directions[directionIndex]
            val nextLocation = position.first + dr to position.second + dc

            if (nextLocation.first !in 0 until height || nextLocation.second !in 0 until width) return Result.OutOfBounds

            return when {
                isObstacle(nextLocation) -> {
                    turnRight()
                    Result.Success
                }
                !path.add(position to directionIndex) -> Result.StuckInLoop
                else -> {
                    position = nextLocation
                    visited += position
                    Result.Success
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)

        while (true) {
            when(grid.guardStep()) {
                Result.OutOfBounds -> break
                else -> continue
            }
        }

        return grid.stepCount()
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        var loopCount = 0

        (0 until grid.height).forEach { row ->
            (0 until grid.width).forEach { col ->
                grid.reset()
                grid.removeGuard()

                if(grid.placeObstacle(row to col)) {
                    while(true) {
                        when (grid.guardStep()) {
                            Result.StuckInLoop -> {
                                loopCount++
                                break
                            }
                            Result.OutOfBounds -> break
                            Result.Success -> continue
                        }
                    }
                    grid.removeObstacle(row to col)
                }
            }
        }

        return loopCount
    }

    // Tests
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
