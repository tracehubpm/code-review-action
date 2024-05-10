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

import com.jcabi.github.Pull;
import com.jcabi.github.mock.MkGithub;
import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link TextPull}.
 *
 * @since 0.1.24
 */
final class TextPullTest {

    @Test
    void transformsPullIntoText() throws Exception {
        final String title = "test";
        final Pull pull = new MkGithub().randomRepo()
            .pulls()
            .create(title, "testing", "master");
        new Pull.Smart(pull).title(title);
        final String changes = "some changes";
        final String txt = new TextPull(
            pull,
            new TextOf(changes)
        ).asString();
        final String expected = String.join(
            "\n",
            String.format("PR title: %s", title),
            "PR changes:",
            changes
        );
        MatcherAssert.assertThat(
            String.format(
                "Composed text pull '%s' for pull '%s' does not match with expected '%s'",
                txt,
                pull,
                expected
            ),
            txt,
            new IsEqual<>(expected)
        );
    }
}
