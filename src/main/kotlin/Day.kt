import java.io.File

sealed class Day {
    private val day = this::class.simpleName!!.firstInt()
    open val input by lazy { File(javaClass.getResource("inputs/$day")?.toURI() ?: error("Missing input for day $day")).readLines() }
    abstract fun partOne(): Any
    abstract fun partTwo(): Any
}

fun getDayByInt(day: Int): Day {
    val dayRegex = day.toString().padStart(2, '0').toRegex()
    val allDays = Day::class.sealedSubclasses
    return allDays.first { dayRegex.containsMatchIn(it.simpleName!!) }.objectInstance!!
}