object Day05: BaseDay<Long>(5) {
    private fun getMaps(): MutableList<List<String>> {
        val maps = mutableListOf<List<String>>()

        var tmpInput = input
        while (true) {
            tmpInput = tmpInput.dropWhile { !it.contains("map:") }.drop(1)
            if (tmpInput.isEmpty()) break
            maps.add(tmpInput.takeWhile { it.isNotBlank() })
        }

        return maps
    }

    private fun Long.getMappedValue(range: String): Long? {
        val values = range.split(" ").map { i -> i.toLong() }
        val sourceRange = LongRange(values[1], values[1] + values[2] - 1)
        if (!sourceRange.contains(this)) return null
        return values[0] + (this - values[1])
    }

    override fun partOne(): Long {
        val seeds = input[0].substringAfter(": ").split(" ").map { it.toLong() }
        var lowestLocation = Long.MAX_VALUE

        seeds.forEach { seed ->
            var currentID = seed

            getMaps().forEach { map ->
                for (range in map) {
                    val value = currentID.getMappedValue(range)
                    if (value != null) {
                        currentID = value
                        break
                    }
                }
            }

            lowestLocation = minOf(lowestLocation, currentID)
        }
        return lowestLocation
    }

    override fun partTwo(): Long {
        return 0L // :(
    }
}