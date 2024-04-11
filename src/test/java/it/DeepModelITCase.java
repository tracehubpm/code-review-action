package it;

import com.yegor256.WeAreOnline;
import git.tracehub.codereview.action.deep.CompleteJson;
import git.tracehub.codereview.action.deep.DeepInfraRequest;
import git.tracehub.codereview.action.deep.DeepModel;
import git.tracehub.codereview.action.deep.Simple;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Integration test case for {@link git.tracehub.codereview.action.deep.DeepModel}.
 *
 * @since 0.0.0
 */
final class DeepModelITCase {

    @Test
    @Tag("simulation")
    @ExtendWith(WeAreOnline.class)
    void completes() throws Exception {
        final String token = System.getProperty("INPUT_DEEPINFRA_TOKEN");
        final String model = System.getProperty("INPUT_DEEPINFRA_MODEL");
        final String completion = new DeepModel(
            new DeepInfraRequest(
                token,
                new CompleteJson(
                    new Simple(
                        model,
                        1.0,
                        512
                    ),
                    "be helpful assistant",
                    "Hello, what is the tensor calculus?"
                )
            )
        ).completion();
        final String contain = "tensor";
        MatcherAssert.assertThat(
            String.format(
            "Completion (%s) does not contain (%s)",
                completion,
                contain
            ),
            completion.contains(contain),
            new IsEqual<>(true)
        );
    }
}
