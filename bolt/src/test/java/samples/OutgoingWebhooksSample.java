package samples;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.middleware.builtin.LegacyRequestVerification;
import com.slack.api.bolt.middleware.builtin.SingleTeamAuthorization;
import samples.util.ResourceLoader;
import samples.util.TestSlackAppServer;

import java.util.Arrays;

public class OutgoingWebhooksSample {

    public static void main(String[] args) throws Exception {
        AppConfig config = ResourceLoader.loadAppConfig();
        // https://github.com/slackapi/java-slack-sdk/blob/master/bolt/src/test/resources
        String verificationToken = ResourceLoader.load("verificationToken.txt");
        App app = new App(config, Arrays.asList(
                // x-slack-signature unsupported
                new LegacyRequestVerification(verificationToken),
                new SingleTeamAuthorization(config, null)
        ));

        app.webhook("something", (req, ctx) -> {
            ctx.logger.info("outgoing webhook - {}", req);
            return ctx.ack("Hello " + req.getPayload().getUserName());
        });
        TestSlackAppServer server = new TestSlackAppServer(app);
        server.start();
    }

}
