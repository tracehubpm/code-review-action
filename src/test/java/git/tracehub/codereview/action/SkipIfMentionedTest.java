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
import com.jcabi.github.mock.MkGithub;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * Test suit for {@link SkipIfMentioned}.
 *
 * @since 0.1.23
 */
final class SkipIfMentionedTest {

    /**
     * Test pr.
     */
    private Pull pull;

    @BeforeEach
    void setUp() throws IOException {
        this.pull = new Pull.Smart(
            new MkGithub().randomRepo()
                .pulls()
                .create(
                    "test-title",
                    "test-head",
                    "test-base"
                )
        );
    }

    @Test
    void skipsWhenAuthorMentioned() throws Exception {
        final AtomicBoolean skipped = new AtomicBoolean(true);
        new SkipIfMentioned(
            new ListOf<>("jeff", "not-jeff"),
            ignored -> skipped.set(false)
        ).exec(this.pull);
        MatcherAssert.assertThat(
            "Skipped flag should be 'true', but was '%s'".formatted(skipped.get()),
            skipped.get(),
            new IsTrue()
        );
    }

    @Test
    void processesAuthorMentioned() throws Exception {
        final AtomicBoolean skipped = new AtomicBoolean(true);
        new SkipIfMentioned(
            Collections.emptyList(),
            ignored -> {
                Logger.info(this, "Processing pr...");
                skipped.set(false);
            }
        ).exec(this.pull);
        MatcherAssert.assertThat(
            "Skipped flag should be 'false', but was '%s'".formatted(skipped.get()),
            skipped.get(),
            new IsNot<>(
                new IsTrue()
            )
        );
    }
}
