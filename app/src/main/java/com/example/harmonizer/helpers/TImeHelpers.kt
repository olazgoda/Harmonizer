package com.example.harmonizer.helpers

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Long.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()) : ZonedDateTime {
    return Instant.ofEpochMilli(this).atZone(zoneId)
}

fun Long.toDateString() : String
{
    val zonedDateTime = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return zonedDateTime.format(formatter)
}

fun ZonedDateTime.toDateString() : String
{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}