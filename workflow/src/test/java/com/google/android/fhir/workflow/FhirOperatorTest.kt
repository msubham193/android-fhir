package com.google.android.fhir.workflow

import androidx.test.core.app.ApplicationProvider
import ca.uhn.fhir.context.FhirContext
import com.google.android.fhir.FhirEngineProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Library
import org.hl7.fhir.r4.model.ResourceType
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class FhirOperatorTest {
  private val fhirEngine =
    FhirEngineProvider.getInstance(ApplicationProvider.getApplicationContext())

  @Test
  fun test() {
    val fhirOperator = FhirOperator(FhirContext.forR4(), fhirEngine)

    val bundle =
      FhirContext.forR4()
        .newJsonParser()
        .parseResource(javaClass.getResourceAsStream("/ANCIND01-bundle.json")) as
        Bundle
    runBlocking {
      for (entry in bundle.entry) {
        if (entry.resource.resourceType == ResourceType.Library) {
          fhirOperator.loadLib(entry.resource as Library)
        } else {
          fhirEngine.save(entry.resource)
        }
      }
    }

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
