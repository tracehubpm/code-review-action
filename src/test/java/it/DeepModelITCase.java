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
package it;

import com.yegor256.WeAreOnline;
import git.tracehub.codereview.action.deep.CompleteJson;
import git.tracehub.codereview.action.deep.DeepInfraRequest;
import git.tracehub.codereview.action.deep.DeepModel;
import git.tracehub.codereview.action.deep.Simple;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Integration test case for {@link git.tracehub.codereview.action.deep.DeepModel}.
 *
 * @since 0.0.0
 */
final class DeepModelITCase {

    @Test
    @Tag("simulation")
    @ExtendWith(WeAreOnline.class)
    void completes() throws Exception {
        final String token = System.getProperty("INPUT_DEEPINFRA_TOKEN");
        final String model = System.getProperty("INPUT_DEEPINFRA_MODEL");
        final String completion = new DeepModel(
            new DeepInfraRequest(
                token,
                new CompleteJson(
                    new Simple(
                        model,
                        1.0,
                        512
                    ),
                    "be helpful assistant",
                    "Hello, what is the tensor calculus?"
                )
            )
        ).completion();
        final String contain = "tensor";
        MatcherAssert.assertThat(
            String.format(
            "Completion (%s) does not contain (%s)",
                completion,
                contain
            ),
            completion.contains(contain),
            new IsEqual<>(true)
        );
    }
}
