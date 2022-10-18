package de.skycave.skycavelib;

import de.skycave.skycavelib.logging.LogHandler;
import de.skycave.skycavelib.models.SkyCavePlugin;

public final class SkyCaveLib extends SkyCavePlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().addHandler(new LogHandler(this));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
