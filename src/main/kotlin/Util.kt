import kotlin.io.path.Path
import kotlin.io.path.readLines

fun getInput(day: Int): List<String> = Path("src/main/resources/inputs/$day").readLines()

abstract class BaseDay(val day: Int) {
    abstract fun printPartOne(input: List<String>)
    abstract fun printPartTwo(input: List<String>)
}