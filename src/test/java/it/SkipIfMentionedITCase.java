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
import git.tracehub.codereview.action.AnalysisRoutine;
import git.tracehub.codereview.action.MinLines;
import git.tracehub.codereview.action.SkipAuthors;
import git.tracehub.codereview.action.SkipIfMentioned;
import git.tracehub.codereview.action.SkipIfTooSmall;
import git.tracehub.codereview.action.github.GhIdentity;
import io.github.h1alexbel.ghquota.Quota;
import java.util.List;
import nl.altindag.log.LogCaptor;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Integration test case for {@link SkipIfMentioned}.
 *
 * @since 0.2.0
 */
final class SkipIfMentionedITCase {

    @Test
    @Tag("simulation")
    @ExtendWith({WeAreOnline.class, Quota.class})
    void skipsAuthorPull() throws Exception {
        final LogCaptor capt = LogCaptor.forClass(SkipIfMentioned.class);
        final Github github = new GhIdentity().value();
        final Pull pull = github.repos()
            .get(new Coordinates.Simple("tracehubpm/test"))
            .pulls()
            .get(5);
        final String token = System.getProperty("INPUT_GITHUB_TOKEN");
        final String skip = "h1alexbel";
        new SkipIfMentioned(
            new SkipAuthors(skip),
            new SkipIfTooSmall(
                new MinLines(),
                new AnalysisRoutine(
                    token,
                    github.users().self().login(),
                    System.getenv().get("INPUT_DEEPINFRA_TOKEN")
                )
            )
        ).exec(
            pull,
            System.getenv().get("INPUT_DEEPINFRA_MODEL")
        );
        final List<String> logs = capt.getInfoLogs();
        final List<String> expected = new ListOf<>(
            String.format(
                "Skipping pull request #%d, since author @%s is excluded",
                pull.number(), skip
            )
        );
        MatcherAssert.assertThat(
            String.format(
                "Received logs (%s) do not match with expected (%s)",
                logs, expected
            ),
            logs,
            new IsEqual<>(expected)
        );
    }
}
