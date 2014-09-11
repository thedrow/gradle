/*
 * Copyright 2014 the original author or authors.
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

package org.gradle.api.internal.artifacts.ivyservice.ivyresolve

import org.gradle.api.artifacts.ModuleVersionIdentifier
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.internal.artifacts.DefaultModuleVersionSelector
import org.gradle.internal.component.model.ComponentResolveMetaData
import org.gradle.internal.component.model.DependencyMetaData
import org.gradle.internal.resolve.ModuleVersionResolveException
import org.gradle.internal.resolve.resolver.ComponentMetaDataResolver
import org.gradle.internal.resolve.resolver.DependencyToComponentIdResolver
import org.gradle.internal.resolve.result.BuildableComponentIdResolveResult
import org.gradle.internal.resolve.result.BuildableComponentResolveResult
import spock.lang.Specification

class ComponentResolverAdapterTest extends Specification {
    def result = Mock(BuildableComponentResolveResult)
    def requested = new DefaultModuleVersionSelector("group", "module", "version")
    def dependency = Stub(DependencyMetaData) {
        getRequested() >> requested
    }
    def metaDataResolver = Mock(ComponentMetaDataResolver)
    def id = Stub(ComponentIdentifier)
    def mvId = Stub(ModuleVersionIdentifier)
    def metaData = Stub(ComponentResolveMetaData)
    def idResolver = Mock(DependencyToComponentIdResolver)
    def resolver = new ComponentResolverAdapter(idResolver, metaDataResolver)

    def "resolves id and then meta-data"() {
        when:
        resolver.resolve(dependency, result)

        then:
        1 * idResolver.resolve(dependency, _) >> { DependencyMetaData dependency, BuildableComponentIdResolveResult idResult ->
            idResult.resolved(id, mvId)
        }
        1 * metaDataResolver.resolve(dependency, id, _) >> { DependencyMetaData dependency, ComponentIdentifier id, BuildableComponentResolveResult metaDataResult ->
            metaDataResult.resolved(metaData)
        }
        1 * result.resolved(metaData)
    }

    def "does not resolve meta-data when it is available during id resolution"() {
        when:
        resolver.resolve(dependency, result)

        then:
        1 * idResolver.resolve(dependency, _) >> { DependencyMetaData dependency, BuildableComponentIdResolveResult idResult ->
            idResult.resolved(metaData)
        }
        1 * result.resolved(metaData)
        0 * metaDataResolver._
    }

    def "propagates details of failed id resolve"() {
        def failure = new ModuleVersionResolveException(requested, "broken")

        when:
        resolver.resolve(dependency, result)

        then:
        1 * idResolver.resolve(dependency, _) >> { DependencyMetaData dependency, BuildableComponentIdResolveResult idResult ->
            idResult.attempted("a")
            idResult.failed(failure)
        }
        1 * result.attempted("a")
        1 * result.failed(failure)
        0 * metaDataResolver._
    }
}