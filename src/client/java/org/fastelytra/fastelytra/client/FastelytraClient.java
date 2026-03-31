package org.fastelytra.fastelytra.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import org.fastelytra.fastelytra.Fastelytra;

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
    KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(Fastelytra.MOD_ID, "fast_elytra_category")
    );
    public static JsonObject config;
    public KeyMapping boostKey;

    @Override
    public void onInitializeClient() {
        loadConfig();

        boostKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.fastelytra.boost",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                Player player = client.player;
                Minecraft minecraftClient = Minecraft.getInstance();

                // Server restriction
                if (!config.get("allowOnServers").getAsBoolean() && client.getCurrentServer() != null) {
                    return;
                }

                // Fast Elytra boost
                if (config.get("enableFastElytra").getAsBoolean()) {
                    boolean useWKey = config.get("useWKeyForBoost").getAsBoolean();
                    boolean isBoostKeyPressed = boostKey.isDown();

                    if (player.isFallFlying() && (useWKey && minecraftClient.options.keyUp.isDown() || isBoostKeyPressed)) {
                        double speedBoost = config.get("speedBoostMultiplier").getAsDouble();
                        Vec3 look = player.getLookAngle();

                        player.addDeltaMovement(new Vec3(
                                look.x * speedBoost,
                                look.y * speedBoost,
                                look.z * speedBoost
                        ));
                    }
                }

                // Jump key stops gliding
                if (!config.get("disableJumpKeyStopsGliding").getAsBoolean()) {
                    boolean jumpKeyPressed = minecraftClient.options.keyJump.isDown();

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
        config.addProperty("speedBoostMultiplier", 0.05);
        config.addProperty("useWKeyForBoost", true);

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
