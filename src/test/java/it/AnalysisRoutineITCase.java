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

import com.jcabi.github.Comment;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Pull;
import com.yegor256.WeAreOnline;
import git.tracehub.codereview.action.AnalysisRoutine;
import git.tracehub.codereview.action.github.GhIdentity;
import io.github.h1alexbel.ghquota.Quota;
import java.time.Instant;
import java.util.Date;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * Integration test case for {@link AnalysisRoutine}.
 *
 * @since 0.1.23
 */
final class AnalysisRoutineITCase {

    @Tag("simulation")
    @ExtendWith({WeAreOnline.class, Quota.class})
    @Test
    void analyzesCodeReview() throws Exception {
        final String token = System.getProperty("INPUT_GITHUB_TOKEN");
        final String model = System.getProperty("INPUT_DEEPINFRA_MODEL");
        final Github github = new GhIdentity().value();
        final Pull pull = github.repos()
            .get(new Coordinates.Simple("tracehubpm/test"))
            .pulls()
            .get(1);
        final String approver = "h1alexbel";
        new AnalysisRoutine(
            token,
            approver,
            new Scalar<String>() {
                @Override
                public String value() throws Exception {
                    return "INPUT_DEEPINFRA_TOKEN";
                }
            }).exec(pull, model);
        final String posted = new Comment.Smart(
            new ListOf<>(
                new Pull.Smart(pull).issue()
                    .comments()
                    .iterate(new Date(Instant.now().toEpochMilli()))
            ).get(0)
        ).body();
        MatcherAssert.assertThat(
            String.format(
                "Posted feedback %s does not contain approver nickname %s, but should be",
                posted,
                approver
            ),
            posted.contains(approver),
            new IsTrue()
        );
        MatcherAssert.assertThat(
            String.format(
                "Posted feedback %s does not contain model tag, but should be",
                posted
            ),
            posted.contains(String.format("Analyzed with `%s`", model)),
            new IsTrue()
        );
    }
}
