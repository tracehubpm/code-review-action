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

import java.util.Collection;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ClearEnvironmentVariable;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

/**
 * Test suit for {@link SkipAuthors}.
 *
 * @since 0.1.23
 */
final class SkipAuthorsTest {

    @Test
    void providesAuthors() throws Exception {
        final Scalar<Collection<String>> authors = new SkipAuthors("ruby", "jeff");
        final Collection<String> expected = new ListOf<>("ruby", "jeff");
        MatcherAssert.assertThat(
            "%s should be equal to %s".formatted(authors.value(), expected),
            expected,
            Matchers.everyItem(
                Matchers.in(authors.value())
            )
        );
    }

    @Test
    @SetEnvironmentVariable(key = "INPUT_SKIP_AUTHORS", value = "[\"ruby\",\"jeff\"]")
    void providesAuthorsFromEnv() throws Exception {
        final Scalar<Collection<String>> authors = new SkipAuthors();
        final Collection<String> expected = new ListOf<>("ruby", "jeff");
        MatcherAssert.assertThat(
            "%s should be equal to %s".formatted(authors.value(), expected),
            expected,
            Matchers.everyItem(
                Matchers.in(
                    authors.value()
                )
            )
        );
    }

    @Test
    @ClearEnvironmentVariable(key = "INPUT_SKIP_AUTHORS")
    void ignoresWhenVariableNotSet() throws Exception {
        final Scalar<Collection<String>> authors = new SkipAuthors();
        MatcherAssert.assertThat(
            "%s should be empty".formatted(authors.value()),
            authors.value(),
            Matchers.empty()
        );
    }
}
