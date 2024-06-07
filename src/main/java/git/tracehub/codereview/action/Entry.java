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

import com.jcabi.github.Coordinates;
import com.jcabi.github.Pull;
import com.jcabi.github.RtGithub;
import com.jcabi.log.Logger;
import git.tracehub.codereview.action.github.EventPull;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Entry point.
 *
 * @since 0.0.0
 * @checkstyle HideUtilityClassConstructorCheck (10 lines)
 */
@SuppressWarnings("PMD.UseUtilityClass")
public final class Entry {

    /**
     * Application entry point.
     *
     * @param args Application arguments
     * @throws Exception if something went wrong.
     * @todo #51:45min Box SkipIfTooSmall.java into more major routine.
     *  SkipIfTooSmall.java is a good routine, but it should be encapsulated
     *  by something that logically is bigger than that. For now it can
     *  be not clear why at the end we run SkipIfTooSmall.java, not
     *  something like Analyze.java or similar.
     */
    public static void main(final String... args) throws Exception {
        final String name = System.getenv().get("GITHUB_REPOSITORY");
        final JsonObject event = Json.createReader(
            new StringReader(
                new String(
                    Files.readAllBytes(
                        Paths.get(
                            System.getenv().get("GITHUB_EVENT_PATH")
                        )
                    )
                )
            )
        ).readObject();
        final String approver = event.getJsonObject("sender").getString("login");
        Logger.info(Entry.class, "approver is %s", approver);
        Logger.info(Entry.class, "event received %s", event.toString());
        final String token = System.getenv().get("INPUT_GITHUB_TOKEN");
        final Pull.Smart pull = new Pull.Smart(
            new EventPull(
                new RtGithub(token).repos().get(new Coordinates.Simple(name)),
                event
            ).value()
        );
        final String title = pull.title();
        Logger.info(
            Entry.class,
            "pull request found: #%s '%s'",
            pull.number(),
            title
        );
        new SkipIfMentioned(
            new SkipAuthors(),
            new SkipIfTooSmall(
                new MinLines(),
                new AnalysisRoutine(
                    token,
                    approver,
                    new Which()
                )
            )
        ).exec(
            pull,
            new AutoRef().asString()
        );
    }
}
