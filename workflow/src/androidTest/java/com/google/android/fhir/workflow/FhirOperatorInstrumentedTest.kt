package com.google.android.fhir.workflow

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.uhn.fhir.context.FhirContext
import com.google.android.fhir.FhirEngineProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.Bundle
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FhirOperatorInstrumentedTest {
  private val fhirEngine =
    FhirEngineProvider.getInstance(ApplicationProvider.getApplicationContext())

  @Test
  fun test() {
    val inputStream = javaClass.getResourceAsStream("/ANCIND01-bundle.json")
    val content = inputStream!!.bufferedReader(Charsets.UTF_8).readText()
    val resourceJson = JSONObject(content)
    val bundle =
      FhirContext.forR4()
        .newJsonParser()
        .parseResource(Bundle::class.java, resourceJson.toString()) as
        Bundle
    for (entry in bundle.entry) {
      runBlocking { fhirEngine.save(entry.resource) }
    }

    val fhirOperator = FhirOperator(FhirContext.forR4(), fhirEngine)

    val measureReport =
      fhirOperator.evaluateMeasure(
        "http://fhir.org/guides/who/anc-cds/Measure/ANCIND01",
        "2020-01-01",
        "2020-01-31",
        "subject",
        "patient-charity-otala-1"
      )

    assertThat(measureReport).isEqualTo(1)
  }
}
