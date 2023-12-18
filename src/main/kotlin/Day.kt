import java.io.File

sealed class BaseDay(private val day: Int) {
    protected open val input by lazy { File(javaClass.getResource("inputs/$day")?.toURI() ?: error("Missing input for day $day")).readLines() }
    abstract fun partOne(): Any
    abstract fun partTwo(): Any
}