object Day08: BaseDay(8) {
    private val directions = input.first()

    private val nodes = input.drop(2).associate {
        val (id, left, right) = it.split("""\W+""".toRegex())
        id to (left to right)
    }

    private fun getStepsBetween(start: String, atEnd: (String) -> Boolean): Long {
        var tmpDirections = directions
        var currentID = start
        var count = 0L
        while (!atEnd(currentID)) {
            val (left, right) = nodes[currentID]!!
            currentID = if (tmpDirections.first() == 'L') left else right

            tmpDirections = tmpDirections.drop(1)
            if (tmpDirections.isEmpty()) tmpDirections = directions

            count++
        }

        return count
    }

    override fun partOne() = getStepsBetween(start = "AAA", atEnd = { it == "ZZZ" })

    override fun partTwo(): Long {
        val startingNodes = nodes.filter { it.key.endsWith('A') }

        val cycleLengths = startingNodes.map { node ->
            getStepsBetween(start = node.key, atEnd = { it.endsWith('Z') })
        }

        return cycleLengths.reduce(::lcm)
    }
}
