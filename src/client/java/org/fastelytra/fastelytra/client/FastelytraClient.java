package org.fastelytra.fastelytra.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class FastelytraClient implements ClientModInitializer {

    public boolean jumpKeyPreviouslyPressed = false; // Track the state of the jump key
    public static final Path CONFIG_PATH = new File("config/fastelytra.json").toPath();
    public static final Gson GSON = new Gson();
    public static JsonObject config;
    public KeyBinding boostKey;

    @Override
    public void onInitializeClient() {
        loadConfig();

        // Register custom keybind
        boostKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastelytra.boost", // Translation key
                InputUtil.Type.KEYSYM, // Input type
                GLFW.GLFW_KEY_B, // Default key
                "category.fastelytra" // Keybind category
        ));

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
                    boolean useWKey = config.get("useWKeyForBoost").getAsBoolean();
                    boolean isBoostKeyPressed = boostKey.isPressed();

                    if (player.isGliding() && (useWKey && minecraftClient.options.forwardKey.isPressed() || isBoostKeyPressed)) {
                        double speedBoost = config.get("speedBoostMultiplier").getAsDouble();
                        player.addVelocity(
                                player.getRotationVector().x * speedBoost,
                                player.getRotationVector().y * speedBoost,
                                player.getRotationVector().z * speedBoost
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
        config.addProperty("allowOnServers", false);
        config.addProperty("speedBoostMultiplier", 0.05); // Default speed boost multiplier
        config.addProperty("useWKeyForBoost", true); // Allow W key to boost by default

        saveConfig();
    }

    public static void saveConfig() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.write(CONFIG_PATH, GSON.toJson(config).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
