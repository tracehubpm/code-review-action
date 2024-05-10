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

import lombok.RequiredArgsConstructor;
import org.cactoos.Text;

/**
 * Suggestions prompt.
 *
 * @since 0.1.24
 */
@RequiredArgsConstructor
public final class SuggestionPrompt implements Text {

    /**
     * Code review.
     */
    private final Text review;

    /**
     * Pull request.
     */
    private final Text pull;

    @Override
    public String asString() throws Exception {
        return String.join(
            "\n",
            "Code review was not excellent, please post one suggestion for code reviewer",
            "what he/she can improve in the future.",
            "Start suggestion with text \"I would recommend ...\"",
            "Code review:",
            this.review.asString(),
            "Pull Request:",
            this.pull.asString()
        );
    }
}
