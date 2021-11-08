package com.google.android.fhir.workflow

import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import ca.uhn.fhir.context.FhirContext
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.FhirEngineProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Library
import org.hl7.fhir.r4.model.Resource
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
  private val fhirContext = FhirContext.forR4()
  private val jsonParser = fhirContext.newJsonParser()
  private val xmlParser = fhirContext.newXmlParser()

  @Test
  fun test() = runBlocking {
    val fhirOperator = FhirOperator(fhirContext, fhirEngine)

    val bundle =
      jsonParser.parseResource(javaClass.getResourceAsStream("/ANCIND01-bundle.json")) as Bundle
    for (entry in bundle.entry) {
      if (entry.resource.resourceType == ResourceType.Library) {
        fhirOperator.loadLib(entry.resource as Library)
      } else {
        fhirEngine.save(entry.resource)
      }
    }

    fhirEngine.loadDirectory("/first-contact/01-registration/patient-charity-otala-1.json")
    fhirEngine.loadDirectory("/first-contact/02-enrollment/careplan-charity-otala-1-pregnancy-plan.xml")
    fhirEngine.loadDirectory("/first-contact/02-enrollment/careteam-anc-team.xml")

    val measureReport =
      fhirOperator.evaluateMeasure(
        "http://fhir.org/guides/who/anc-cds/Measure/ANCIND01",
        "2020-01-01",
        "2020-01-31",
        "subject",
        "charity-otala-1"
      )

    assertThat(measureReport.date).isEqualTo(1)
  }

  private suspend fun FhirEngine.loadDirectory(path: String) {
      if (path.endsWith(suffix = ".xml")) {
        val resource = xmlParser.parseResource(javaClass.getResourceAsStream(path)) as Resource
        save(resource)
      } else if (path.endsWith(".json")) {
        val resource = jsonParser.parseResource(javaClass.getResourceAsStream(path)) as Resource
        save(resource)
      }
  }
}
