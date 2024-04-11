package git.tracehub.codereview.action;

/**
 * Action's feedback
 *
 * @since 0.0.0
 */
public interface Feedback {

    /**
     * Deliver feedback.
     */
    void deliver() throws Exception;
}
