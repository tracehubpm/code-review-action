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
import javax.json.JsonArray;
import javax.json.JsonObject;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

/**
 * Reviews with comments.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class WithComments implements Scalar<JsonArray> {

    /**
     * Origin.
     */
    private final Scalar<JsonArray> origin;

    /**
     * Request.
     */
    private final Scalar<Request> request;

    /**
     * Pull request.
     */
    private final Pull pull;

    @Override
    public JsonArray value() throws Exception {
        final JsonArray base = this.origin.value();
        base.forEach(
            review -> {
                final JsonObject json = review.asJsonObject();
                final int identifier = json.getInt("id");
                json.put(
                    "comments",
                    new Authored(
                        new JsonComments(
                            this.request,
                            this.pull,
                            identifier
                        )
                    ).value()
                );
            }
        );
        return base;
    }
}
