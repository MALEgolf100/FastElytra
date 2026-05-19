package org.fastelytra.fastelytra.client.compatibility;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import org.fastelytra.fastelytra.client.FastelytraClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Map;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("config.fastelytra.title"));

            ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.fastelytra.category.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("config.fastelytra.enable_fast_elytra"),
                            FastelytraClient.config.get("enableFastElytra").getAsBoolean())
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("config.fastelytra.enable_fast_elytra.tooltip"))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("enableFastElytra", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("config.fastelytra.disable_jump_stops_gliding"),
                            FastelytraClient.config.get("disableJumpKeyStopsGliding").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("config.fastelytra.disable_jump_stops_gliding.tooltip"))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("disableJumpKeyStopsGliding", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("config.fastelytra.allow_on_servers"),
                            FastelytraClient.config.get("allowOnServers").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("config.fastelytra.allow_on_servers.tooltip"))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("allowOnServers", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startFloatField(
                            Text.translatable("config.fastelytra.speed_boost_multiplier"),
                            (float) FastelytraClient.config.get("speedBoostMultiplier").getAsDouble())
                    .setDefaultValue(0.05f)
                    .setMin(0.01f)
                    .setMax(1.0f)
                    .setTooltip(Text.translatable("config.fastelytra.speed_boost_multiplier.tooltip"))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("speedBoostMultiplier", newValue))
                    .build()
            );

            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("config.fastelytra.use_w_key_for_boost"),
                            FastelytraClient.config.get("useWKeyForBoost").getAsBoolean())
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("config.fastelytra.use_w_key_for_boost.tooltip"))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("useWKeyForBoost", newValue))
                    .build()
            );

            // Speed limiter toggle
            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("config.fastelytra.enable_speed_limit"),
                            FastelytraClient.config.get("enableSpeedLimit").getAsBoolean())
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("config.fastelytra.enable_speed_limit.tooltip"))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("enableSpeedLimit", newValue))
                    .build()
            );

            // Speed limit value (m/s)
            general.addEntry(entryBuilder.startFloatField(
                            Text.translatable("config.fastelytra.speed_limit"),
                            (float) FastelytraClient.config.get("speedLimit").getAsDouble())
                    .setDefaultValue(100.0f)
                    .setMin(1.0f)
                    .setMax(1000.0f)
                    .setTooltip(Text.translatable("config.fastelytra.speed_limit.tooltip"))
                    .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("speedLimit", newValue))
                    .build()
            );

            builder.setSavingRunnable(FastelytraClient::saveConfig);

            Screen screen = builder.build();
            return screen;
        };
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return ModMenuApi.super.getProvidedConfigScreenFactories();
    }
}