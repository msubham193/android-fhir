package com.google.android.fhir.workflow

import org.hl7.elm.r1.VersionedIdentifier
import org.hl7.fhir.instance.model.api.IBaseResource
import org.hl7.fhir.r4.model.Library
import org.opencds.cqf.cql.evaluator.cql2elm.content.fhir.BaseFhirLibraryContentProvider
import org.opencds.cqf.cql.evaluator.fhir.adapter.r4.AdapterFactory

class FhirEngineLibraryContentProvider(
  adapterFactory: AdapterFactory
) : BaseFhirLibraryContentProvider(adapterFactory) {
  val libs = mutableMapOf<String, Library>()

  override fun getLibrary(libraryIdentifier: VersionedIdentifier): IBaseResource {
    return libs[libraryIdentifier.id]!!
  }
}
