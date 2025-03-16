package com.nemesis.empractice

fun shakerSort(list: List<Int?>?): List<Int?> {
    if (list == null) return emptyList()

    val result = list.toMutableList()

    var start = 0
    var end = result.lastIndex

    var sorted = false

    while (!sorted) {
        sorted = true

        for (i in start until end) {
            if (swapIfGreaterOrNull(result, i, i + 1)) {
                sorted = false
            }
        }

        if (!sorted) {
            do {
                end--
            } while (result[end] == null && end >= start)


            for (i in end downTo start + 1) {
                if (swapIfGreaterOrNull(result, i - 1, i)) {
                    sorted = false
                }
            }

            start++
        }
    }

    return result
}

private fun swapIfGreaterOrNull(list: MutableList<Int?>, i1: Int, i2: Int): Boolean {
    val n1 = list[i1]
    val n2 = list[i2]

    if (n1 == null || (n2 != null && n1 > n2)) {
        list[i1] = n2
        list[i2] = n1
        return true
    }

    return false
}
