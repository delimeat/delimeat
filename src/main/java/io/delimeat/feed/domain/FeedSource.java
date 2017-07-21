/*
 * Copyright 2013-2017 the original author or authors.
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
package io.delimeat.feed.domain;

public enum FeedSource {

    KAT(0),
    TORRENTPROJECT(1),
    LIMETORRENTS(2),
    EXTRATORRENT(3),
    BITSNOOP(4),
    TORRENTDOWNLOADS(5),
    ZOOQLE(6),
    PIRATEBAY(7),
    SKYTORRENTS(8);

    private final int value;

    private FeedSource(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
