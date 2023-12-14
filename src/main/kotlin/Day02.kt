object Day02: BaseDay(2) {
    override fun partOne(): Int {
        val maximums = mapOf("red" to 12, "green" to 13, "blue" to 14)
        var sum = 0
        line@ for (line in input) {
            val id = line.substring(line.indexOf(" ") + 1, line.indexOf(":")).toInt()
            val data = line.substringAfter(": ").replace(";", ",")
            for (cube in data.split(", ")) {
                val info = cube.split(" ")
                val count = info.first().toInt()
                val color = info.last()
                if (count > maximums[color]!!) {
                    continue@line
                }
            }
            sum += id
        }
        return sum
    }

    override fun partTwo(): Int {
        var sum = 0
        for (line in input) {
            val data = line.substringAfter(": ").replace(";", ",")
            val maximums = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            for (cube in data.split(", ")) {
                val info = cube.split(" ")
                val count = info.first().toInt()
                val color = info.last()
                if (count > maximums[color]!!) {
                    maximums[color] = count
                }
            }

            var power = 1
            maximums.values.forEach { power *= it }
            sum += power
        }
        return sum
    }
}