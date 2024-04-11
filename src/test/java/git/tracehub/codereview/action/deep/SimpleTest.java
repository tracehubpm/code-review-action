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
package git.tracehub.codereview.action.deep;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Simple}.
 *
 * @since 0.0.0
 */
final class SimpleTest {

    @Test
    void returnsModelName() {
        final String expected = "gpt-4";
        final String model = new Simple(expected, 1.0, 512).model();
        MatcherAssert.assertThat(
            String.format(
                "Model name %s does not match with expected %s",
                model,
                expected
            ),
            model,
            new IsEqual<>(expected)
        );
    }

    @Test
    void returnsTemperature() {
        final double expected = 0.7;
        final double temperature = new Simple("test", expected, 1024)
            .temperature();
        MatcherAssert.assertThat(
            String.format(
                "Temperature %s does not match with expected %s",
                temperature,
                expected
            ),
            temperature,
            new IsEqual<>(expected)
        );
    }

    @Test
    void returnsMaxNewTokens() {
        final int expected = 256;
        final int tokens = new Simple("test", 1.0, expected).maxNewTokens();
        MatcherAssert.assertThat(
            String.format(
                "Max new tokens %s does not match with expected %s",
                tokens,
                expected
            ),
            tokens,
            new IsEqual<>(expected)
        );
    }
}
