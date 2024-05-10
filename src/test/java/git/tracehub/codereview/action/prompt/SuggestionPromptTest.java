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

import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link SuggestionPrompt}.
 *
 * @since 0.1.24
 */
final class SuggestionPromptTest {

    @Test
    void compilesSuggestionPrompt() throws Exception {
        final String review = "testing";
        final String pull = "testing pull";
        final String prompt = new SuggestionPrompt(
            new TextOf(review),
            new TextOf(pull)
        ).asString();
        final String expected = String.join(
            "\n",
            "Code review was not excellent, please post one suggestion for code reviewer",
            "what he/she can improve in the future.",
            "Start suggestion with text \"I would recommend ...\"",
            "Code review:",
            review,
            "Pull Request:",
            pull
        );
        MatcherAssert.assertThat(
            String.format(
                "Compiled prompt '%s' does not match with expected '%s'",
                prompt,
                expected
            ),
            prompt,
            new IsEqual<>(expected)
        );
    }
}
