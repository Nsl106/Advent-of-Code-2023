import java.io.File

abstract class BaseDay(val day: Int) {
    protected val input by lazy { File(javaClass.getResource("inputs/$day")?.toURI() ?: error("Missing input for day $day")).readLines() }
    abstract fun printPartOne()
    abstract fun printPartTwo()
}