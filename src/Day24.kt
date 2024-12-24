enum class Operation {
    AND, OR, XOR;

    fun evaluate(input1: Boolean, input2: Boolean): Boolean = when (this) {
        AND -> input1 && input2
        OR -> input1 || input2
        XOR -> input1 xor input2
    }

    companion object {
        fun fromString(op: String): Operation {
            return when (op.uppercase()) {
                "AND" -> AND
                "OR" -> OR
                "XOR" -> XOR
                else -> throw IllegalArgumentException("Unknown operation: $op")
            }
        }
    }
}

/**
 * [Crossed Wires](https://adventofcode.com/2024/day/24)
 */
fun main() {
    data class Wire(val name: String, var value: Boolean? = null)
    data class Gate(val input1: Wire, val input2: Wire, val output: Wire, val operation: Operation) {

        fun evaluate() {
            output.value = operation.evaluate(input1.value!!, input2.value!!)
        }
    }

    class Circuit {
        val wires = mutableMapOf<String, Wire>()
        val gates = mutableListOf<Gate>()

        fun getOrCreateWire(name: String, value: Boolean? = null): Wire =
            wires.getOrPut(name) { Wire(name, value) }.apply { this.value = this.value ?: value }

        fun addGate(input1Name: String, input2Name: String, outputName: String, operationName: String) {
            val input1 = getOrCreateWire(input1Name)
            val input2 = getOrCreateWire(input2Name)
            val output = getOrCreateWire(outputName)
            val operation = Operation.fromString(operationName)
            gates.add(Gate(input1, input2, output, operation))
        }

        fun getRegister(name: Char): Long {
            val str = wires.filterKeys { it.startsWith(name) }
                .toSortedMap()
                .values.joinToString("") { if (it.value == true) "1" else "0" }
                .reversed()

            return str.toLong(2)
        }

        fun evaluate(): Long {
            val queue = ArrayDeque<Gate>()

            gates.filter { it.input1.value != null && it.input2.value != null }
                .forEach { queue.add(it) }

            while (queue.isNotEmpty()) {
                val currentGate = queue.removeFirst()
                currentGate.evaluate()

                gates.filter { it.input1 == currentGate.output || it.input2 == currentGate.output }
                    .filter { it.input1.value != null && it.input2.value != null }
                    .forEach { queue.add(it) }
            }

            return getRegister('z')
        }
    }

    fun parseInput(input: List<String>): Circuit {
        val circuit = Circuit()

        input.forEach { line ->
            when {
                line.contains(":") -> {
                    val (name, value) = line.split(":").map(String::trim)
                    circuit.getOrCreateWire(name, value == "1")
                }

                "->" in line -> {
                    val (input1, operation, input2, _, output) = line.split(Regex("\\s+"))
                    circuit.addGate(input1, input2, output, operation)
                }
            }
        }

        return circuit
    }

    fun part1(input: List<String>): Long {
        val circuit = parseInput(input)
        return circuit.evaluate()
    }

    // Tests
    val testInput1 = readInput("Day24_test1")
    check(part1(testInput1) == 2024L)

    val input = readInput("Day24")
    part1(input).println()
}
