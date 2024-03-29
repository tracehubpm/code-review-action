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
package git.tracehub.codereview.action.github;

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
                        "git/tracehub/codereview/action/github/fake-reviews.json"
                    ).stream()
                )
            ).readArray()
        ).value();
        final List<Integer> expected = new ListOf<>(
            1_928_092_753,
            1_933_376_078,
            1_933_400_866,
            1_933_423_242,
            1_934_365_729,
            1_936_499_773,
            1_937_766_798,
            1_937_769_165,
            1_938_765_378,
            1_941_381_055,
            1_942_346_249,
            1_942_473_609,
            1_942_543_997,
            1_942_546_920,
            1_943_201_197,
            1_944_125_139,
            1_944_149_138,
            1_944_162_704,
            1_945_559_698,
            1_945_588_726,
            1_945_825_481,
            1_957_939_991,
            1_958_081_438,
            1_958_089_833,
            1_958_101_405,
            1_958_244_013,
            1_958_387_750,
            1_958_419_033,
            1_958_445_820,
            1_958_450_376
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
