package com.google.android.fhir.workflow

import com.google.android.fhir.FhirEngine
import com.google.android.fhir.search.search
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.Patient
import org.opencds.cqf.cql.engine.retrieve.TerminologyAwareRetrieveProvider
import org.opencds.cqf.cql.engine.runtime.Code
import org.opencds.cqf.cql.engine.runtime.Interval

class FhirEngineRetrieveProvider(val fhirEngine: FhirEngine) : TerminologyAwareRetrieveProvider() {
  override fun retrieve(
    context: String?,
    contextPath: String?,
    contextValue: Any?,
    dataType: String?,
    templateId: String?,
    codePath: String?,
    codes: MutableIterable<Code>?,
    valueSet: String?,
    datePath: String?,
    dateLowPath: String?,
    dateHighPath: String?,
    dateRange: Interval?
  ): MutableIterable<Any> {
    return runBlocking {
      if (context == "Patient") {
        val id = contextValue.toString().removePrefix("patient-")
        val result = fhirEngine.load(Patient::class.java, id)
        result
      }
      mutableListOf()
    }
  }
}
