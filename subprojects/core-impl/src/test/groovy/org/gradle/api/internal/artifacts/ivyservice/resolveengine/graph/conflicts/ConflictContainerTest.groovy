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

package org.gradle.api.internal.artifacts.ivyservice.resolveengine.graph.conflicts

import spock.lang.Specification
import spock.lang.Subject

class ConflictContainerTest extends Specification {

    @Subject container = new ConflictContainer<String, Integer>()

    def "contains few unconflicted elements"() {
        container.newElement("a", [1], null)
        container.newElement("b", [1], null)
        container.newElement("c", [1], "d")
        expect:
        container.size == 0
    }

    def "contains conflicting element"() {
        container.newElement("a", [1], null)
        container.newElement("b", [1, 2], null)
        expect:
        container.size == 1
        container.popConflict().toString() == "b:1,2"
    }

    def "contains multiple conflicting elements"() {
        container.newElement("a", [1, 2], null)
        container.newElement("b", [3, 4], null)
        expect:
        container.size == 2
        container.popConflict().toString() == "a:1,2"
        container.popConflict().toString() == "b:3,4"
    }

    def "pops conflicts orderly"() {
        container.newElement("c", [5, 6], null)
        container.newElement("b", [3, 4], null)
        container.newElement("a", [1, 2], null)
        expect:
        container.size == 3
        container.popConflict().toString() == "c:5,6"
        container.popConflict().toString() == "b:3,4"
        container.popConflict().toString() == "a:1,2"
    }

    def "replacement conflict"() {
        container.newElement("a", [1, 2], "b")
        container.newElement("b", [3, 4], null)
        expect:
        container.size == 1
        container.popConflict().toString() == "a,b:3,4"
    }

    def "replacement conflict reversed"() {
        container.newElement("b", [3, 4], null)
        container.newElement("a", [1, 2], "b")
        expect:
        container.size == 1
        container.popConflict().toString() == "a,b:3,4"
    }

    def "replacement and standard conflict"() {
        container.newElement("a", [1, 2], "b")
        container.newElement("b", [3], null)
        container.newElement("b", [3, 4], null)

        expect:
        container.size == 1
        container.popConflict().toString() == "a,b:3,4"
    }

    def "replacement and standard conflict reversed"() {
        container.newElement("b", [3], null)
        container.newElement("b", [3, 4], null)
        container.newElement("a", [1, 2], "b")

        expect:
        container.size == 1
        container.popConflict().toString() == "a,b:3,4"
    }

    def "replacement and standard conflict mixed"() {
        container.newElement("b", [3], null)
        container.newElement("a", [1, 2], "b")
        container.newElement("b", [3, 4], null)

        expect:
        container.size == 1
        container.popConflict().toString() == "a,b:3,4"
    }

    def "replacement and standard conflict interleaving"() {
        container.newElement("a", [1], null)
        container.newElement("b", [1, 2], null) //standard
        container.newElement("c", [3], "a") //module
        container.newElement("d", [4], null)

        expect:
        container.size == 2
        container.popConflict().toString() == "b:1,2"
        container.popConflict().toString() == "c,a:1"
    }
}
