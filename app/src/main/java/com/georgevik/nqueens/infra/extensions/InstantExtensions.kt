package com.georgevik.nqueens.infra.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val uiDateTimeFormatter =
    DateTimeFormatter.ofPattern("d-MM-yyyy HH:mm").withZone(ZoneId.systemDefault())

fun Instant.toUiFormat(): String = uiDateTimeFormatter.format(this)
