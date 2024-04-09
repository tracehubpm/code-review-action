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

import com.jcabi.github.Pull;
import com.jcabi.github.mock.MkGithub;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.json.Json;
import javax.json.JsonObject;
import nl.altindag.log.LogCaptor;
import org.cactoos.io.ResourceOf;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test case for {@link SkipIfTooSmall}.
 *
 * @since 0.0.0
 */
final class SkipIfTooSmallTest {

    @Test
    void skipsIfZero() throws Exception {
        final Pull created = new MkGithub().randomRepo().pulls()
            .create("test", "test", "master");
        final AtomicInteger increment = new AtomicInteger(1);
        new SkipIfTooSmall(
            () -> 0,
            pull -> increment.getAndIncrement()
        ).exec(created);
        final int expected = 2;
        MatcherAssert.assertThat(
            String.format(
                "Increment value (%d) does not match with expected (%d)",
                increment.get(),
                expected
            ),
            increment.get(),
            new IsEqual<>(expected)
        );
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void skipsSmallPullRequests(@Mock final Pull mock) throws Exception {
        final List<JsonObject> files = new ListOf<>();
        Json.createReader(
            new InputStreamReader(
                new ResourceOf(
                    "git/tracehub/codereview/action/github/small.json"
                ).stream()
            )
        ).readArray().forEach(value -> files.add(value.asJsonObject()));
        Mockito.when(mock.files()).thenReturn(files);
        final LogCaptor capt = LogCaptor.forClass(SkipIfTooSmall.class);
        new SkipIfTooSmall(
            () -> 15,
            pull -> {
            }
        ).exec(mock);
        final List<String> infos = capt.getInfoLogs();
        final List<String> expected = new ListOf<>(
            "Skipping pull request #0 since changes count 3 less than min_lines 15"
        );
        MatcherAssert.assertThat(
            String.format(
                "Received logs (%s) do not match with expected (%s)",
                infos,
                expected
            ),
            infos,
            new IsEqual<>(expected)
        );
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void doesNotSkipEnoughPullRequest(@Mock final Pull mock) throws Exception {
        final List<JsonObject> files = new ListOf<>();
        Json.createReader(
            new InputStreamReader(
                new ResourceOf(
                    "git/tracehub/codereview/action/github/files.json"
                ).stream()
            )
        ).readArray().forEach(value -> files.add(value.asJsonObject()));
        Mockito.when(mock.files()).thenReturn(files);
        final LogCaptor capt = LogCaptor.forClass(SkipIfTooSmall.class);
        new SkipIfTooSmall(
            () -> 15,
            pull -> {
            }
        ).exec(mock);
        final List<String> infos = capt.getInfoLogs();
        final List<String> expected = new ListOf<>(
            "Pull request #0 has enough number of lines (31), min: 15"
        );
        MatcherAssert.assertThat(
            String.format(
                "Received logs (%s) do not match with expected (%s)",
                infos,
                expected
            ),
            infos,
            new IsEqual<>(expected)
        );
    }
}
