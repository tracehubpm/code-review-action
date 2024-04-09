package git.tracehub.codereview.action;

import com.jcabi.github.Pull;
import com.jcabi.log.Logger;
import git.tracehub.codereview.action.github.ChangesCount;
import lombok.RequiredArgsConstructor;
import org.cactoos.Proc;
import org.cactoos.Scalar;

/**
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class SkipIfTooSmall implements Proc<Pull> {

    private final Scalar<Integer> lines;
    private final Proc<Pull> routine;

    @Override
    public void exec(final Pull pull) throws Exception {
        final Integer min = this.lines.value();
        if (min != 0) {
            final int changes = new ChangesCount(pull).value();
            if (min > changes) {
                Logger.info(
                    Entry.class,
                    "Skipping pull request #%d since changes count %d less than min_lines %d",
                    pull.number(),
                    changes,
                    min
                );
            } else {
                this.routine.exec(pull);
            }
        }
        this.routine.exec(pull);
    }
}
