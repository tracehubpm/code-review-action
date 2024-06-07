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

import git.tracehub.codereview.action.deep.CompleteJson;
import git.tracehub.codereview.action.deep.DeepInfraRequest;
import git.tracehub.codereview.action.deep.DeepModel;
import git.tracehub.codereview.action.deep.Simple;
import git.tracehub.codereview.action.openai.OpenAIModel;
import git.tracehub.codereview.action.openai.OpenJson;
import git.tracehub.codereview.action.openai.OpenRequest;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

/**
 * Auto model.
 * @since 0.2.0
 */
@RequiredArgsConstructor
public final class AutoModel implements Scalar<Model> {

    /**
     * Credential to use.
     */
    private final String credential;

    /**
     * Platform to interact with.
     */
    private final Scalar<String> platform;

    /**
     * System prompt.
     */
    private final String system;

    /**
     * User prompt.
     */
    private final String prompt;

    @Override
    public Model value() throws Exception {
        final String platf = this.platform.value();
        final Model model;
        if ("INPUT_DEEPINFRA_TOKEN".equals(platf)) {
            model = new DeepModel(
                new DeepInfraRequest(
                    this.credential,
                    new CompleteJson(
                        new Simple(
                            System.getenv().get("INPUT_DEEPINFRA_MODEL"),
                            0.7,
                            512
                        ),
                        this.system,
                        this.prompt
                    )
                )
            );
        } else {
            model = new OpenAIModel(
                new OpenRequest(
                    this.credential,
                    new OpenJson(
                        new Simple(
                            System.getenv().get("INPUT_OPENAI_MODEL"),
                            0.7,
                            512
                        ),
                        this.system,
                        this.prompt
                    )
                )
            );
        }
        return model;
    }
}
