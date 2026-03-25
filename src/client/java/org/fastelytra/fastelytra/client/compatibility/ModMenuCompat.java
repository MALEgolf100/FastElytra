package org.fastelytra.fastelytra.client.compatibility;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.fastelytra.fastelytra.client.FastelytraClient;

// ---- CLOTH CONFIG IMPORTS (uncomment when Cloth Config updates for this MC version) ----
// import me.shedaniel.clothconfig2.api.ConfigBuilder;
// import me.shedaniel.clothconfig2.api.ConfigCategory;
// import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
// import net.minecraft.client.gui.screens.Screen;
// import net.minecraft.network.chat.Component;
// -----------------------------------------------------------------------------------------

import java.util.Map;

//public class ModMenuCompat implements ModMenuApi {

    //@Override
    //public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // Config screen disabled until Cloth Config updates for this Minecraft version.
        // To re-enable: uncomment the imports above and replace the return below with the full screen builder.
        // ---- CLOTH CONFIG SCREEN (uncomment when ready) ----
        // return parent -> {
        //     ConfigBuilder builder = ConfigBuilder.create()
        //             .setParentScreen(parent)
        //             .setTitle(Component.literal("Fast Elytra Settings"));
        //
        //     ConfigCategory general = builder.getOrCreateCategory(Component.literal("General Settings"));
        //     ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        //
        //     general.addEntry(entryBuilder.startBooleanToggle(
        //                     Component.literal("Enable Fast Elytra"),
        //                     FastelytraClient.config.get("enableFastElytra").getAsBoolean())
        //             .setDefaultValue(true)
        //             .setTooltip(Component.literal("Enable or disable the mod."))
        //             .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("enableFastElytra", newValue))
        //             .build()
        //     );
        //
        //     general.addEntry(entryBuilder.startBooleanToggle(
        //                     Component.literal("Disable Jump Key Stops Gliding"),
        //                     FastelytraClient.config.get("disableJumpKeyStopsGliding").getAsBoolean())
        //             .setDefaultValue(false)
        //             .setTooltip(Component.literal("Prevent jump key from stopping gliding."))
        //             .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("disableJumpKeyStopsGliding", newValue))
        //             .build()
        //     );
        //
        //     general.addEntry(entryBuilder.startBooleanToggle(
        //                     Component.literal("Allow On Servers"),
        //                     FastelytraClient.config.get("allowOnServers").getAsBoolean())
        //             .setDefaultValue(false)
        //             .setTooltip(Component.literal("Allows use on multiplayer servers."))
        //             .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("allowOnServers", newValue))
        //             .build()
        //     );
        //
        //     general.addEntry(entryBuilder.startFloatField(
        //                     Component.literal("Speed Boost Multiplier"),
        //                     (float) FastelytraClient.config.get("speedBoostMultiplier").getAsDouble())
        //             .setDefaultValue(0.05f)
        //             .setMin(0.01f)
        //             .setMax(1.0f)
        //             .setTooltip(Component.literal("Change how fast your Elytra boosting is."))
        //             .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("speedBoostMultiplier", newValue))
        //             .build()
        //     );
        //
        //     general.addEntry(entryBuilder.startBooleanToggle(
        //                     Component.literal("Use W Key For Boost"),
        //                     FastelytraClient.config.get("useWKeyForBoost").getAsBoolean())
        //             .setDefaultValue(true)
        //             .setTooltip(Component.literal("Use the W key for boosting."))
        //             .setSaveConsumer(newValue -> FastelytraClient.config.addProperty("useWKeyForBoost", newValue))
        //             .build()
        //     );
        //
        //     builder.setSavingRunnable(FastelytraClient::saveConfig);
        //     Screen screen = builder.build();
        //     return screen;
        // };
        // -----------------------------------------------------

        //return parent -> null; // No config screen until Cloth Config updates
    //}

    //@Override
    //public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
    //    return ModMenuApi.super.getProvidedConfigScreenFactories();
    //}
//}