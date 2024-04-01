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
import com.jcabi.github.Repo;
import java.io.IOException;
import javax.json.JsonObject;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

/**
 * Pull request on GitHub in JSON.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class PullRequest implements Scalar<Pull> {

    /**
     * Repo.
     */
    private final Repo repo;

    /**
     * JSON event.
     */
    private final JsonObject event;

    @Override
    public Pull value() throws IOException {
        return this.repo.pulls().get(
            this.event.getJsonObject("pull_request").getInt("number")
        );
    }
}
