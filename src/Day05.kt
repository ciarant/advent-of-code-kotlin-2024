import java.io.File

/**
 * [Print Queue](https://adventofcode.com/2024/day/5)
 */
fun main() {
    data class Rule(val pageBefore: Int, val pageAfter: Int)

    data class Update(val pages: List<Int>)

    fun parseFile(fileName: String): Pair<List<Rule>, List<Update>> {
        val lines = File(fileName).readLines().filter { it.isNotBlank() }
        return lines.partition { "|" in it }.let { (ruleLines, updateLines) ->
            ruleLines.map { it.split("|").let { (before, after) -> Rule(before.toInt(), after.toInt()) } } to
                    updateLines.map { Update(it.split(",").map(String::toInt)) }
        }
    }

    fun middleNumber(list: List<Int>): Int = list[list.size / 2]

    fun createDependenciesMap(rules: List<Rule>): Map<Int, Set<Int>> {
        val dependencies = mutableMapOf<Int, MutableSet<Int>>()
        rules.forEach { rule ->
            dependencies.computeIfAbsent(rule.pageAfter) { mutableSetOf() }.add(rule.pageBefore)
        }
        return dependencies
    }

    fun isValidUpdate(update: Update, dependencies: Map<Int, Set<Int>>): Boolean {
        val indexMap = update.pages.withIndex().associate { it.value to it.index }

        return dependencies.all { (page, requiredBefore) ->
            page !in indexMap || requiredBefore.none {
                it in indexMap && (indexMap[it] ?: Int.MAX_VALUE) >= (indexMap[page] ?: Int.MAX_VALUE)
            }
        }
    }

    // Order an update so that all dependencies are respected
    fun sortUpdate(update: Update, dependencies: Map<Int, Set<Int>>): Update {
        val pageSet = update.pages.toSet()

        val relevantDependencies = dependencies
            .filterKeys { it in pageSet }
            .mapValues { (_, value) -> value.filterTo(mutableSetOf()) { it in pageSet } }

        val indegree = relevantDependencies.values.flatten().groupingBy { it }.eachCount().toMutableMap()
        val queue = ArrayDeque(update.pages.filter { indegree.getOrDefault(it, 0) == 0 })

        val sortedPages = buildList {
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                add(current)
                relevantDependencies[current]?.forEach { neighbor ->
                    if (indegree.merge(neighbor, -1, Int::plus) == 0) queue.addLast(neighbor)
                }
            }
        }

        return Update(sortedPages)
    }

    fun part1(rules: List<Rule>, updates: List<Update>, dependencies: Map<Int, Set<Int>>): Int =
        updates.filter { isValidUpdate(it, dependencies) }.sumOf { middleNumber(it.pages) }

    fun part2(rules: List<Rule>, updates: List<Update>, dependencies: Map<Int, Set<Int>>): Int =
        updates
            .partition { isValidUpdate(it, dependencies) }
            .second // The invalid updates
            .map { sortUpdate(it, dependencies) }
            .sumOf { middleNumber(it.pages) }

    // Tests
    val (testRules, testUpdates) = parseFile("src/Day05_test.txt")
    val testDependencies = createDependenciesMap(testRules)
    check(part1(testRules, testUpdates, testDependencies) == 143)
    check(part2(testRules, testUpdates, testDependencies) == 123)

    val (rules, updates) = parseFile("src/Day05.txt")
    val dependencies = createDependenciesMap(rules)
    part1(rules, updates, dependencies).println()
    part2(rules, updates, dependencies).println()
}
