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

package org.gradle.language.base
import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.Sample
import org.junit.Rule

import static org.gradle.util.TextUtil.toPlatformLineSeparators

class CustomLanguageSampleIntegrationTest extends AbstractIntegrationSpec {
    @Rule Sample customLanguageType = new Sample(temporaryFolder, "customLanguageType")

    def "can use custom language in component"() {
        given:
        sample customLanguageType
        /*customLanguageType.dir.file("build.gradle") << """

task checkModel << {
    assert project.componentSpecs.size() == 2
    def titleAImage = project.componentSpecs.TitleA
    assert titleAImage instanceof ImageComponent
    assert titleAImage.projectPath == project.path
    assert titleAImage.displayName == "DefaultImageComponent 'TitleA'"
    assert titleAImage.binaries.collect{it.name}.sort() == ['TitleA14pxBinary', 'TitleA28pxBinary', 'TitleA40pxBinary']
}

"""       */
        when:
        succeeds "components"
        then:
        output.contains(toPlatformLineSeparators("""
DefaultSampleComponent 'main'
-----------------------------

Source sets
    DefaultSomeLanguageSourceSet 'main:myCustomSources'
        src${File.separator}main${File.separator}myCustomSources
"""))
    }
}