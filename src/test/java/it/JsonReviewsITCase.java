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
package it;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Pull;
import com.yegor256.WeAreOnline;
import git.tracehub.codereview.action.github.GhIdentity;
import git.tracehub.codereview.action.github.GhRequest;
import git.tracehub.codereview.action.github.JsonReviews;
import io.github.h1alexbel.ghquota.Quota;
import java.io.InputStreamReader;
import javax.json.Json;
import javax.json.JsonArray;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Integration test case for {@link JsonReviews}.
 *
 * @since 0.0.0
 */
final class JsonReviewsITCase {

    @ParameterizedTest
    @ValueSource(
        ints = {
            311,
            312,
            313
        }
    )
    @Tag("simulation")
    @ExtendWith({WeAreOnline.class, Quota.class})
    void fetchesReviews(final int pid) throws Exception {
        final Github github = new GhIdentity().value();
        final Pull pull = github.repos()
            .get(new Coordinates.Simple("h1alexbel/test"))
            .pulls()
            .get(pid);
        final JsonArray reviews = new JsonReviews(
            pull,
            new GhRequest(System.getProperty("INPUT_GITHUB_TOKEN"))
        ).value();
        final JsonArray expected = Json.createReader(
            new InputStreamReader(
                new ResourceOf(
                    String.format(
                        "it/%d-reviews.json",
                        pid
                    )
                ).stream()
            )
        ).readArray();
        MatcherAssert.assertThat(
            String.format(
                "Received reviews (%s) do not match with expected (%s)",
                reviews,
                expected
            ),
            reviews,
            new IsEqual<>(expected)
        );
    }
}
