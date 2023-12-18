// https://github.com/Mistborn94/advent-of-code-2023/blob/master/src/main/kotlin/helper/graph/AStarSearch.kt
fun <T> runDijkstra(
    getNeighbors: (T) -> Iterable<T>,
    getCost: (T, T) -> Int,
    start: T,
    atEnd: (T) -> Boolean,
): SearchResult<T> {
    val toVisit = mutableSetOf(ItemCost(start, 0))
    var endVertex: T? = null
    val seenPoints = mutableMapOf<T, PathSegment<T>>()

    while (endVertex == null) {
        if (toVisit.isEmpty()) break

        val shortestTraveledItem = toVisit.minBy { it.cost }
        toVisit.remove(shortestTraveledItem)
        val (currentItem, currentDistance) = shortestTraveledItem

        if (atEnd(currentItem)) endVertex = currentItem

        val nextPoints = getNeighbors(currentItem)
            .filter { it !in seenPoints }
            .map { ItemCost(it, currentDistance + getCost(currentItem, it)) }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.map { it.value to PathSegment(it.cost, currentItem) })
    }

    return SearchResult(start, endVertex, seenPoints)
}

class SearchResult<T>(val start: T, val end: T?, private val result: Map<T, PathSegment<T>>) {
    fun getCost(item: T = end!!) = result.getValue(item).totalCost
    fun getPath() = getPath(end!!, emptyList())
    fun allSeen() = result.keys

    private tailrec fun getPath(endItem: T, pathEnd: List<T>): List<T> {
        val previous = result[endItem]?.previous

        return if (previous == null) {
            listOf(endItem) + pathEnd
        } else {
            getPath(previous, listOf(endItem) + pathEnd)
        }
    }
}

data class ItemCost<T>(val value: T, val cost: Int)
data class PathSegment<T>(val totalCost: Int, val previous: T)