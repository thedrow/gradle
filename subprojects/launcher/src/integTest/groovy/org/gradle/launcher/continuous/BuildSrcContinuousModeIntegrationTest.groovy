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

class BuildSrcContinuousModeIntegrationTest extends NonComponentProjectContinuousModeIntegrationTest {
    def setup() {
        def buildSrcBuildFile = file("buildSrc/build.gradle")
        def buildSrcSrc = file("src/main/java/example/Main.java")

        buildSrcSrc.parentFile.mkdirs()

        buildSrcBuildFile << """
    apply plugin: 'java'
"""
        buildSrcSrc << """
package example;
public class Main { }
"""
    }
}
