package com.georgevik.nqueens.infra.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val uiDateTimeFormatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(ZoneId.systemDefault())

fun Instant.toUiFormat(): String = uiDateTimeFormatter.format(this)
