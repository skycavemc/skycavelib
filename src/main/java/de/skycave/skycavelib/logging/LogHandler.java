package de.skycave.skycavelib.logging;

import de.skycave.skycavelib.models.SkyCavePlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogHandler extends Handler {

    private final SkyCavePlugin main;

    public LogHandler(SkyCavePlugin main) {
        this.main = main;
    }

    @Override
    public void publish(@NotNull LogRecord record) {
        final Throwable thrown = record.getThrown();
        if (record.getLevel() == Level.SEVERE) {
            if (thrown != null) {
                main.getRollbar().error(record.getThrown());
            } else {
                main.getRollbar().error(record.getMessage());
            }
        }
        if (record.getLevel() == Level.WARNING) {
            if (thrown != null) {
                main.getRollbar().warning(record.getThrown());
            } else {
                main.getRollbar().warning(record.getMessage());
            }
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {
        try {
            main.getRollbar().close(true);
        } catch (Exception e) {
            main.getRollbar().error(e);
        }
    }
}
