object Day07: BaseDay<Int>(7) {
    override fun partOne(): Int {
        val betMap = input.associate { it.substringBefore(" ") to it.substringAfter(" ") }.mapValues { it.value.toInt() }
        val hands = betMap.keys
        val sortedHands = hands.map { hand ->
            val charCount = hand.associate { c -> c to hand.count { it == c } }.values
            // if a hand has 5 of a kind it's score is 25, 4 of a kind is 16 + 1, full house is 9 + 4, etc.
            hand to charCount.sumOf { it * it }
        }

        val handTypes = sortedHands.groupBy { it.second }.toSortedMap()

        val cardRankings = mutableListOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2', '_').reversed()
        val result = handTypes.flatMap { (type, hands) ->
            hands.sortedBy { (cards, rank) ->
                cards.map { String.format("%02d", cardRankings.indexOf(it)) }.joinToString("").toLong()
            }
        }.map { it.first }

        return result.mapIndexed { index, hand -> betMap[hand]!! * (index + 1) }.sum()
    }

    override fun partTwo(): Int {
        val betMap = input.associate { it.substringBefore(" ") to it.substringAfter(" ") }.mapValues { it.value.toInt() }
        val hands = betMap.keys
        val sortedHands = hands.map { hand ->
            val cardCount = hand.associate { c -> c to hand.count { it == c && it != 'J' } }.toMutableMap()
            // add jokers to the type of card that there is already the most of
            val jokerCount = hand.count { it == 'J' }
            val highestCardCount = cardCount.maxBy { it.value }
            cardCount[highestCardCount.key] = highestCardCount.value + jokerCount
            hand to cardCount.values.sumOf { it * it }
        }

        val handTypes = sortedHands.groupBy { it.second }.toSortedMap()

        val cardRankings = mutableListOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J', '_').reversed()
        val result = handTypes.flatMap { (type, hands) ->
            hands.sortedBy { (cards, rank) ->
                cards.map { String.format("%02d", cardRankings.indexOf(it)) }.joinToString("").toLong()
            }
        }.map { it.first }

        return result.mapIndexed { index, hand -> betMap[hand]!! * (index + 1) }.sum()
    }

}
