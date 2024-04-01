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
package git.tracehub.codereview.action.github;

import com.jcabi.github.Pull;
import com.jcabi.http.Request;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Scalar;

/**
 * All review comments, presented in JSON format.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class JsonComments implements Comments {

    /**
     * Request.
     */
    private final Scalar<Request> request;

    /**
     * Pull request.
     */
    private final Pull pull;

    /**
     * Review ID.
     */
    private final int review;

    @Override
    @SneakyThrows
    public JsonArray value() {
        return Json.createReader(
            new StringReader(
                this.request.value()
                    .uri()
                    .path(
                        String.format(
                            "repos/%s/%s/pulls/%s/reviews/%s/comments",
                            this.pull.repo().coordinates().user(),
                            this.pull.repo().coordinates().repo(),
                            this.pull.number(),
                            this.review
                        )
                    )
                    .back()
                    .fetch()
                    .body()
            )
        ).readArray();
    }
}
