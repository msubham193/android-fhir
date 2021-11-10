package com.google.android.fhir.workflow

import ca.uhn.fhir.context.FhirContext
import com.google.android.fhir.FhirEngine
import java.util.EnumSet
import org.hl7.fhir.r4.model.Library
import org.hl7.fhir.r4.model.MeasureReport
import org.opencds.cqf.cql.engine.data.CompositeDataProvider
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver
import org.opencds.cqf.cql.evaluator.engine.model.CachingModelResolverDecorator
import org.opencds.cqf.cql.evaluator.fhir.adapter.r4.AdapterFactory
import org.opencds.cqf.cql.evaluator.measure.MeasureEvalConfig
import org.opencds.cqf.cql.evaluator.measure.MeasureEvalOptions
import org.opencds.cqf.cql.evaluator.measure.r4.R4MeasureProcessor

class FhirOperator(fhirContext: FhirContext, fhirEngine: FhirEngine) {
  private var measureProcessor: R4MeasureProcessor
  val fhirEngineDal = FhirEngineDal(fhirEngine)
  val adapterFactory = AdapterFactory()
  val libraryContentProvider = FhirEngineLibraryContentProvider(adapterFactory)

  init {
    val terminologyProvider = FhirEngineTerminologyProvider(fhirContext, fhirEngine)
    val bundleRetrieveProvider =
      FhirEngineRetrieveProvider(fhirEngine).apply {
        setTerminologyProvider(terminologyProvider)
        isExpandValueSets = true
      }
    val dataProvider =
      CompositeDataProvider(
        CachingModelResolverDecorator(R4FhirModelResolver()),
        bundleRetrieveProvider
      )
    measureProcessor =
      R4MeasureProcessor(
        terminologyProvider,
        libraryContentProvider,
        dataProvider,
        fhirEngineDal
      )
  }

  fun loadLib(lib: Library) {
    if (lib.url != null) {
      fhirEngineDal.libs[lib.url] = lib
    }
    if (lib.name != null) {
      libraryContentProvider.libs[lib.name] = lib
    }
  }

  fun evaluateMeasure(
    url: String,
    start: String,
    end: String,
    reportType: String,
    subject: String
  ): MeasureReport {
    return measureProcessor.evaluateMeasure(
      url,
      start,
      end,
      reportType,
      subject,
      null,
      null,
      null,
      null,
      null,
      null
    )
  }
}
