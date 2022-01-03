package dev.projectg.crossplatforms.config.mapping;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Getter
@ConfigSerializable
public class GeneralConfig extends Configuration {

    @Getter
    private static final int defaultVersion = 1;

    @Setting("enable-debug")
    private boolean enableDebug;
}