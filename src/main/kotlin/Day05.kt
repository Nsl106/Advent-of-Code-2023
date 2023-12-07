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

    private fun LongRange.move(translation: Long) = LongRange(start + translation, last + translation)

    private fun LongRange.checkRange(destinationStart: Long, sourceStart: Long, length: Long): RangeOutput {
        val sourceEnd = sourceStart + length
        val movement = destinationStart - sourceStart

        // If the range is completely outside
        if (this.last < sourceStart || this.first > sourceEnd) {
            return RangeOutput(null, listOf(this))
        }

        // If the range is completely inside
        if (this.first >= sourceStart && this.last <= sourceEnd) {
            return RangeOutput(this.move(movement), listOf())
        }

        // If the range contains the source range fully
        if (this.first < sourceStart && this.last > sourceEnd) {
            val leftRange = LongRange(this.first, sourceStart)
            val movedRange = LongRange(sourceStart, sourceEnd).move(movement)
            val rightRange = LongRange(sourceEnd, this.last)
            return RangeOutput(movedRange, listOf(leftRange, rightRange))
        }

        // If the range overlaps but sticks out the front
        if (this.first < sourceStart && this.last <= sourceEnd) {
            val movedRange = LongRange(sourceStart, this.last).move(movement)
            val extraRange = LongRange(this.first, sourceStart)
            return RangeOutput(movedRange, listOf(extraRange))
        }

        // If the range overlaps but sticks out the back
        if (this.first >= sourceStart && this.last > sourceEnd) {
            val movedRange = LongRange(this.first, sourceEnd).move(movement)
            val extraRange = LongRange(sourceEnd, this.last)
            return RangeOutput(movedRange, listOf(extraRange))
        }

        throw Exception("spooky stuff happened")
    }

    private fun recurse(initial: LongRange, ranges: List<List<Long>>): List<LongRange> {
        val processedValues = mutableListOf<LongRange>()

        val range = ranges.first()
        val value = initial.checkRange(range[0], range[1], range[2])
        if (value.movedRange != null) {
            processedValues.add(value.movedRange)
        }
        value.missedRanges.forEach {
            if (ranges.size > 1) {
                processedValues.addAll(recurse(it, ranges.drop(1)))
            } else {
                processedValues.add(it)
            }
        }
        return processedValues
    }

    data class RangeOutput(val movedRange: LongRange?, val missedRanges: List<LongRange>)

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
        val seedRanges = input[0].substringAfter(": ").split(" ").chunked(2) { (start, length) -> LongRange(start.toLong(), start.toLong() + length.toLong()) }

        val locationRanges = mutableListOf<LongRange>()

        seedRanges.forEach { seedRange ->
            var currentSeedRanges = listOf(seedRange)

            getMaps().forEach { map ->
                val ranges = map.map { it.split(" ").map(String::toLong) }

                currentSeedRanges = currentSeedRanges.flatMap {
                    recurse(it, ranges)
                }
            }
            
            locationRanges.addAll(currentSeedRanges)
        }

        return locationRanges.sortedBy { it.first }[0].first
    }
}