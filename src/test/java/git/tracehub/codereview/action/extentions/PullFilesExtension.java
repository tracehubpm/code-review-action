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
package git.tracehub.codereview.action.extentions;

import com.jcabi.github.Pull;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.SneakyThrows;
import org.cactoos.io.ResourceOf;
import org.cactoos.list.ListOf;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.Mockito;

/**
 * Pull Files JUnit extension for mocking Pull#files().
 *
 * @since 0.0.0
 */
public final class PullFilesExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext pct, final ExtensionContext ect)
        throws ParameterResolutionException {
        return Objects.equals(pct.getParameter().getType(), Pull.class);
    }

    @SneakyThrows
    @Override
    public Object resolveParameter(final ParameterContext pct, final ExtensionContext ect)
        throws ParameterResolutionException {
        final Pull mock = Mockito.mock(Pull.class);
        final List<JsonObject> files = new ListOf<>();
        final PullFiles annotation = ect.getRequiredTestMethod()
            .getAnnotation(PullFiles.class);
        Json.createReader(
            new InputStreamReader(
                new ResourceOf(annotation.value()).stream()
            )
        ).readArray().forEach(value -> files.add(value.asJsonObject()));
        Mockito.when(mock.files()).thenReturn(files);
        return mock;
    }
}
