typealias MapRange = List<Long>

object Day05: BaseDay(5) {
    private val maps by lazy {
        val maps = mutableListOf<List<String>>()

        var tmpInput = input
        while (true) {
            tmpInput = tmpInput.dropWhile { !it.contains("map:") }.drop(1)
            if (tmpInput.isEmpty()) break
            maps.add(tmpInput.takeWhile { it.isNotBlank() })
        }

        maps
    }

    // Part one specific

    private fun Long.getValueFromMap(mapRange: MapRange): Long? {
        val (destinationStart, sourceStart, length) = mapRange

        val sourceRange = LongRange(sourceStart, sourceStart + length - 1)
        if (!sourceRange.contains(this)) return null
        return destinationStart + (this - sourceStart)
    }

    override fun partOne(): Long {
        val seeds = input[0].substringAfter(": ").split(" ").map(String::toLong)

        var lowestLocation = Long.MAX_VALUE

        seeds.forEach { seed ->
            var currentID = seed

            maps.forEach { map ->
                for (range in map) {
                    val value = currentID.getValueFromMap(range.split(" ").map(String::toLong))
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

    // Part two specific

    data class RangeOutput(val matchedRanges: LongRange?, val missedRanges: List<LongRange>)

    private fun LongRange.checkAgainstRange(mapRange: MapRange): RangeOutput {
        val (destinationStart, sourceStart, length) = mapRange

        fun LongRange.move(translation: Long) = LongRange(start + translation, last + translation)
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

    private fun processMap(initial: LongRange, map: List<MapRange>): List<LongRange> {
        val processedValues = mutableListOf<LongRange>()

        val range = map.first()
        val output = initial.checkAgainstRange(range)

        // save the parts of the range that overlap the map
        if (output.matchedRanges != null) processedValues.add(output.matchedRanges)

        // recursively check each of the extra bits against the rest of the map
        output.missedRanges.forEach {
            if (map.size > 1) {
                processedValues.addAll(processMap(it, map.drop(1)))
            } else {
                processedValues.add(it)
            }
        }
        return processedValues
    }

    override fun partTwo(): Long {
        val seedRanges = input[0].substringAfter(": ").split(" ").chunked(2) { (start, length) -> LongRange(start.toLong(), start.toLong() + length.toLong()) }

        val locationRanges = mutableListOf<LongRange>()

        seedRanges.forEach { seedRange ->
            var currentSeedRanges = listOf(seedRange)

            maps.forEach { map ->
                val ranges = map.map { it.split(" ").map(String::toLong) }

                currentSeedRanges = currentSeedRanges.flatMap {
                    processMap(it, ranges)
                }
            }

            locationRanges.addAll(currentSeedRanges)
        }

        return locationRanges.sortedBy { it.first }[0].first
    }
}
