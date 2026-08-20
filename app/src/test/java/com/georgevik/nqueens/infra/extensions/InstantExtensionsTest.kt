package com.georgevik.nqueens.infra.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class InstantExtensionsTest {

    @Test
    fun `formats the instant with the ui pattern WHEN toUiFormat is called`() {
        val instant = Instant.parse("2024-01-02T03:04:05Z")
        val expected = instant.atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("d-MM-yyyy HH:mm"))

        assertEquals(expected, instant.toUiFormat())
    }

    @Test
    fun `matches the d-MM-yyyy HH mm shape WHEN toUiFormat is called`() {
        val result = Instant.parse("2024-12-31T23:59:00Z").toUiFormat()

        assert(result.matches(Regex("""\d{1,2}-\d{2}-\d{4} \d{2}:\d{2}""")))
    }
}
