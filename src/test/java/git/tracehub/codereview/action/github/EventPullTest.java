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

import com.jcabi.github.Pull;
import com.jcabi.github.Repo;
import com.jcabi.github.mock.MkGithub;
import javax.json.Json;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link EventPull}.
 *
 * @since 0.0.0
 */
final class EventPullTest {

    @Test
    void readsJsonPull() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final Pull created = repo.pulls().create("test", "master", "master");
        final int expected = created.number();
        final Pull pull = new EventPull(
            repo,
            Json.createObjectBuilder()
                .add(
                    "pull_request",
                    Json.createObjectBuilder()
                        .add("number", expected)
                        .build()
                )
                .build()
        ).value();
        final int number = pull.number();
        MatcherAssert.assertThat(
            String.format(
                "Received Pull Request number #%s (%s) does not match with expected (%s)",
                number,
                pull,
                expected
            ),
            number,
            new IsEqual<>(expected)
        );
    }
}
