package com.nemesis.empractice

fun List<Any>.firstInt(): Int? = firstOrNull { it is Int } as? Int

fun List<Any>.firstIntAlternative(): Int? = firstNotNullOfOrNull { it.toString().toIntOrNull() }