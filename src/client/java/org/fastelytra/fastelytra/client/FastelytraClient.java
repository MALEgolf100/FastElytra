package org.fastelytra.fastelytra.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class FastelytraClient implements ClientModInitializer {

    public boolean jumpKeyPreviouslyPressed = false;
    public static final Path CONFIG_PATH = new File("config/fastelytra.json").toPath();
    public static final Gson GSON = new Gson();
    private static final KeyBinding.Category FAST_ELYTRA_CATEGORY = KeyBinding.Category.create(Identifier.of("fastelytra", "main"));
    public static JsonObject config;
    public KeyBinding boostKey;

    @Override
    public void onInitializeClient() {
        loadConfig();

        boostKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastelytra.boost",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_B,
                FAST_ELYTRA_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                PlayerEntity player = client.player;
                MinecraftClient minecraftClient = MinecraftClient.getInstance();

                // Server restriction
                if (!config.get("allowOnServers").getAsBoolean() && client.getCurrentServerEntry() != null) {
                    return;
                }

                // Fast Elytra boost
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

                // Speed limiter runs after boost so the cap is always enforced
                if (config.get("enableSpeedLimit").getAsBoolean() && player.isGliding()) {
                    double speedLimit = config.get("speedLimit").getAsDouble();
                    // Convert m/s → blocks/tick (20 ticks per second)
                    double maxSpeedPerTick = speedLimit / 20.0;

                    Vec3d velocity = player.getVelocity();
                    double currentSpeed = velocity.length();

                    if (currentSpeed > maxSpeedPerTick) {
                        double scale = maxSpeedPerTick / currentSpeed;
                        player.setVelocity(velocity.multiply(scale));
                    }
                }

                // Jump key stops gliding
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
                // Backfill new keys for existing config files that predate this feature
                if (!config.has("enableSpeedLimit")) config.addProperty("enableSpeedLimit", false);
                if (!config.has("speedLimit"))        config.addProperty("speedLimit", 30.0);
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
        config.addProperty("speedBoostMultiplier", 0.05);
        config.addProperty("useWKeyForBoost", true);
        config.addProperty("enableSpeedLimit", false);
        config.addProperty("speedLimit", 30.0);

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