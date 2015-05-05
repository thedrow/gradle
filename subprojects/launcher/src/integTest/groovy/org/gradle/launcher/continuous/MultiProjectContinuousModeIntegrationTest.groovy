/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.launcher.continuous

import org.gradle.integtests.fixtures.jvm.IncrementalTestJvmComponent

abstract class MultiProjectContinuousModeIntegrationTest extends AbstractContinuousModeExecutionIntegrationTest {
    @Override
    IncrementalTestJvmComponent getApp() {
        return null
    }

    @Override
    String getCompileTask() {
        return null
    }

    @Override
    void validSource() {

    }

    @Override
    void invalidSource() {

    }

    @Override
    void changeSource() {

    }

    @Override
    void createSource() {

    }

    @Override
    void deleteSource() {

    }
}
