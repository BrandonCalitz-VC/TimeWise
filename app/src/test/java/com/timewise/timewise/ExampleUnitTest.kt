package com.timewise.timewise

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun ValidEmail() {
        assertTrue(isValidEmail("st10039352@vcconnect,edu.za"))
    }
}
