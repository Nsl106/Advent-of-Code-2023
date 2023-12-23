class CubeGrid<T>(private val xSize: Int, private val ySize: Int, private val zSize: Int) {
    val internalList: MutableList<MutableList<MutableList<T?>>> = MutableList(xSize) { MutableList(ySize) { MutableList(zSize) { null } } }

    fun isCoordinateEmpty(coordinate: TripleCoordinate): Boolean {
        return internalList[coordinate.x][coordinate.y][coordinate.z] == null
    }

    operator fun set(coordinate: TripleCoordinate, newValue: T?) {
        internalList[coordinate.x][coordinate.y][coordinate.z] = newValue
    }

    operator fun get(coordinate: TripleCoordinate): T? {
        return internalList[coordinate.x][coordinate.y][coordinate.z]
    }

    fun copy(): CubeGrid<T> {
        val newGrid = CubeGrid<T>(xSize, ySize, zSize)
        for (x in 0..<xSize) {
            for (y in 0..<ySize) {
                for (z in 0..<zSize) {
                    val coordinate = TripleCoordinate(x, y, z)
                    newGrid[coordinate] = get(coordinate)
                }
            }
        }
        return newGrid
    }

    override fun equals(other: Any?): Boolean {
        return (other is CubeGrid<*>) && (this.internalList == other.internalList)
    }

    override fun hashCode(): Int {
        var result = xSize
        result = 31 * result + ySize
        result = 31 * result + zSize
        result = 31 * result + internalList.hashCode()
        return result
    }
}

data class TripleCoordinate(val x: Int, val y: Int, val z: Int)