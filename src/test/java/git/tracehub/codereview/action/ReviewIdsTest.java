/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 Tracehub.git
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package git.tracehub.codereview.action;

import java.io.InputStreamReader;
import java.util.List;
import javax.json.Json;
import org.cactoos.io.ResourceOf;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ReviewIds}.
 *
 * @since 0.0.0
 */
final class ReviewIdsTest {

    @Test
    void collectsAllIds() throws Exception {
        final List<Integer> identifiers = new ReviewIds(
            () -> Json.createReader(
                new InputStreamReader(
                    new ResourceOf(
                        "git/tracehub/codereview/action/fake-reviews.json"
                    ).stream()
                )
            ).readArray()
        ).value();
        final List<Integer> expected = new ListOf<>(
            1928092753,
            1933376078,
            1933400866,
            1933423242,
            1934365729,
            1936499773,
            1937766798,
            1937769165,
            1938765378,
            1941381055,
            1942346249,
            1942473609,
            1942543997,
            1942546920,
            1943201197,
            1944125139,
            1944149138,
            1944162704,
            1945559698,
            1945588726,
            1945825481,
            1957939991,
            1958081438,
            1958089833,
            1958101405,
            1958244013,
            1958387750,
            1958419033,
            1958445820,
            1958450376
        );
        MatcherAssert.assertThat(
            String.format(
                "Collected identifiers (%s) do not match with expected (%s)",
                identifiers,
                expected
            ),
            identifiers,
            new IsEqual<>(expected)
        );
    }
}
