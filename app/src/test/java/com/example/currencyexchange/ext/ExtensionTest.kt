package com.example.currencyexchange.ext

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Calendar

class ExtensionTest {

    @Test
    fun `to display string`() {
        val current = 1626171692836
        val inString = current.toDisplayString()
        assertEquals(inString, "Tue, 13 Jul 2021 15:51:32")
    }

    @Test
    fun `is cache time out`(){
        val current = System.currentTimeMillis()
        val isTimeOut = current.isCachedTimeout(30L)
        assertFalse(isTimeOut)

        val calenderBefore29Minutes = Calendar.getInstance()
        calenderBefore29Minutes.add(Calendar.MINUTE, -29)
        val isTimeOut29 = calenderBefore29Minutes.time.time.isCachedTimeout(30L)
        assertFalse(isTimeOut29)

        val calenderBefore31Minutes = Calendar.getInstance()
        calenderBefore31Minutes.add(Calendar.MINUTE, -31)
        val isTimeOut31 = calenderBefore31Minutes.time.time.isCachedTimeout(30L)
        assertTrue(isTimeOut31)
    }
}