object Day09: BaseDay(9) {

    private fun extrapolate(input: List<List<Int>>): List<List<Int>> {
        val nums = input.first()

        val newNums = mutableListOf<Int>()
        for (i in 0..nums.size - 2) {
            newNums.add(nums[i + 1] - nums[i])
        }

        if (newNums.all { it == 0 }) return input

        val newOutput = input.toMutableList()
        newOutput.add(0, newNums)
        return extrapolate(newOutput)
    }

    private val parsedInput = input.map { extrapolate(listOf(it.split(" ").map(String::toInt))) }

    override fun partOne() = parsedInput.sumOf { it.fold(0) { acc, ints -> acc + ints.last() }.toInt() }

    override fun partTwo() = parsedInput.sumOf { it.fold(0) { acc, ints -> ints.first() - acc }.toInt() }
}
