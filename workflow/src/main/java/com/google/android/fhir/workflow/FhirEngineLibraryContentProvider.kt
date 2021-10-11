package com.google.android.fhir.workflow

import com.google.android.fhir.FhirEngine
import com.google.android.fhir.search.StringFilterModifier
import com.google.android.fhir.search.search
import kotlinx.coroutines.runBlocking
import org.hl7.elm.r1.VersionedIdentifier
import org.hl7.fhir.instance.model.api.IBaseResource
import org.hl7.fhir.r4.model.Library
import org.opencds.cqf.cql.evaluator.cql2elm.content.fhir.BaseFhirLibraryContentProvider
import org.opencds.cqf.cql.evaluator.fhir.adapter.r4.AdapterFactory

class FhirEngineLibraryContentProvider(
  private val fhirEngine: FhirEngine,
  adapterFactory: AdapterFactory
) : BaseFhirLibraryContentProvider(adapterFactory) {

  override fun getLibrary(libraryIdentifier: VersionedIdentifier): IBaseResource {
    return runBlocking {
      fhirEngine
        .search<Library> {
          filter(Library.NAME) {
            value = libraryIdentifier.id
            modifier = StringFilterModifier.MATCHES_EXACTLY
          }
        }
        .single()
    }
  }
}
