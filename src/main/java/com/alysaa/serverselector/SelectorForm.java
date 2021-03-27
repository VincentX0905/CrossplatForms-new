package com.alysaa.serverselector;

import com.alysaa.serverselector.common.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class SelectorForm {
    static UUID uuid;

    public static void SelectServer() {

        boolean isBedrockPlayer = CheckJavaOrFloodPlayer.isBedrockPlayer(uuid);
        if (isBedrockPlayer) {
            FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
            player.sendForm(
                    SimpleForm.builder()
                            .title("Title")
                            .content("Content")
                            .button("server1", FormImage.Type.URL, "https://github.com/GeyserMC.png?size=200")
                            .button("server2", FormImage.Type.PATH, "textures/i/glyph_world_template.png")
                            .button("server3", FormImage.Type.URL, "https://github.com/GeyserMC.png?size=200")
                            .button("server4", FormImage.Type.URL, "https://github.com/GeyserMC.png?size=200")
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (response.getClickedButtonId() == 0) {
                                    System.out.println("hello");
                                }
                                if (response.getClickedButtonId() == 1) {
                                    System.out.println("hello2");
                                }
                                if (response.getClickedButtonId() == 2) {
                                    System.out.println("hello3");
                                }
                            }));
        }else { for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("Sorry! this command is only for bedrock players.");
        }
        }
    }
}