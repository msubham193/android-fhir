package com.google.android.fhir.workflow

import com.google.android.fhir.FhirEngine
import com.google.android.fhir.search.search
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.instance.model.api.IBaseResource
import org.hl7.fhir.instance.model.api.IIdType
import org.hl7.fhir.r4.model.Patient
import org.opencds.cqf.cql.evaluator.fhir.dal.FhirDal

class FhirEngineDal(private val fhirEngine: FhirEngine) : FhirDal {
  override fun read(id: IIdType?): IBaseResource {
    TODO("Not yet implemented")
  }

  override fun create(resource: IBaseResource?) {
    TODO("Not yet implemented")
  }

  override fun update(resource: IBaseResource?) {
    TODO("Not yet implemented")
  }

  override fun delete(id: IIdType?) {
    TODO("Not yet implemented")
  }

  override fun search(resourceType: String?): MutableIterable<IBaseResource> {
    return runBlocking {
      when (resourceType) {
        "Patient" -> fhirEngine.search<Patient> {}.toMutableList()
        else -> fhirEngine.search<Patient> {}.toMutableList()
      }
    }
  }

  override fun searchByUrl(resourceType: String?, url: String?): MutableIterable<IBaseResource> {
    return runBlocking {
      when (resourceType) {
        "Patient" -> fhirEngine.search<Patient> {}.toMutableList()
        else -> fhirEngine.search<Patient> {}.toMutableList()
      }
    }
  }
}
