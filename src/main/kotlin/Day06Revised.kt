import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object Day06Revised: BaseDay(6) {
    override fun partOne(): Int {
        val (times, records) = input.map { it.substringAfter(":").trim().split(Regex(" +")).map(String::toDouble) }
        return List(times.size) { calculate(times[it], records[it]) }.reduce(Int::times).toInt()
    }

    override fun partTwo(): Int {
        return input.map { it.filter(Char::isDigit).toDouble() }.let { calculate(it[0], it[1]) }
    }

    // x = held time
    // (racetime - x) * x = distance
    // (racetime * x) - (x^2) = distance
    // racetime * x = distance + (x^2)
    // 0 = (x^2) - (racetime * x) + distance

    private fun calculate(raceTime: Double, recordToBeat: Double): Int {
        return (floor((raceTime + sqrt((raceTime * raceTime) - (4 * recordToBeat))) / 2) -
                (ceil(raceTime - sqrt((raceTime * raceTime) - (4 * recordToBeat))) / 2) + 1).toInt()
    }
}
