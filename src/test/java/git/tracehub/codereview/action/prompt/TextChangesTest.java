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
 * Test case for {@link TextChanges}.
 *
 * @since 0.1.22
 */
final class TextChangesTest {

    @Test
    void transformsChangesIntoText() throws Exception {
        final String text = new TextChanges(
            () -> Json.createReader(
                new InputStreamReader(
                    new ResourceOf(
                        "git/tracehub/codereview/action/github/compiled.json"
                    ).stream()
                )
            ).readArray()
        ).asString();
        final String expected = String.join(
            "\n",
            "[filename: eo-maven-plugin/src/main/resources/org/eolang/maven/pre/to-java.xsl",
            "patch:",
            "@@ -419,9 +419,6 @@ SOFTWARE.\n       <xsl:when test=\"$method='&amp;'\">\n         <xsl:text>σ</xsl:text>\n       </xsl:when>\n-      <xsl:when test=\"$method='&lt;'\">\n-        <xsl:text>ν</xsl:text>\n-      </xsl:when>\n       <xsl:otherwise>\n         <xsl:value-of select=\"eo:attr-name($method)\"/>\n       </xsl:otherwise>",
            "additions: 0",
            "deletions: 3",
            "changes: 3]"
        );
        MatcherAssert.assertThat(
            String.format(
                "Output text (%s) does not match with expected (%s)",
                text,
                expected
            ),
            text,
            new IsEqual<>(expected)
        );
    }

    @Test
    @PullFiles("git/tracehub/codereview/action/github/compiled-array.json")
    @ExtendWith(PullFilesExtension.class)
    void transformsWithGitHub(final Pull mock) throws Exception {
        final String text = new TextChanges(new PullChanges(mock))
            .asString().trim();
        final String expected = String.join(
            "\n",
            "[filename: eo-maven-plugin/src/main/resources/org/eolang/maven/pre/to-java.xsl",
            "patch:",
            "@@ -419,9 +419,6 @@ SOFTWARE.",
            "       <xsl:when test=\"$method='&amp;'\">",
            "         <xsl:text>σ</xsl:text>",
            "       </xsl:when>",
            "-      <xsl:when test=\"$method='&lt;'\">",
            "-        <xsl:text>ν</xsl:text>",
            "-      </xsl:when>",
            "       <xsl:otherwise>",
            "         <xsl:value-of select=\"eo:attr-name($method)\"/>",
            "       </xsl:otherwise>",
            "additions: 0",
            "deletions: 3",
            "changes: 3, filename: eo-parser/src/test/resources/org/eolang/parser/packs/add-locators.yaml",
            "patch:",
            "@@ -12,11 +12,10 @@ tests:",
            "   - //o[not(@base) and @name='e' and @loc='Φ.org.abc.tt.α2.e']",
            "   - //o[@base='.hello' and @loc='Φ.org.abc.tt.α2.φ']",
            "   - //o[@base='e' and @loc='Φ.org.abc.tt.α2.φ.ρ']",
            "-  - //o[@name='q' and @base='.<' and @loc='Φ.org.abc.q']",
            "-  - //o[@base='.p' and not(@name) and @loc='Φ.org.abc.q.ρ']",
            "-  - //o[@base='.^' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ']",
            "-  - //o[@base='.&' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ.ρ']",
            "-  - //o[@base='$' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ.ρ.ρ']",
            "+  - //o[@name='q' and @base='.p' and @loc='Φ.org.abc.q']",
            "+  - //o[@base='.^' and not(@name) and @loc='Φ.org.abc.q.ρ']",
            "+  - //o[@base='.&' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ']",
            "+  - //o[@base='$' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ.ρ']",
            " eo: |",
            "   +alias org.abc.foo.b",
            "   +alias x",
            "@@ -38,4 +37,4 @@ eo: |",
            "     [e]",
            "       e.hello > @",
            "   ",
            "-  $.&.^.p.< > q",
            "+  $.&.^.p > q",
            "additions: 5",
            "deletions: 6",
            "changes: 11, filename: eo-parser/src/test/resources/org/eolang/parser/packs/full-syntax.yaml",
            "patch:",
            "@@ -10,7 +10,6 @@ tests:",
            "   - //o[@as='1']",
            "   - //o[@as='0']",
            "   - //o[@base='&']",
            "-  - //o[@base='.<']",
            "   - //o[@base='.five']",
            "   - //objects[not(.//o[@name=''])]",
            "   - //o[@atom and @name='atom' and count(o)=2 and o[@name='a']]",
            "@@ -37,20 +36,20 @@ eo: |",
            "   [x] > first",
            "     x > @",
            "     second > hello",
            "-      $.plus.@ 5.< > i",
            "+      $.plus.@ 5 > i",
            "       third > x!",
            "         $",
            "-        <.",
            "+        z.",
            "           z",
            "         f",
            "           12:foo",
            "-          ((t' r 8.54 \"yes\".< \"\\t\").print 88 0x1f):hey",
            "-          TRUE.<:vtx",
            "+          ((t' r 8.54 \"yes\" \"\\t\").print 88 0x1f):hey",
            "+          TRUE:vtx",
            "           false:fle > a!",
            "             []",
            "               Q.x.f.d Q Q",
            "               QQ.y QQ",
            "-              &.@.< > t",
            "+              &.@ > t",
            "               ^.@.hey > you",
            "               Q",
            "               QQ",
            "@@ -91,7 +90,7 @@ eo: |",
            "   # This is the default 64+ symbols comment in front of abstract object.",
            "   [] > named",
            "     one.two.three.four.five",
            "-      t.<",
            "+      t.o",
            "     .two \"hello!\"",
            "     .three > a1",
            "     .four (a b c') > a2",
            "additions: 6",
            "deletions: 7",
            "changes: 13, filename: eo-runtime/src/test/java/org/eolang/PhDefaultTest.java",
            "patch:",
            "@@ -274,8 +274,8 @@ void hasContextedChildWithSetRhoWhenFormed() {",
            "     void makesObjectIdentity() {",
            "         final Phi phi = new PhDefaultTest.Int();",
            "         MatcherAssert.assertThat(",
            "-            new Dataized(phi.take(Attr.VERTEX)).take(Long.class),",
            "-            Matchers.greaterThan(0L)",
            "+            phi.hashCode(),",
            "+            Matchers.greaterThan(0)",
            "         );",
            "     }",
            " ",
            "additions: 2",
            "deletions: 2",
            "changes: 4]"
        ).trim();
        MatcherAssert.assertThat(
            String.format(
                "Output text (%s) does not match with expected (%s)",
                text,
                expected
            ),
            text,
            new IsEqual<>(expected)
        );
    }
}
