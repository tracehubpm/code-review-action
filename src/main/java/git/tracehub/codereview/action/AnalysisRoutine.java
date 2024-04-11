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

import com.jcabi.github.Pull;
import com.jcabi.log.Logger;
import git.tracehub.codereview.action.deep.AnalyzedWith;
import git.tracehub.codereview.action.deep.CompleteJson;
import git.tracehub.codereview.action.deep.DeepInfraRequest;
import git.tracehub.codereview.action.deep.DeepModel;
import git.tracehub.codereview.action.deep.Simple;
import git.tracehub.codereview.action.github.FeedbackInPull;
import git.tracehub.codereview.action.github.FixedReviews;
import git.tracehub.codereview.action.github.GhRequest;
import git.tracehub.codereview.action.github.JsonReviews;
import git.tracehub.codereview.action.github.PullChanges;
import git.tracehub.codereview.action.github.StartWithNickname;
import git.tracehub.codereview.action.github.WithComments;
import git.tracehub.codereview.action.prompt.AnalysisPrompt;
import git.tracehub.codereview.action.prompt.SystemPrompt;
import javax.json.JsonArray;
import lombok.RequiredArgsConstructor;
import org.cactoos.Proc;

/**
 * Analysis routine.
 *
 * @since 0.0.0
 * @todo #60:30min Create integration test case for AnalysisRoutine.
 *  We should create integration test case for whole analysis routine.
 *  Profiles in maven are ready and packed with all necessary env variables.
 *  Let's create it and run on reasonable pull request in h1alexbel/test repo.
 *  Don't forget to remove this puzzle.
 */
@RequiredArgsConstructor
public final class AnalysisRoutine implements Proc<Pull> {

    /**
     * GitHub token.
     */
    private final String token;

    /**
     * Approver.
     */
    private final String approver;

    @Override
    public void exec(final Pull pull) throws Exception {
        final JsonArray reviews = new WithComments(
            new FixedReviews(new JsonReviews(pull, new GhRequest(this.token))),
            new GhRequest(this.token),
            pull
        ).value();
        Logger.info(this, "found reviews: %s", reviews);
        final String system = new SystemPrompt().asString();
        Logger.info(this, "compiled system prompt: %s", system);
        final String prompt = new AnalysisPrompt(
            new PullChanges(pull),
            new Pull.Smart(pull).title(),
            reviews
        ).asString();
        Logger.info(this, "compiled user prompt: %s", prompt);
        final String model = System.getenv().get("INPUT_DEEPINFRA_MODEL");
        new FeedbackInPull(
            new StartWithNickname(
                this.approver,
                new AnalyzedWith(
                    new DeepModel(
                        new DeepInfraRequest(
                            System.getenv().get("INPUT_DEEPINFRA_TOKEN"),
                            new CompleteJson(
                                new Simple(
                                    model,
                                    0.7,
                                    512
                                ),
                                system,
                                prompt
                            )
                        )
                    ),
                    model
                )
            ),
            pull
        ).deliver();
    }
}
