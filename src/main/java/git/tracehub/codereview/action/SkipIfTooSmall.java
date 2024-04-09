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
import com.jcabi.log.Logger;
import git.tracehub.codereview.action.github.ChangesCount;
import lombok.RequiredArgsConstructor;
import org.cactoos.Proc;
import org.cactoos.Scalar;

/**
 * Skip pull request if it's too small.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class SkipIfTooSmall implements Proc<Pull> {

    /**
     * Min lines.
     */
    private final Scalar<Integer> lines;

    /**
     * Routine to run.
     */
    private final Proc<Pull> routine;

    @Override
    public void exec(final Pull pull) throws Exception {
        final Integer min = this.lines.value();
        if (min != 0) {
            final int changes = new ChangesCount(pull).value();
            if (min > changes) {
                Logger.info(
                    this,
                    "Skipping pull request #%d since changes count %d less than min_lines %d",
                    pull.number(),
                    changes,
                    min
                );
            } else {
                Logger.info(
                    this,
                    "Pull request #%d has enough number of lines (%d), min: %d",
                    pull.number(),
                    changes,
                    min
                );
                this.routine.exec(pull);
            }
        }
        this.routine.exec(pull);
    }
}
