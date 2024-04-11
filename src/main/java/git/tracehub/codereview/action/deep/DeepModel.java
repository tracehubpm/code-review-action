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

import com.jcabi.http.Request;
import com.jcabi.log.Logger;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

/**
 * Deep infra model.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class DeepModel implements Model {

    /**
     * Request.
     */
    private final Scalar<Request> request;

    @Override
    public String completion() throws Exception {
        final JsonObject response = Json.createReader(
            new StringReader(
                this.request.value()
                    .fetch()
                    .body()
            )
        ).readObject();
        final String completion = response.getJsonArray("choices")
            .get(0)
            .asJsonObject()
            .getJsonObject("message")
            .getString("content");
        final JsonObject usage = response.getJsonObject("usage");
        Logger.info(
            this,
            "Completion result: %s",
            completion
        );
        Logger.info(this, "Prompt tokens spent: %s", usage.getInt("prompt_tokens"));
        Logger.info(this, "Completion tokens spent: %s", usage.getInt("completion_tokens"));
        Logger.info(this, "Total tokens spent: %s", usage.getInt("total_tokens"));
        return completion;
    }
}
