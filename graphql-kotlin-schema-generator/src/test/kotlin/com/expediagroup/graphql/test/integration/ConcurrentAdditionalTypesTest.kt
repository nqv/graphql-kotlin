/*
 * Copyright 2020 Expedia, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.expediagroup.graphql.test.integration

import com.expediagroup.graphql.TopLevelObject
import com.expediagroup.graphql.testSchemaConfig
import com.expediagroup.graphql.toSchema
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class ConcurrentAdditionalTypesTest {

    @Test
    fun `verify a concurrent exception is not thrown if there are additionalTypes added when generating the additionalTypes`() {
        val queries = listOf(TopLevelObject(SimpleQuery()))
        val schema = toSchema(testSchemaConfig, queries)
        assertNotNull(schema)
        assertNotNull(schema.getType("InterfaceOne"))
        assertNotNull(schema.getType("InterfaceTwo"))
        assertNotNull(schema.getType("ClassOneA"))
        assertNotNull(schema.getType("ClassOneB"))
        assertNotNull(schema.getType("ClassTwoA"))
        assertNotNull(schema.getType("ClassTwoB"))
    }

    class SimpleQuery {
        fun getClassOne(): InterfaceOne = ClassOneB("foo")
    }

    interface InterfaceOne {
        val value: String
    }

    interface InterfaceTwo {
        val value: String
    }

    class ClassOneA(
        override val value: String,
        val interfaceField: InterfaceTwo
    ) : InterfaceOne

    class ClassOneB(override val value: String) : InterfaceOne

    class ClassTwoA(override val value: String) : InterfaceTwo

    class ClassTwoB(override val value: String) : InterfaceTwo
}