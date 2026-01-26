package com.peco2282.transfer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testExtractUrl() {
    val body = "Short URL created!\nShort: https://example.com/api/s/abcdef"
    val expected = "https://example.com/api/s/abcdef"
    assertEquals(expected, extractUrl(body))
  }

  @Test
  fun testExtractUrlWithWhitespace() {
    val body = "Short URL created!\nShort: https://example.com/api/s/abcdef  \n"
    val expected = "https://example.com/api/s/abcdef"
    assertEquals(expected, extractUrl(body))
  }

  @Test
  fun testExtractUrlInvalid() {
    val body = "Error occurred"
    assertNull(extractUrl(body))
  }
}
