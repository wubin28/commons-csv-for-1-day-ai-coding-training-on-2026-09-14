/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.commons.csv;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Interface contracts for {@link CSVFormat.Builder#setRequiredHeaders(String...)}.
 *
 * <p>
 * Business facts about the validation itself live in the Approved Scenarios fixtures under {@code src/test/resources/approved}. What is pinned here are the API
 * contracts that are not business facts: default value, defensive copying, and value-object semantics.
 * </p>
 */
public class CSVFormatRequiredHeadersTest {

    @Test
    public void testDefaultIsEmptyAndDisablesTheCheck() {
        assertArrayEquals(new String[0], CSVFormat.DEFAULT.getRequiredHeaders());
    }

    @Test
    public void testGetterReturnsDefensiveCopy() {
        final CSVFormat format = CSVFormat.DEFAULT.builder().setRequiredHeaders("order_id", "amount").get();
        final String[] first = format.getRequiredHeaders();
        first[0] = "tampered";
        assertArrayEquals(new String[] { "order_id", "amount" }, format.getRequiredHeaders());
    }

    @Test
    public void testParticipatesInEqualsAndHashCode() {
        final CSVFormat a = CSVFormat.DEFAULT.builder().setRequiredHeaders("order_id").get();
        final CSVFormat b = CSVFormat.DEFAULT.builder().setRequiredHeaders("order_id").get();
        final CSVFormat c = CSVFormat.DEFAULT.builder().setRequiredHeaders("amount").get();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    public void testSurvivesBuilderRoundTrip() {
        final CSVFormat original = CSVFormat.DEFAULT.builder().setRequiredHeaders("order_id", "currency").get();
        final CSVFormat copy = original.builder().get();
        assertArrayEquals(original.getRequiredHeaders(), copy.getRequiredHeaders());
    }

    @Test
    public void testExposesMissingHeadersInDeclarationOrder() {
        final CSVFormat format = CSVFormat.DEFAULT.builder().setHeader().setRequiredHeaders("currency", "amount", "order_id").get();
        final MissingRequiredHeadersException e =
                assertThrows(MissingRequiredHeadersException.class, () -> CSVParser.parse("order_id\nA-1001\n", format).close());
        assertEquals(Arrays.asList("currency", "amount"), e.getMissingHeaders());
    }

    @Test
    public void testThrowsBeforeProducingAnyRecord() {
        final CSVFormat format = CSVFormat.DEFAULT.builder().setHeader().setRequiredHeaders("currency").get();
        assertThrows(MissingRequiredHeadersException.class, () -> {
            try (CSVParser parser = CSVParser.parse("order_id\nA-1001\n", format)) {
                throw new IOException("unreachable: the parser must fail while initializing");
            }
        });
    }
}
