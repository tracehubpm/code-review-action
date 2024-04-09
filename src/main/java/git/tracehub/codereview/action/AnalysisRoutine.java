package git.tracehub.codereview.action;

import com.jcabi.github.Pull;
import com.jcabi.log.Logger;
import git.tracehub.codereview.action.github.FixedReviews;
import git.tracehub.codereview.action.github.GhRequest;
import git.tracehub.codereview.action.github.JsonReviews;
import git.tracehub.codereview.action.github.WithComments;
import javax.json.JsonArray;
import lombok.RequiredArgsConstructor;
import org.cactoos.Proc;

/**
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class AnalysisRoutine implements Proc<Pull> {

    /**
     * GitHub token.
     */
    private final String token;

    @Override
    public void exec(final Pull pull) throws Exception {
        final JsonArray reviews = new WithComments(
            new FixedReviews(new JsonReviews(pull, new GhRequest(this.token))),
            new GhRequest(this.token),
            pull
        ).value();
        Logger.info(Entry.class, "found reviews: %s", reviews);

    }
}
