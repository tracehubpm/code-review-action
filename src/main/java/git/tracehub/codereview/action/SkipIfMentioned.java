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
/*
 * @todo #101:45min Add integration test for pr.
 *  SkipIfMentioned should be applied on incoming pull request
 *  to check author on exclusion. It should be an integration test.
 */
package git.tracehub.codereview.action;

import com.jcabi.github.Pull;
import com.jcabi.log.Logger;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.cactoos.BiProc;
import org.cactoos.Scalar;

/**
 * Skip pull request if author is excluded.
 *
 * @since 0.1.23
 */
@RequiredArgsConstructor
public class SkipIfMentioned implements BiProc<Pull, String> {

    /**
     * Authors to skip.
     */
    private final Scalar<Collection<String>> mentions;

    /**
     * Routine to run.
     */
    private final BiProc<Pull, String> routine;

    @Override
    public final void exec(final Pull pull, final String param) throws Exception {
        final String author = new Pull.Smart(pull).author().login();
        if (this.mentions.value().contains(author)) {
            Logger.info(
                this,
                "Skipping pull request #%d, since author @%s is excluded",
                pull.number(),
                author
            );
        } else {
            this.routine.exec(pull, param);
        }
    }
}
