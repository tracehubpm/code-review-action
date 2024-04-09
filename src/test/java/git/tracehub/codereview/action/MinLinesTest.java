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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link MinLines}.
 *
 * @since 0.0.0
 */
final class MinLinesTest {

    /**
     * Returns provided min lines from environment.
     * @throws Exception if something went wrong
     * @todo #51:30min This test fails since there is no injected INPUT_MIN_LINES variable.
     *  During test execution we should set `INPUT_MIN_LINES` into system environment,
     *  so `MinLines.java` will fetch it and should return expected number of lines.
     *  Don't forget to remove this puzzle.
     */
    @Test
    @Disabled
    void returnsProvidedMinLines() throws Exception {
        final int lines = new MinLines().value();
        final int expected = 15;
        MatcherAssert.assertThat(
            String.format(
                "Provided min lines %d does not match with expected %d",
                lines,
                expected
            ),
            lines,
            new IsEqual<>(expected)
        );
    }

    @Test
    void returnsZeroWhenNotProvided() throws Exception {
        final int lines = new MinLines().value();
        final int expected = 0;
        MatcherAssert.assertThat(
            String.format(
                "Provided min lines %d does not match with expected %d",
                lines,
                expected
            ),
            lines,
            new IsEqual<>(expected)
        );
    }
}
