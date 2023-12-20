object Day19: BaseDay(19) {
    data class Item(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun getByType(type: ValueType) = when (type) {
            ValueType.X -> x
            ValueType.M -> m
            ValueType.A -> a
            ValueType.S -> s
        }
    }

    data class Rule(val valueType: ValueType, val next: String, val value: Int, val operatorIsGreater: Boolean)
    data class Workflow(val rules: List<Rule>, val fallback: String)

    private val typeMap = ValueType.entries.associateBy { it.name.lowercase()[0] }

    enum class ValueType {
        X, M, A, S
    }

    private val items: List<Item>
    private val workflows: MutableMap<String, Workflow>

    init {
        val (unparsedWorkflows, unparsedItems) = input.split { it.isEmpty() }
        items = unparsedItems.map { item -> item.split(",").map(String::getFirstInt).let { Item(it[0], it[1], it[2], it[3]) } }
        workflows = mutableMapOf()

        unparsedWorkflows.forEach { ln ->
            val workflowName = ln.substringBefore('{')

            val all = ln.substringAfter('{').substringBefore('}').split(',')
            val rules = all.dropLast(1).map { step ->
                val type = typeMap.getValue(step.first(Char::isLetter))
                val next = step.substringAfter(":")
                val value = step.getFirstInt()
                val operatorIsGreater = step.contains('>')
                Rule(type, next, value, operatorIsGreater)
            }
            val fallback = all.last()
            workflows[workflowName] = Workflow(rules, fallback)
        }
    }

    override fun partOne(): Any {
        val acceptedParts = mutableListOf<Item>()

        for (item in items) {
            var currentWorkflow = "in"
            while (currentWorkflow != "A" && currentWorkflow != "R") {
                val workflow = workflows.getValue(currentWorkflow)
                val steps = workflow.rules

                // Find the first workflow that matches the value, and using the fallback if none match
                currentWorkflow = steps.firstOrNull {
                    val value = item.getByType(it.valueType)
                    if (it.operatorIsGreater) value > it.value else value < it.value
                }?.next ?: workflow.fallback
            }
            if (currentWorkflow == "A") acceptedParts.add(item)
        }

        return acceptedParts.sumOf { it.x + it.m + it.a + it.s }
    }

    override fun partTwo(): Any {
        val ranges = ValueType.entries.associateWith { 1..4000L }.toMutableMap()
        return checkRule(ranges, "in")
    }

    private fun checkRule(ranges: Map<ValueType, LongRange>, name: String): Long {
        if (name == "A") {
            return ranges.values.fold(1L) { acc, range -> acc * if (range.isEmpty()) 1 else (range.last - range.first) + 1 }
        } else if (name == "R") {
            return 0
        }

        var total = 0L

        val workflow = workflows.getValue(name)
        val rules = workflow.rules
        val newRanges = ranges.toMutableMap()
        for (rule in rules) {
            val selectedRange = newRanges.getValue(rule.valueType)
            val goodRange = if (rule.operatorIsGreater) rule.value + 1..selectedRange.last else selectedRange.first..rule.value - 1
            val badRange = if (rule.operatorIsGreater) selectedRange.first..rule.value else rule.value..selectedRange.last

            if (!goodRange.isEmpty()) {
                val nextRanges = newRanges.toMutableMap()
                nextRanges[rule.valueType] = goodRange
                total += checkRule(nextRanges, rule.next)
            }
            if (!badRange.isEmpty()) newRanges[rule.valueType] = badRange
        }

        total += checkRule(newRanges, workflow.fallback)
        return total
    }
}