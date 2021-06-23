package dev.projectg.geyserhub;

import dev.projectg.geyserhub.reloadable.Reloadable;
import dev.projectg.geyserhub.reloadable.ReloadableRegistry;

public class SelectorLogger implements Reloadable {

    private static final SelectorLogger LOGGER = new SelectorLogger(GeyserHubMain.getInstance());

    private final GeyserHubMain plugin;
    private boolean debug;

    public static SelectorLogger getLogger() {
        return LOGGER;
    }

    private SelectorLogger(GeyserHubMain plugin) {
        this.plugin = plugin;
        debug = plugin.getConfig().getBoolean("Enable-Debug", false);
        ReloadableRegistry.registerReloadable(this);
    }

    public void log(Level level, String message) {
        switch (level) {
            default: // intentional fallthrough
            case INFO:
                info(message);
                break;
            case WARN:
                warn(message);
                break;
            case SEVERE:
                severe(message);
                break;
            case DEBUG:
                debug(message);
                break;
        }
    }
    public void info(String message) {
        plugin.getLogger().info(message);
    }
    public void warn(String message) {
        plugin.getLogger().warning(message);
    }
    public void severe(String message) {
        plugin.getLogger().severe(message);
    }
    public void debug(String message) {
        if (debug) {
            plugin.getLogger().info(message);
        }
    }

    public enum Level {
        INFO,
        WARN,
        SEVERE,
        DEBUG
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public boolean reload() {
        debug = plugin.getConfig().getBoolean("Enable-Debug", false);
        return true;
    }
}
