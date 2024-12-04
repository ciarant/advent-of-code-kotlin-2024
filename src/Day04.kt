fun main() {

    fun part1(input: List<String>): Int {
        val directions = listOf(
            0 to 1,    // Right
            1 to 0,    // Down
            0 to -1,   // Left
            -1 to 0,   // Up
            1 to 1,    // Down-right
            1 to -1,   // Down-left
            -1 to 1,   // Up-right
            -1 to -1   // Up-left
        )

        val search = "XMAS"

        return input.indices.sumOf { row ->
            input[0].indices.sumOf { col ->
                // From each point on the grid, try to find the search string in each direction
                directions.count { (dx, dy) ->
                    search.indices.all { i ->
                        val nx = row + i * dx
                        val ny = col + i * dy
                        nx in input.indices &&
                                ny in input[nx].indices &&
                                input[nx][ny] == search[i]
                    }
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        // Order in each String: Northwest, Northeast, Southwest, Southeast
        val patterns = listOf("SSMM", "SMMS", "MMSS", "MSSM")

        return (1 until input.size - 1).sumOf { row ->
            (1 until input[0].length - 1).count { col ->
                // For each occurrence of 'A', check if its diagonal neighbours match a desired pattern
                input[row][col] == 'A' &&
                        listOf(
                            input[row - 1][col - 1],
                            input[row - 1][col + 1],
                            input[row + 1][col + 1],
                            input[row + 1][col - 1]
                        ).joinToString("") in patterns
            }
        }
    }

    // Tests
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
