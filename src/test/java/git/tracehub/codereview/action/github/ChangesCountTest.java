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
import java.io.InputStreamReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import org.cactoos.io.ResourceOf;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test case for {@link ChangesCount}.
 *
 * @since 0.0.0
 */
final class ChangesCountTest {

    @Test
    @ExtendWith(MockitoExtension.class)
    void calculatesChanges(@Mock final Pull mock) throws Exception {
        final List<JsonObject> files = new ListOf<>();
        Json.createReader(
            new InputStreamReader(
                new ResourceOf(
                    "git/tracehub/codereview/action/github/files.json"
                ).stream()
            )
        ).readArray().forEach(value -> files.add(value.asJsonObject()));
        Mockito.when(mock.files()).thenReturn(files);
        final int changes = new ChangesCount(mock).value();
        final int expected = 31;
        MatcherAssert.assertThat(
            String.format(
                "Changes count (%s) does not match with expected %s",
                changes,
                expected
            ),
            changes,
            new IsEqual<>(expected)
        );
    }
}
