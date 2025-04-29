package org.fastelytra.fastelytra.client.compatibility;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import org.fastelytra.fastelytra.client.FastelytraClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Map;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Fast Elytra Settings"));

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General Settings"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // "Enable Fast Elytra" toggle
            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Enable Fast Elytra"),
                            FastelytraClient.config.get("enableFastElytra").getAsBoolean())
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Enable or disable fast flying with Elytra."))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("enableFastElytra", newValue))
                    .build()
            );

            // "Disable Jump Key Stops Gliding" toggle
            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Disable Jump Key Stops Gliding"),
                            FastelytraClient.config.get("disableJumpKeyStopsGliding").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("Prevent jump key from stopping gliding."))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("disableJumpKeyStopsGliding", newValue))
                    .build()
            );

            // "Allow On Servers" toggle
            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Allow On Servers"),
                            FastelytraClient.config.get("allowOnServers").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("Allow boosting on multiplayer servers."))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("allowOnServers", newValue))
                    .build()
            );

            // "Speed Boost Multiplier" slider
            general.addEntry(entryBuilder.startFloatField(
                            Text.literal("Speed Boost Multiplier"),
                            (float) FastelytraClient.config.get("speedBoostMultiplier").getAsDouble())
                    .setDefaultValue(0.05f)
                    .setMin(0.01f)
                    .setMax(1.0f)
                    .setTooltip(Text.literal("Change how fast your Elytra boosting is."))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("speedBoostMultiplier", newValue))
                    .build()
            );

            // "Use W Key For Boost" toggle
            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Use W Key For Boost"),
                            FastelytraClient.config.get("useWKeyForBoost").getAsBoolean())
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Use the W key for boosting instead of boost key."))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("useWKeyForBoost", newValue))
                    .build()
            );

            builder.setSavingRunnable(FastelytraClient::saveConfig); // Save config when screen closes

            Screen screen = builder.build();
            return screen;
        };
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return ModMenuApi.super.getProvidedConfigScreenFactories();
    }
}
