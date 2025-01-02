/**
 * [Red-Nosed Reports](https://adventofcode.com/2024/day/2)
 */
fun main() {

    fun extractReports(input: List<String>) = input.map { line ->
        line.split(" ").map(String::toInt)
    }

    fun isReportSafe(report: List<Int>) =
        report.zipWithNext { a, b -> b - a }
            .let { differences ->
                differences.all { it in 1..3 } || differences.all { it in -3..-1 }
            }

    fun isReportSafeWithDampener(report: List<Int>) =
        isReportSafe(report) || report.indices.any { index ->
            isReportSafe(report.filterIndexed { i, _ -> i != index })
        }

    fun part1(input: List<String>) = extractReports(input).count(::isReportSafe)

    fun part2(input: List<String>) = extractReports(input).count(::isReportSafeWithDampener)

    // Tests
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
