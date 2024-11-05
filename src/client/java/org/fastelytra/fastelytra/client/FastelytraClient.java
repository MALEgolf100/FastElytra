package org.fastelytra.fastelytra.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.util.InputUtil;

public class FastelytraClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                PlayerEntity player = client.player;
                MinecraftClient minecraftClient = MinecraftClient.getInstance();

                // Check if the player is flying with an elytra and holding the "W" key (forward)
                if (player.isFallFlying() && minecraftClient.options.forwardKey.isPressed()) {
                    // Increase the player's flight speed when holding W with elytra
                    player.addVelocity(
                            player.getRotationVector().x * 0.05,
                            player.getRotationVector().y * 0.05,
                            player.getRotationVector().z * 0.05
                    );
                }
            }
        });
    }
}
