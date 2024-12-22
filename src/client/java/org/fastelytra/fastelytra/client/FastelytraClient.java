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

                // Check if mod functions are allowed on servers
                if (!config.get("allowOnServers").getAsBoolean() && client.getCurrentServerEntry() != null) {
                    return; // Disable mod functions on servers if not allowed
                }

                // Fast Elytra functionality
                if (config.get("enableFastElytra").getAsBoolean()) {
                    if (player.isFallFlying() && minecraftClient.options.forwardKey.isPressed()) {
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

                    if (player.isFallFlying() && jumpKeyPressed && !jumpKeyPreviouslyPressed) {
                        player.stopFallFlying();
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
        config.addProperty("allowOnServers", false);

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
