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

import java.util.Collections;
import java.util.List;

/**
 * Signals that the parsed input does not contain every header name declared through
 * {@link CSVFormat.Builder#setRequiredHeaders(String...)}.
 *
 * <p>
 * The parser throws this while initializing, before producing any {@link CSVRecord}, so callers never see a partially processed file.
 * </p>
 *
 * @since 1.15.0
 */
public class MissingRequiredHeadersException extends CSVException {

    private static final long serialVersionUID = 1L;

    /** Missing header names, in the order they were declared. */
    private final List<String> missingHeaders;

    /**
     * Constructs a new instance for the given missing header names.
     *
     * @param missingHeaders the declared header names absent from the input, in declaration order.
     */
    MissingRequiredHeadersException(final List<String> missingHeaders) {
        super("Missing required header(s): %s", String.join(", ", missingHeaders));
        this.missingHeaders = Collections.unmodifiableList(missingHeaders);
    }

    /**
     * Gets the declared header names absent from the input, in declaration order.
     *
     * @return an immutable list of the missing header names.
     */
    public List<String> getMissingHeaders() {
        return missingHeaders;
    }

}
