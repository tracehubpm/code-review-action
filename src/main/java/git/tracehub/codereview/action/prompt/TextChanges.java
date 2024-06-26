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
package git.tracehub.codereview.action.prompt;

import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.list.ListOf;

/**
 * Text changes.
 *
 * @since 0.1.22
 */
@RequiredArgsConstructor
public final class TextChanges implements Text {

    /**
     * Changes in JSON.
     */
    private final Scalar<JsonArray> changes;

    @Override
    public String asString() throws Exception {
        final List<String> composed = new ListOf<>();
        this.changes.value()
            .forEach(
                change -> {
                    final JsonObject json = change.asJsonObject();
                    composed.add(
                        String.join(
                            "\n",
                            String.format(
                                "filename: %s",
                                json.getString("filename")
                            ),
                            "patch:",
                            json.getString("patch"),
                            String.format(
                                "additions: %s",
                                json.getInt("additions")
                            ),
                            String.format(
                                "deletions: %s",
                                json.getInt("deletions")
                            ),
                            String.format(
                                "changes: %s",
                                json.getInt("changes")
                            )
                        )
                    );
                }
            );
        return composed.toString();
    }
}
