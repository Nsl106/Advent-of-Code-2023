import kotlin.io.path.Path
import kotlin.io.path.readLines

fun getInput(day: Int): List<String> = Path("src/main/resources/inputs/$day").readLines()
