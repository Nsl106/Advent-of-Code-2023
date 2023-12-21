data object Day14: Day() {
    enum class ShiftDirection {
        NORTH, WEST, SOUTH, EAST
    }

    private val variationCount = mutableSetOf<List<List<Char>>>()
    private fun shift(shiftDirection: ShiftDirection, initial: List<List<Char>>): List<List<Char>> {
        variationCount.add(initial)
        val mutableGrid = initial.map { it.toMutableList() }.toMutableList()

        when (shiftDirection) {
            ShiftDirection.NORTH -> {
                for (row in mutableGrid.indices) {
                    for (col in mutableGrid[row].indices) {
                        if (mutableGrid[row][col] != 'O') continue
                        var indexAbove = row - 1
                        while (true) {
                            val charAbove = mutableGrid.getOrNull(indexAbove)?.get(col) ?: break
                            if (charAbove == '.') {
                                mutableGrid[indexAbove][col] = 'O'
                                mutableGrid[indexAbove + 1][col] = '.'
                                indexAbove--
                            } else break
                        }
                    }
                }
            }

            ShiftDirection.WEST -> {
                for (row in mutableGrid.indices) {
                    for (col in mutableGrid[row].indices) {
                        if (mutableGrid[row][col] != 'O') continue
                        var indexLeft = col - 1
                        while (true) {
                            val charBelow = mutableGrid[row].getOrNull(indexLeft) ?: break
                            if (charBelow == '.') {
                                mutableGrid[row][indexLeft] = 'O'
                                mutableGrid[row][indexLeft + 1] = '.'
                                indexLeft--
                            } else break
                        }
                    }
                }
            }

            ShiftDirection.SOUTH -> {
                for (row in mutableGrid.indices.reversed()) {
                    for (col in mutableGrid[row].indices) {
                        if (mutableGrid[row][col] != 'O') continue
                        var indexBelow = row + 1
                        while (true) {
                            val charBelow = mutableGrid.getOrNull(indexBelow)?.get(col) ?: break
                            if (charBelow == '.') {
                                mutableGrid[indexBelow][col] = 'O'
                                mutableGrid[indexBelow - 1][col] = '.'
                                indexBelow++
                            } else break
                        }
                    }
                }
            }

            ShiftDirection.EAST -> {
                for (row in mutableGrid.indices) {
                    for (col in mutableGrid[row].indices.reversed()) {
                        if (mutableGrid[row][col] != 'O') continue
                        var indexRight = col + 1
                        while (true) {
                            val charBelow = mutableGrid[row].getOrNull(indexRight) ?: break
                            if (charBelow == '.') {
                                mutableGrid[row][indexRight] = 'O'
                                mutableGrid[row][indexRight - 1] = '.'
                                indexRight++
                            } else break
                        }
                    }
                }
            }
        }
        return mutableGrid
    }

    override fun partOne(): Any {
        val grid = input.map { it.toMutableList() }.toMutableList()
        var currentWeight = 0
        return shift(ShiftDirection.NORTH, grid).reversed().sumOf { row ->
            currentWeight++
            row.count { it == 'O' } * currentWeight
        }
    }

    override fun partTwo(): Any {
        val grid = input.map { it.toMutableList() }.toMutableList()
        var currentGrid: List<List<Char>> = grid
        var lastCachedSize = 0

        // Cycle until it stops doing new things, then skip through those repeated segments and run the last bits
        for (i in 0..1000000000) {
            currentGrid = cycle(currentGrid)
            if (lastCachedSize == variationCount.size) break
            lastCachedSize = variationCount.size
        }
        for (i in 1..(lastCachedSize + (1000000000 % lastCachedSize))) {
            currentGrid = cycle(currentGrid)
        }

        var currentWeight = 0
        return currentGrid.reversed().sumOf { row ->
            currentWeight++
            row.count { it == 'O' } * currentWeight
        }
    }

    private fun cycle(currentGrid: List<List<Char>>): List<List<Char>> {
        var grid = currentGrid
        grid = shift(ShiftDirection.NORTH, grid)
        grid = shift(ShiftDirection.WEST, grid)
        grid = shift(ShiftDirection.SOUTH, grid)
        grid = shift(ShiftDirection.EAST, grid)
        return grid
    }
}