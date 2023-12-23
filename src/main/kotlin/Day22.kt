data object Day22: Day() {
    fun TripleCoordinate.drop() = TripleCoordinate(x, y, z - 1)

    data class Block(var one: TripleCoordinate, var two: TripleCoordinate) {
        val occupyingCoordinates get() = buildSet { for (x in one.x..two.x) for (y in one.y..two.y) for (z in one.z..two.z) add(TripleCoordinate(x, y, z)) }

        val minZ get() = one.z

        fun getSupportingLayer() = occupyingCoordinates.filter { it.z == minZ }.map { it.drop() }

        fun drop() {
            one = one.drop()
            two = two.drop()
        }

        fun copy() = Block(one.copy(), two.copy())
    }

    private val allBlocks = mutableSetOf<Block>()
    private val occupiedCoordinates: MutableSet<TripleCoordinate>

    init {
        input.forEach { line ->
            val (first, second) = line.split('~').map { it.split(",").map(String::toInt) }.map { (x, y, z) -> TripleCoordinate(x, y, z) }
            allBlocks.add(Block(first, second))
        }
        occupiedCoordinates = allBlocks.flatMap { it.occupyingCoordinates }.toMutableSet()

        solve()
    }

    private fun dropBlocks(blocks: MutableSet<Block>, occupied: MutableSet<TripleCoordinate>): Int {
        var totalDropped = 0
        blocks.sortedBy { it.minZ }.forEach { block ->
            var wasDropped = false
            // While the block has nothing below it and isn't on the bottom, move it down in the grid and the block coordinate systems
            while (block.getSupportingLayer().all { !occupied.contains(it) && it.z > 0 }) {
                // Remove the current position of the block
                occupied.removeAll(block.occupyingCoordinates)

                // Drop the block, then re-add it
                block.drop()
                occupied.addAll(block.occupyingCoordinates)

                wasDropped = true
            }
            if (wasDropped) totalDropped++
        }

        return totalDropped
    }


    var partOne = 0
    var partTwo = 0

    private fun solve() {
        // Initial drop
        dropBlocks(allBlocks, occupiedCoordinates)

        // For each block, remove it from a copy of the lists, then re-run the fall and count its effect
        allBlocks.forEach { block ->
            val newBlocks = allBlocks.map { it.copy() }.toMutableSet()
            newBlocks.remove(block)

            val newOccupied = occupiedCoordinates.map { it.copy() }.toMutableSet()
            newOccupied.removeAll(block.occupyingCoordinates)

            val result = dropBlocks(newBlocks, newOccupied)

            if (result == 0) partOne++
            partTwo += result
        }
    }

    override fun partOne() = partOne
    override fun partTwo() = partTwo
}