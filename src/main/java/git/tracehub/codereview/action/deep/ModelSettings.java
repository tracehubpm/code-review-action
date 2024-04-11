package git.tracehub.codereview.action.deep;

/**
 * @since 0.0.0
 */
public interface ModelSettings {

    String model();

    double temperature();

    int maxNewTokens();
}
