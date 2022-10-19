package de.skycave.skycavelib;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.notifier.config.ConfigBuilder;
import de.skycave.skycavelib.models.SkyCavePlugin;

public final class SkyCaveLib extends SkyCavePlugin {

    private static Rollbar rollbar;

    @Override
    public void onEnable() {
        super.onEnable();
        Config config = ConfigBuilder.withAccessToken(System.getenv("ROLLBAR_ACCESS_TOKEN"))
                .environment("production")
                .codeVersion("1.3.0")
                .build();
        rollbar = Rollbar.init(config);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Rollbar remoteLogger() {
        return rollbar;
    }
}
