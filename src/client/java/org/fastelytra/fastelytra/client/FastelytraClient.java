package org.fastelytra.fastelytra.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class FastelytraClient implements ClientModInitializer {

    private boolean jumpKeyPreviouslyPressed = false; // Track the state of the jump key
    private static final Path CONFIG_PATH = new File("config/fastelytra.json").toPath();
    private static final Gson GSON = new Gson();
    private JsonObject config;

    @Override
    public void onInitializeClient() {
        loadConfig();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                PlayerEntity player = client.player;
                MinecraftClient minecraftClient = MinecraftClient.getInstance();

                // Disable mod functions on servers
                if (client.getCurrentServerEntry() != null) {
                    return;
                }

                // Fast Elytra functionality
                if (config.get("enableFastElytra").getAsBoolean()) {
                    if (player.isGliding() && minecraftClient.options.forwardKey.isPressed()) {
                        player.addVelocity(
                                player.getRotationVector().x * 0.05,
                                player.getRotationVector().y * 0.05,
                                player.getRotationVector().z * 0.05
                        );
                    }
                }

                // Jump key stops gliding functionality
                if (!config.get("disableJumpKeyStopsGliding").getAsBoolean()) {
                    boolean jumpKeyPressed = minecraftClient.options.jumpKey.isPressed();

                    if (player.isGliding() && jumpKeyPressed && !jumpKeyPreviouslyPressed) {
                        player.stopGliding();
                    }

                    jumpKeyPreviouslyPressed = jumpKeyPressed;
                }
            }
        });
    }

    private void loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String content = new String(Files.readAllBytes(CONFIG_PATH));
                config = GSON.fromJson(content, JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
                createDefaultConfig();
            }
        } else {
            createDefaultConfig();
        }
    }

    private void createDefaultConfig() {
        config = new JsonObject();
        config.addProperty("enableFastElytra", true);
        config.addProperty("disableJumpKeyStopsGliding", false);

        saveConfig();
    }

    private void saveConfig() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.write(CONFIG_PATH, GSON.toJson(config).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
