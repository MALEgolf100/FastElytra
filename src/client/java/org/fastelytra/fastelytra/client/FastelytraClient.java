package org.fastelytra.fastelytra.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FastelytraClient implements ClientModInitializer {

    private boolean jumpKeyPreviouslyPressed = false; // Track the state of the jump key

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                PlayerEntity player = client.player;
                MinecraftClient minecraftClient = MinecraftClient.getInstance();

                // Check if the player is flying with an elytra and holding the "W" key (forward)
                if (player.isGliding() && minecraftClient.options.forwardKey.isPressed()) {
                    // Increase the player's flight speed when holding W with elytra
                    player.addVelocity(
                            player.getRotationVector().x * 0.05,
                            player.getRotationVector().y * 0.05,
                            player.getRotationVector().z * 0.05
                    );
                }

                // Check if the player is flying with an elytra and presses the jump key
                boolean jumpKeyPressed = minecraftClient.options.jumpKey.isPressed();

                if (player.isGliding() && jumpKeyPressed && !jumpKeyPreviouslyPressed) {
                    // Make the player stop using the elytra if the jump key was just pressed
                    player.stopGliding();
                }

                // Update the previous state of the jump key
                jumpKeyPreviouslyPressed = jumpKeyPressed;
            }
        });
    }
}
