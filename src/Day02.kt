import kotlin.math.absoluteValue

/**
 * [Red-Nosed Reports](https://adventofcode.com/2024/day/2)
 */
fun main() {

    fun extractReports(input: List<String>) = input.map { line ->
        line.split(" ").map { it.toInt() }
    }

    fun isReportSafe(report: List<Int>): Boolean =
        report.zipWithNext { a, b -> b - a }
            .let { differences ->
                differences.all { it in 1..3 } || differences.all { it in -3..-1 }
            }

    fun isReportSafeWithDampener(report: List<Int>): Boolean =
        isReportSafe(report) || report.indices.any { index ->
            isReportSafe(report.filterIndexed { i, _ -> i != index })
        }

    fun part1(input: List<String>): Int {
        val reports = extractReports(input)

        return reports.count(::isReportSafe)
    }

    fun part2(input: List<String>): Int {
        val reports = extractReports(input)

        return reports.count(::isReportSafeWithDampener)
    }

    // Tests
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
