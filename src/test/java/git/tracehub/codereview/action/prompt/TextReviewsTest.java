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
package git.tracehub.codereview.action.prompt;

import java.io.InputStreamReader;
import javax.json.Json;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link TextReviews}.
 *
 * @since 0.1.22
 */
final class TextReviewsTest {

    @Test
    void transformsReviewIntoText() throws Exception {
        final String text = new TextReviews(
            Json.createArrayBuilder()
                .add(
                    Json.createObjectBuilder()
                        .add("submitted", "just one")
                        .add(
                            "comments",
                            Json.createArrayBuilder()
                                .add("one comment")
                                .build()
                        ).build()
                ).build()
        ).asString();
        final String expected = "Feedback: just one; Comments: [\"one comment\"]\n";
        MatcherAssert.assertThat(
            String.format(
                "Output text (%s) does not match with expected (%s)",
                text,
                expected
            ),
            text,
            new IsEqual<>(expected)
        );
    }

    @Test
    void transformsMultipleReviewsIntoText() throws Exception {
        final String text = new TextReviews(
            Json.createReader(
                new InputStreamReader(
                    new ResourceOf(
                        "git/tracehub/codereview/action/prompt/reviews.json"
                    ).stream()
                )
            ).readArray()
        ).asString();
        final String expected = String.join(
            "\n",
            "Feedback: comment; Comments: [\"test1\", \"test2\", \"test3\"]",
            "Feedback: approve; Comments: [\"test4\"]",
            "Feedback: r; Comments: [\"test5\", \"test6\"]",
            ""
        );
        MatcherAssert.assertThat(
            String.format(
                "Output text (%s) does not match with expected (%s)",
                text,
                expected
            ),
            text,
            new IsEqual<>(expected)
        );
    }
}
