data object Day20: Day() {
    enum class SignalType { HIGH, LOW }

    val operationQueue = mutableListOf<Pair<() -> SignalType, Module>>()

    private val modules: Map<String, Module>

    init {
        val input = input.map { Triple(it.first(), it.firstWord(), it.substringAfter("-> ").split(", ")) }

        modules = input.associate { (type, name, outputs) ->
            val module = when (type) {
                '%' -> FlipFlop(name, outputs)
                '&' -> {
                    // Get all modules that use this as an output
                    val inputs = input.filter { (_, _, outputs) -> outputs.contains(name) }.map { (_, name, _) -> name }
                    Conjunction(name, outputs, inputs)
                }

                else -> Broadcaster(name, outputs)
            }
            name to module
        }
    }

    override fun partOne(): Any {
        var highCount = 0L
        var lowCount = 0L

        for (i in 1..1000) {
            Button.press()
            while (operationQueue.isNotEmpty()) {
                val (operation, _) = operationQueue.pop()
                val signal = operation.invoke()
                if (signal == SignalType.HIGH) highCount++ else lowCount++
            }
        }

        return lowCount * highCount
    }

    override fun partTwo(): Any {
        // Get the module that outputs to rx
        val (_, rxController) = modules.toList().first { (_, module) -> module.outputsTo.contains("rx") }
        // and get what it needs to activate
        val dependencies = (rxController as Conjunction).inputs

        // Collect the iteration that each one sends a high signal on and try to find a pattern
        val intervalMap = dependencies.associateWith { mutableListOf<Int>() }

        var i = 1
        // Loop until each module has been recorded at least three times (two to compare, and ignoring the first one)
        while (intervalMap.values.all { it.size <= 3 }) {
            Button.press()
            while (operationQueue.isNotEmpty()) {
                val (operation, module) = operationQueue.pop()
                val signalType = operation.invoke()
                // If one of the rx dependencies is sending a high signal, record it
                if (dependencies.contains(module.name) && signalType == SignalType.HIGH) {
                    intervalMap.getValue(module.name).add(i)
                }
            }
            i++
        }

        // Ignore the first value of each, then measure the difference between the next two and find the lowest common multiplier
        val intervals = intervalMap.map { it.value.drop(1).let { (first, second) -> second - first }.toLong() }
        return intervals.reduce(::lcm)
    }

    sealed class Module(open val name: String, open val outputsTo: List<String>) {
        abstract fun accept(signal: SignalType, from: String)

        fun sendHigh() = sendSignal(SignalType.HIGH)
        fun sendLow() = sendSignal(SignalType.LOW)

        private fun sendSignal(type: SignalType) {
            outputsTo.forEach {
                operationQueue.add({ modules[it]?.accept(type, name); type } to this)
            }
        }
    }

    data class FlipFlop(override val name: String, override val outputsTo: List<String>): Module(name, outputsTo) {
        private var isOn = false
        override fun accept(signal: SignalType, from: String) {
            if (signal == SignalType.LOW) {
                isOn = !isOn
                if (isOn) sendHigh() else sendLow()
            }
        }
    }

    data class Conjunction(override val name: String, override val outputsTo: List<String>, val inputs: List<String>): Module(name, outputsTo) {
        private val inputMap = inputs.associateWith { SignalType.LOW }.toMutableMap()
        override fun accept(signal: SignalType, from: String) {
            inputMap[from] = signal
            if (inputMap.values.all { it == SignalType.HIGH }) sendLow() else sendHigh()
        }
    }

    data class Broadcaster(override val name: String, override val outputsTo: List<String>): Module(name, outputsTo) {
        override fun accept(signal: SignalType, from: String) = if (signal == SignalType.HIGH) sendHigh() else sendLow()
    }

    data object Button: Module("button", listOf("broadcaster")) {
        override fun accept(signal: SignalType, from: String) = throw UnsupportedOperationException()
        fun press() = sendLow()
    }
}