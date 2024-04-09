package git.tracehub.codereview.action;

import org.cactoos.Scalar;

/**
 * @since 0.0.0
 */
public final class MinLines implements Scalar<Integer> {

    private static final int NOT_PROVIDED = 0;

    @Override
    public Integer value() throws Exception {
        final int min;
        final String input = System.getenv().get("INPUT_MIN_LINES");
        if (input != null) {
            min = Integer.parseInt(input);
        } else {
            min = MinLines.NOT_PROVIDED;
        }
        return min;
    }
}
