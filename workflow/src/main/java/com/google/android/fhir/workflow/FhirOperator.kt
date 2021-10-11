package com.google.android.fhir.workflow

import ca.uhn.fhir.context.FhirContext
import com.google.android.fhir.FhirEngine
import org.hl7.fhir.r4.model.MeasureReport
import org.opencds.cqf.cql.engine.data.CompositeDataProvider
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver
import org.opencds.cqf.cql.evaluator.engine.model.CachingModelResolverDecorator
import org.opencds.cqf.cql.evaluator.fhir.adapter.r4.AdapterFactory
import org.opencds.cqf.cql.evaluator.measure.MeasureEvalConfig
import org.opencds.cqf.cql.evaluator.measure.r4.R4MeasureProcessor

class FhirOperator(fhirContext: FhirContext, fhirEngine: FhirEngine) {
  private var measureProcessor: R4MeasureProcessor
  init {
    val terminologyProvider = FhirEngineTerminologyProvider(fhirContext, fhirEngine)
    val adapterFactory = AdapterFactory()
    val libraryContentProvider = FhirEngineLibraryContentProvider(fhirEngine, adapterFactory)
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
    val fhirEngineDal = FhirEngineDal(fhirEngine)
    measureProcessor =
      R4MeasureProcessor(
        null,
        null,
        null,
        null,
        null,
        terminologyProvider,
        libraryContentProvider,
        dataProvider,
        fhirEngineDal,
        MeasureEvalConfig.defaultConfig()
      )
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
