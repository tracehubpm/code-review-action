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

import com.jcabi.github.Pull;
import git.tracehub.codereview.action.extentions.PullFiles;
import git.tracehub.codereview.action.extentions.PullFilesExtension;
import git.tracehub.codereview.action.github.PullChanges;
import java.io.InputStreamReader;
import javax.json.Json;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Test case for {@link AnalysisPrompt}.
 *
 * @since 0.0.0
 */
final class AnalysisPromptTest {

    @Test
    @PullFiles("git/tracehub/codereview/action/github/small.json")
    @ExtendWith(PullFilesExtension.class)
    void compilesPrompt(final Pull mock) throws Exception {
        final String prompt = new AnalysisPrompt(
            new TextChanges(new PullChanges(mock)),
            "feat(#1): xsl changes",
            new TextReviews(
                Json.createReader(
                    new InputStreamReader(
                        new ResourceOf(
                            "git/tracehub/codereview/action/prompt/reviews.json"
                        ).stream()
                    )
                ).readArray()
            )
        ).asString();
        final String expected = String.join(
            "\n",
            "Please analyze how thorough the code review was.",
            "In the end of analysis suggest a review score, like \"excellent",
            " review\", \"poor review\" or something in the middle.",
            "Analysis must be just one readable paragraph in long.",
            "Pull Request: ",
            "PR title: feat(#1): xsl changes",
            "PR changes:",
            "[filename: eo-maven-plugin/src/main/resources/org/eolang/maven/pre/to-java.xsl",
            "patch:",
            "@@ -419,9 +419,6 @@ SOFTWARE.\n       <xsl:when test=\"$method='&amp;'\">\n         <xsl:text>σ</xsl:text>\n       </xsl:when>\n-      <xsl:when test=\"$method='&lt;'\">\n-        <xsl:text>ν</xsl:text>\n-      </xsl:when>\n       <xsl:otherwise>\n         <xsl:value-of select=\"eo:attr-name($method)\"/>\n       </xsl:otherwise>",
            "additions: 0",
            "deletions: 3",
            "changes: 3]",
            "Code review:",
            "Feedback: comment; Comments: [\"test1\", \"test2\", \"test3\"]",
            "Feedback: approve; Comments: [\"test4\"]",
            "Feedback: r; Comments: [\"test5\", \"test6\"]",
            ""
        );
        MatcherAssert.assertThat(
            String.format(
                "Compiled prompt (%s) does not match with expected (%s)",
                prompt,
                expected
            ),
            prompt,
            new IsEqual<>(expected)
        );
    }
}
