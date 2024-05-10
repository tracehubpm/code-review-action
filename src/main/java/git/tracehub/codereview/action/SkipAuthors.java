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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;

/**
 * SkipAuthors system environment variable.
 *
 * @since 0.1.23
 */
public final class SkipAuthors implements Scalar<Collection<String>> {

    /**
     * Regex to match '[' ']' and '"' chars.
     */
    private static final Pattern ARRAY_TOKENS = Pattern.compile("[\\[\\]\"]");

    /**
     * Authors to skip.
     */
    private final List<String> authors;

    /**
     * Primary ctor.
     *
     * @param skip Authors which will be skipped.
     */
    SkipAuthors(final List<String> skip) {
        this.authors = skip;
    }

    /**
     * Vararg ctor.
     *
     * @param skip Authors which will be skipped.
     */
    SkipAuthors(final String... skip) {
        this(new ListOf<>(skip));
    }

    @Override
    public Collection<String> value() {
        final Collection<String> result;
        if (this.authors.isEmpty()) {
            final String env = System.getenv().get("INPUT_SKIP_AUTHORS");
            if (env == null) {
                result = Collections.emptyList();
            } else {
                result = new ListOf<>(
                    SkipAuthors.ARRAY_TOKENS
                        .matcher(env)
                        .replaceAll("")
                        .split(",")
                );
            }
        } else {
            result = Collections.unmodifiableCollection(this.authors);
        }
        return result;
    }
}
