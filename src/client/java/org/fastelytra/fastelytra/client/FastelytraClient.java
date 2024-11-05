package org.fastelytra.fastelytra.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FastelytraClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                PlayerEntity player = client.player;

                // Check if the player is flying with an elytra and sprinting
                if (player.isFallFlying() && player.isSprinting()) {
                    // Increase the player's flight speed when sprinting with elytra
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
