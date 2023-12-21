data object Day15: Day() {
    private fun String.HASH(): Int {
        var currentValue = 0
        forEach { char ->
            currentValue += char.code
            currentValue *= 17
            currentValue %= 256
        }
        return currentValue
    }

    override fun partOne() = input[0].split(",").sumOf { it.HASH() }

    override fun partTwo(): Any {
        val boxes = MutableList(256) { mutableMapOf<String, Int>() }
        input[0].split(",").forEach { step ->
            val label = step.takeWhile(Char::isLetter)
            val operation = step.first { it in "-=" }

            val boxNumber = label.HASH()
            when (operation) {
                '-' -> boxes[boxNumber].remove(label)
                '=' -> boxes[boxNumber][label] = step.takeLast(1).toInt()
            }
        }

        var sum = 0
        boxes.forEachIndexed { boxID, lenses ->
            lenses.onEachIndexed { lensIndex, entry ->
                val boxNumber = boxID + 1
                val lensNumber = lensIndex + 1
                val focusNumber = entry.value
                sum += boxNumber * lensNumber * focusNumber
            }
        }

        return sum
    }
}