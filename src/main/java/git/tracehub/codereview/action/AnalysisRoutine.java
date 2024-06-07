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
import git.tracehub.codereview.action.github.FeedbackInPull;
import git.tracehub.codereview.action.github.FixedReviews;
import git.tracehub.codereview.action.github.GhRequest;
import git.tracehub.codereview.action.github.JsonReviews;
import git.tracehub.codereview.action.github.PullChanges;
import git.tracehub.codereview.action.github.StartWithNickname;
import git.tracehub.codereview.action.github.WithComments;
import git.tracehub.codereview.action.github.WithScore;
import git.tracehub.codereview.action.prompt.AnalysisPrompt;
import git.tracehub.codereview.action.prompt.SuggestionPrompt;
import git.tracehub.codereview.action.prompt.SystemPrompt;
import git.tracehub.codereview.action.prompt.TextChanges;
import git.tracehub.codereview.action.prompt.TextPull;
import git.tracehub.codereview.action.prompt.TextReviews;
import java.util.Locale;
import javax.json.JsonArray;
import lombok.RequiredArgsConstructor;
import org.cactoos.BiProc;
import org.cactoos.Scalar;

/**
 * Analysis routine.
 *
 * @todo #88:90min Create integration tests with OpenAIModel.
 * We should create an integration test cases for OpenAI integration objects.
 * Let's do them the similar way as DeepModelITCase.java. Don't forget to
 * remove this puzzle.
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class AnalysisRoutine implements BiProc<Pull, String> {

    /**
     * GitHub token.
     */
    private final String github;

    /**
     * Approver.
     */
    private final String approver;

    /**
     * Platform token.
     */
    private final Scalar<String> platform;

    @Override
    public void exec(final Pull pull, final String model) throws Exception {
        final JsonArray reviews = new WithComments(
            new FixedReviews(new JsonReviews(pull, new GhRequest(this.github))),
            new GhRequest(this.github),
            pull
        ).value();
        Logger.info(this, "found reviews: %s", reviews);
        final String system = new SystemPrompt().asString();
        Logger.info(this, "compiled system prompt: %s", system);
        final String prompt = new AnalysisPrompt(
            new TextChanges(new PullChanges(pull)),
            new Pull.Smart(pull).title(),
            new TextReviews(reviews)
        ).asString();
        Logger.info(this, "compiled user prompt: %s", prompt);
        final String platf = this.platform.value();
        String credential = System.getenv().get(platf);
        if (credential == null) {
            credential = System.getProperty(platf);
        }
        final Model analysis = new AutoModel(
            credential, this.platform, system, prompt
        ).value();
        final String score = analysis.completion();
        if (score.toLowerCase(Locale.ROOT).contains("excellent")) {
            new FeedbackInPull(
                new StartWithNickname(
                    this.approver,
                    new AnalyzedWith(
                        score,
                        model
                    )
                ),
                pull
            ).deliver();
        } else {
            new FeedbackInPull(
                new StartWithNickname(
                    this.approver,
                    new WithScore(
                        new AnalyzedWith(
                            new AutoModel(
                                credential,
                                this.platform,
                                system,
                                new SuggestionPrompt(
                                    new TextReviews(reviews),
                                    new TextPull(
                                        pull,
                                        new TextChanges(
                                            new PullChanges(pull)
                                        )
                                    )
                                ).asString()
                            ).value().completion(),
                            model
                        ),
                        score
                    )
                ),
                pull
            ).deliver();
        }
    }
}
