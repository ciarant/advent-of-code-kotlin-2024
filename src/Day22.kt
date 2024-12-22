/**
 * [Monkey Market](https://adventofcode.com/2024/day/22)
 */
fun main() {

    fun nextSecret(initialSecret: Int): Int {
        var next = initialSecret xor (initialSecret * 64) and 0xFFFFFF
        next = next xor (next / 32) and 0xFFFFFF
        next = next xor (next * 2048) and 0xFFFFFF

        return next
    }

    fun generateSecretSequence(initialSecret: Int, n: Int): Sequence<Int> =
        generateSequence(initialSecret) { nextSecret(it) }.take(n)

    fun encode(c0: Int, c1: Int, c2: Int, c3: Int) =
        listOf(c0, c1, c2, c3)
            .map { it + 9 }
            .reduce { acc, i -> acc * 19 + i }

    fun part1(secrets: List<String>) =
        secrets.map(String::toLong).sumOf { secret ->
            generateSecretSequence(secret.toInt(), 2001).last().toLong()
        }

    fun part2(secrets: List<String>): Long {
        val sums = LongArray(19 * 19 * 19 * 19) { 0L }

        secrets.map(String::toInt).forEach { secret ->
            val prices = (0..2000).scan(secret) { current, _ -> nextSecret(current) }
                .map { it % 10 }

            val changes = prices.zipWithNext { a, b -> b - a }

            val earliestPrices = IntArray(19 * 19 * 19 * 19) { -1 }
            changes.windowed(4).forEachIndexed { index, window ->
                val patternIndex = encode(window[0], window[1], window[2], window[3])
                if (earliestPrices[patternIndex] == -1) {
                    earliestPrices[patternIndex] = prices[index + 4]
                }
            }

            earliestPrices.forEachIndexed { patternIndex, sellPrice ->
                if (sellPrice != -1) {
                    sums[patternIndex] += sellPrice.toLong()
                }
            }
        }

        return sums.max()
    }

    // Tests
    check(part1(readInput("Day22_test1")) == 37327623L)
    check(part2(readInput("Day22_test2")) == 23L)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}