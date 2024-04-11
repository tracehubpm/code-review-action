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
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link DeepInfraRequest}.
 *
 * @since 0.0.0
 */
final class DeepInfraRequestTest {

    @Test
    void compilesRequest() throws Exception {
        final Request request = new DeepInfraRequest(
            "test",
            new CompleteJson(
                new Simple("Phind/Phind-CodeLlama-34B-v2", 0.7, 1024),
                "system test",
                "user test"
            )
        ).value();
        final JsonObject body = Json.createReader(
            new StringReader(request.body().get())
        ).readObject();
        final JsonObject expected = Json.createReader(
            new InputStreamReader(
                new ResourceOf(
                    "git/tracehub/codereview/action/deep/body.json"
                ).stream()
            )
        ).readObject();
        MatcherAssert.assertThat(
            String.format(
                "Request body (%s) does not match with expected (%s)",
                body,
                expected
            ),
            body,
            new IsEqual<>(expected)
        );
    }

    @Test
    // @checkstyle StringLiteralsConcatenationCheck (20 lines)
    void checksFullHttpRequest() throws Exception {
        final Request request = new DeepInfraRequest(
            "test",
            new CompleteJson(
                new Simple("Phind/Phind-CodeLlama-34B-v2", 1.0, 1024),
                "system...",
                "user..."
            )
        ).value();
        final String http = request.toString();
        final String expected =
            "HTTP/1.1 POST /v1/openai/chat/completions (api.deepinfra.com)\n"
            + "Authorization: Bearer test\nContent-Type: application/json\n\n"
            + "{\"model\":\"Phind/Phind-CodeLlama-34B-v2\",\"temperature\":1.0"
            + ",\"max_new_tokens\":1024,\"messages\":[{\"role\":\"system\",\"content"
            + "\":\"system...\"},{\"role\":\"user\",\"content\":\"user...\"}]}";
        MatcherAssert.assertThat(
            String.format(
                "Compiled HTTP request (%s) does not match with expected (%s)",
                http,
                expected
            ),
            http,
            new IsEqual<>(expected)
        );
    }
}
