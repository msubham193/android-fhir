/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fhir

import android.content.Context

/** The builder for [FhirEngine] instance */
object FhirEngineProvider {
  lateinit var fhirEngine: FhirEngine

  /**
   * Returns the cached [FhirEngine] instance. Creates a new instance from the supplied [Context] if
   * it doesn't exist.
   */
  @Synchronized
  fun getInstance(context: Context): FhirEngine {
    if (!::fhirEngine.isInitialized) {
      fhirEngine = FhirServices.builder(context.applicationContext).build().fhirEngine
    }
    return fhirEngine
  }
}
