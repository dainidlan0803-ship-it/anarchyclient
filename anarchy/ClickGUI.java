package com.example.anarchy;

import com.example.anarchy.modules.*;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGUI {
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Anarchy Client"));

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("Modules"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        for (Module module : ModuleManager.getModules()) {
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal(module.getName()), module.isEnabled())
                    .setDefaultValue(false)
                    .setSaveConsumer(module::setEnabled)
                    .build());
        }

        AntiBanModule antiBan = (AntiBanModule) ModuleManager.getModules().stream()
                .filter(m -> m instanceof AntiBanModule).findFirst().orElse(null);
        if (antiBan != null) {
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Auto-Reconnect"), antiBan.isAutoReconnect())
                    .setDefaultValue(true)
                    .setSaveConsumer(antiBan::setAutoReconnect)
                    .build());
            general.addEntry(entryBuilder.startIntSlider(Text.literal("Reconnect Delay (sec)"), Config.reconnectDelaySeconds, 1, 10)
                    .setDefaultValue(3)
                    .setSaveConsumer(v -> Config.reconnectDelaySeconds = v)
                    .build());
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Stealth Mode"), Config.stealthMode)
                    .setDefaultValue(false)
                    .setSaveConsumer(v -> Config.stealthMode = v)
                    .build());
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Auto-Unban (if op)"), antiBan.isAutoUnban())
                    .setDefaultValue(true)
                    .setSaveConsumer(antiBan::setAutoUnban)
                    .build());
        }

        ReachModule reach = (ReachModule) ModuleManager.getModules().stream()
                .filter(m -> m instanceof ReachModule).findFirst().orElse(null);
        if (reach != null) {
            general.addEntry(entryBuilder.startDoubleSlider(Text.literal("Reach Distance"), reach.getReachDistance(), 3.0, 20.0)
                    .setDefaultValue(6.0)
                    .setSaveConsumer(reach::setReachDistance)
                    .build());
        }

        FlightModule flight = (FlightModule) ModuleManager.getModules().stream()
                .filter(m -> m instanceof FlightModule).findFirst().orElse(null);
        if (flight != null) {
            general.addEntry(entryBuilder.startFloatSlider(Text.literal("Flight Speed"), flight.getFlySpeed(), 0.01f, 1.0f)
                    .setDefaultValue(0.1f)
                    .setSaveConsumer(flight::setFlySpeed)
                    .build());
        }

        general.addEntry(entryBuilder.startStrField(Text.literal("Target Name"), Config.targetName)
                .setDefaultValue("")
                .setSaveConsumer(newName -> Config.targetName = newName)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Text.literal("Ban Duration (sec)"), Config.banDurationSeconds, 10, 600)
                .setDefaultValue(60)
                .setSaveConsumer(v -> Config.banDurationSeconds = v)
                .build());

        builder.setSavingRunnable(() -> {});
        return builder.build();
    }
}
