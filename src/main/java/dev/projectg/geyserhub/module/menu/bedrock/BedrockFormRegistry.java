package dev.projectg.geyserhub.module.menu.bedrock;

import dev.projectg.geyserhub.reloadable.Reloadable;
import dev.projectg.geyserhub.reloadable.ReloadableRegistry;
import dev.projectg.geyserhub.SelectorLogger;
import dev.projectg.geyserhub.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BedrockFormRegistry implements Reloadable {

    private static BedrockFormRegistry INSTANCE;
    public static final String DEFAULT = "default";

    /**
     * If the bedrock form is enabled in the config. the {@link #getFormNames()} may still return an empty list.
     */
    private boolean isEnabled = false;
    private final Map<String, BedrockForm> enabledForms = new HashMap<>();

    public static BedrockFormRegistry getInstance() {
        return INSTANCE;
    }

    public BedrockFormRegistry() {
        ReloadableRegistry.registerReloadable(this);
        load();
        INSTANCE = this;
    }

    private void load() {
        ConfigManager configManager = new ConfigManager();
        FileConfiguration config = configManager.getFileConfiguration("selector");
        Objects.requireNonNull(config);
        SelectorLogger logger = SelectorLogger.getLogger();

        enabledForms.clear();

        if (config.contains("Bedrock-Selector", true) && config.isConfigurationSection("Bedrock-Selector")) {
            ConfigurationSection selectorSection = config.getConfigurationSection("Bedrock-Selector");
            Objects.requireNonNull(selectorSection);

            if (selectorSection.contains("Enable", true) && selectorSection.isBoolean("Enable")) {
                if (selectorSection.getBoolean("Enable")) {
                    isEnabled = true;
                    if (selectorSection.contains("Forms", true) && selectorSection.isConfigurationSection("Forms")) {
                        ConfigurationSection forms = selectorSection.getConfigurationSection("Forms");
                        Objects.requireNonNull(forms);

                        boolean noSuccess = true;
                        boolean containsDefault = false;
                        for (String entry : forms.getKeys(false)) {
                            if (!forms.isConfigurationSection(entry)) {
                                logger.warn("Bedrock form with name " + entry + " is being skipped because it is not a configuration section");
                                continue;
                            }
                            ConfigurationSection formInfo = forms.getConfigurationSection(entry);
                            Objects.requireNonNull(formInfo);
                            BedrockForm form = new BedrockForm(formInfo);
                            if (form.isEnabled()) {
                                enabledForms.put(entry, form);
                                noSuccess = false;
                            }
                            if ("default".equals(entry)) {
                                containsDefault = true;
                            }
                        }

                        if (noSuccess) {
                            logger.warn("Failed to load ALL bedrock forms, due to configuration error.");
                            return;
                        } else {
                            logger.info("Valid Bedrock forms are: " + enabledForms.keySet());
                        }

                        if (!containsDefault) {
                            logger.warn("Failed to load a default form! The Server Selector compass will not work and players will not be able to open the default form with \"/ghub\"");
                        }
                    }
                } else {
                    logger.debug("Not enabling bedrock forms because it is disabled in the config.");
                }
            } else {
                logger.warn("Not enabling bedrock forms because the Enable value is not present in the config.");
            }
        } else {
            logger.warn("Not enabling bedrock forms because the whole configuration section is not present.");
        }
    }

    public void sendForm(@Nonnull FloodgatePlayer player, @Nonnull String form) {
        enabledForms.get(form).sendForm(player);
    }

    public boolean isEnabled() {
        return isEnabled;
    }
    public List<String> getFormNames() {
        return new ArrayList<>(enabledForms.keySet());
    }

    @Override
    public boolean reload() {
        load();
        return isEnabled;
    }
}
