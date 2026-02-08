package com.getyourplace.Helpers

object FilterMatcher {
    fun check(value: Int, requirement: String?): Boolean {
        if (requirement == null || requirement == "All" || requirement == "None") {
            return true
        }

        // Handle "4+" logic
        if (requirement.contains("+")) {
            val numericPart = requirement.replace("+", "")
            val target = numericPart.toIntOrNull()
            if (target != null) {
                return value >= target
            }
        }

        // Handle exact numeric match (e.g., "1", "2")
        val exactTarget = requirement.toIntOrNull()
        if (exactTarget != null) {
            return value == exactTarget
        }

        return true
    }
}