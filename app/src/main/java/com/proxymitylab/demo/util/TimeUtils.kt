package com.proxymitylab.demo.util

import java.util.*
import java.util.concurrent.TimeUnit



object TimeUtils {
    val times: List<Long> = Arrays.asList(
        TimeUnit.DAYS.toMillis(365),
        TimeUnit.DAYS.toMillis(30),
        TimeUnit.DAYS.toMillis(7),
        TimeUnit.DAYS.toMillis(1),
        TimeUnit.HOURS.toMillis(1),
        TimeUnit.MINUTES.toMillis(1),
        TimeUnit.SECONDS.toMillis(1)
    )
    val timesString: List<String> = Arrays.asList(
        "yr", "mo", "wk", "day", "hr", "min", "sec"
    )

    fun getRelativeTime(date: Long): CharSequence {
        return toDuration(Math.abs(System.currentTimeMillis() - date))
    }

    private fun toDuration(duration: Long): String {
        val sb = StringBuilder()
        for (i in times.indices) {
            val current = times[i]
            val temp = duration / current
            if (temp > 0) {
                sb.append(temp)
                    .append(" ")
                    .append(timesString[i])
                    .append(if (temp > 1) "s" else "")
                    .append(" ago")
                break
            }
        }
        return if (sb.toString().isEmpty()) "now" else sb.toString()
    }
}