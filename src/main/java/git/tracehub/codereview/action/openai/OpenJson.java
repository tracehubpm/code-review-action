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
package git.tracehub.codereview.action.openai;

import git.tracehub.codereview.action.deep.ModelSettings;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

/**
 * JSON request to OpenAI.
 *
 * @since 0.2.0
 */
@RequiredArgsConstructor
public final class OpenJson implements Scalar<JsonObject> {

    /**
     * Model settings.
     */
    private final ModelSettings settings;

    /**
     * System prompt.
     */
    private final String system;

    /**
     * User prompt.
     */
    private final String prompt;

    @Override
    public JsonObject value() throws Exception {
        return Json.createObjectBuilder()
            .add("model", this.settings.model())
            .add("temperature", this.settings.temperature())
            .add("max_tokens", this.settings.maxNewTokens())
            .add(
                "messages",
                Json.createArrayBuilder()
                    .add(
                        Json.createObjectBuilder()
                            .add("role", "system")
                            .add("content", this.system)
                            .build()
                    ).add(
                        Json.createObjectBuilder()
                            .add("role", "user")
                            .add("content", this.prompt)
                            .build()
                    ).build()
            ).build();
    }
}
