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

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ClearEnvironmentVariable;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;

/**
 * Test case for {@link Which}.
 *
 * @since 0.2.0
 */
final class WhichTest {

    @Test
    @SetEnvironmentVariable(key = "INPUT_DEEPINFRA_TOKEN", value = "testing")
    @ClearEnvironmentVariable(key = "INPUT_OPENAI_TOKEN")
    void returnsDeep() throws Exception {
        final String platform = new Which().value();
        final String expected = "INPUT_DEEPINFRA_TOKEN";
        MatcherAssert.assertThat(
            String.format(
                "Received platform '%s' does not match with expected '%s'",
                platform,
                expected
            ),
            platform,
            new IsEqual<>(expected)
        );
    }

    @Test
    @SetEnvironmentVariable(key = "INPUT_OPENAI_TOKEN", value = "testing")
    @ClearEnvironmentVariable(key = "INPUT_DEEPINFRA_TOKEN")
    void returnsOpen() throws Exception {
        final String platform = new Which().value();
        final String expected = "INPUT_OPENAI_TOKEN";
        MatcherAssert.assertThat(
            String.format(
                "Received platform '%s' does not match with expected '%s'",
                platform,
                expected
            ),
            platform,
            new IsEqual<>(expected)
        );
    }

    @Test
    @ClearEnvironmentVariable(key = "INPUT_OPENAI_TOKEN")
    @ClearEnvironmentVariable(key = "INPUT_DEEPINFRA_TOKEN")
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void raisesErrorIfEmptyEnv() {
        new Assertion<>(
            "Which does not throw exception, but should",
            () -> new Which().value(),
            new Throws<>(
                "You should provide either 'openai_token' or 'deepinfra_token'",
                IllegalStateException.class
            )
        ).affirm();
    }
}
