package com.google.android.fhir.workflow

import ca.uhn.fhir.context.FhirContext
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.search.search
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.ValueSet
import org.opencds.cqf.cql.engine.runtime.Code
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo
import org.opencds.cqf.cql.evaluator.engine.util.ValueSetUtil

class FhirEngineTerminologyProvider(
  private val fhirContext: FhirContext,
  private val fhirEngine: FhirEngine
) : TerminologyProvider {
  override fun `in`(code: Code, valueSet: ValueSetInfo): Boolean {
    return expand(valueSet).any { it.code == code.code && it.system == code.system }
  }

  override fun expand(valueSetInfo: ValueSetInfo): MutableIterable<Code> {
    val valueSet = runBlocking {
      fhirEngine.search<ValueSet> { filter(ValueSet.URL, valueSetInfo.id) }.singleOrNull()
    }
    return ValueSetUtil.getCodesInExpansion(fhirContext, valueSet)
  }

  override fun lookup(code: Code?, codeSystem: CodeSystemInfo?): Code {
    TODO("Not yet implemented")
  }
}
