/**
 * [LAN Party](https://adventofcode.com/2024/day/23)
 */
fun main() {

    fun parseInput(input: List<String>) = input.map { it.split("-") }
        .fold(mutableMapOf<String, MutableSet<String>>()) { adjacencyMap, (from, to) ->
            adjacencyMap.getOrPut(from) { mutableSetOf() }.add(to)
            adjacencyMap.getOrPut(to) { mutableSetOf() }.add(from)
            adjacencyMap
        }

    fun bronKerbosch(
        clique: Set<String>, candidates: Set<String>, excluded: Set<String>,
        adjacency: Map<String, Set<String>>, bestClique: MutableSet<String>
    ) {
        if (clique.size + candidates.size <= bestClique.size) return

        if (candidates.isEmpty() && excluded.isEmpty()) {
            if (clique.size > bestClique.size) {
                bestClique.clear()
                bestClique.addAll(clique)
            }
            return
        }

        val pivot = (candidates + excluded).firstOrNull() ?: return
        val pivotNeighbours = adjacency[pivot] ?: return

        candidates.subtract(pivotNeighbours).forEach { node ->
            adjacency[node]?.let { neighbours ->
                bronKerbosch(
                    clique + node,
                    candidates.intersect(neighbours),
                    excluded.intersect(neighbours),
                    adjacency,
                    bestClique
                )
            }
        }
    }

    fun part1(input: List<String>): Int {
        val adjacency = parseInput(input)

        return adjacency.asSequence()
            .flatMap { (u, neighbours) ->
                neighbours.asSequence().flatMap { v ->
                    adjacency[v]?.intersect(neighbours)?.map { w ->
                        listOf(u, v, w).sorted()
                    }?.asSequence() ?: emptySequence()
                }
            }
            .filter { it.any { node -> node.startsWith('t') } }
            .distinct()
            .count()
    }

    fun part2(input: List<String>): String {
        val adjacency = parseInput(input)
        val bestClique = mutableSetOf<String>()

        bronKerbosch(emptySet(), adjacency.keys, emptySet(), adjacency, bestClique)

        return bestClique.sorted().joinToString(",")
    }

    // Tests
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}