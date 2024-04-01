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
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;
import com.jcabi.log.Logger;
import git.tracehub.codereview.action.github.Authored;
import git.tracehub.codereview.action.github.GhRequest;
import git.tracehub.codereview.action.github.JsonComments;
import git.tracehub.codereview.action.github.PullRequest;
import git.tracehub.codereview.action.github.ReviewIds;
import git.tracehub.codereview.action.github.Reviews;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
     * @todo #2:25min Implement GhIdentity.
     *  For now we do create RtGithub right here, in the main method.
     *  Let's implement some sort of "smart" identity that based on
     *  INPUT_GITHUBTOKEN variable presence inside the environment will
     *  create corresponding object, either RtGithub (real github instance)
     *  or MkGithub (mocked github server in memory xml).
     * @todo #2:30min Develop a prompt to the language model.
     *  After information is collected (pull request itself, its files,
     *  and reviews) we can feed it into the model asking what is the quality
     *  of the following code review.
     * @todo #2:30min Fetch all the info about pull request we are working with.
     *  We should fetch all useful information about pull request that we are dealing with:
     *  title, files (changes), and its author. Let's present this data in JSON/XML format.
     * @todo #2:30min Formulate action stoppers.
     *  We should formulate some action stoppers that would not "go further"
     *  into processing if: pull request is too small (we need a specific number),
     *  or some other alerts that would be reasonable.
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
        Logger.info(Entry.class, "event received %s", event.toString());
        final String token = System.getenv().get("INPUT_GITHUB_TOKEN");
        final Repo repo = new RtGithub(token)
            .repos()
            .get(new Coordinates.Simple(name));
        final Pull pull = new PullRequest(repo, event).value();
        Logger.info(Entry.class, "pull request found: %s", pull);
        new ReviewIds(new Reviews(pull, new GhRequest(token))).value()
            .forEach(
                review -> {
                    final List<String> comments = new Authored(
                        new JsonComments(new GhRequest(token), pull, review)
                    ).value();
                    Logger.info(
                        Entry.class,
                        "found comments for review #%s: %s",
                        review,
                        comments
                    );
                }
            );
    }
}
