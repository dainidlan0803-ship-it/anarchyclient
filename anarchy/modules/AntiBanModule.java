package com.example.anarchy.modules;

import com.example.anarchy.Config;
import com.example.anarchy.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public class AntiBanModule extends Module {
    private boolean autoReconnect = true;
    private boolean autoUnban = true;
    private boolean hasAttemptedUnban = false;

    public AntiBanModule() {
        super("AntiBan");
    }

    public boolean isAutoReconnect() { return autoReconnect; }
    public void setAutoReconnect(boolean val) { this.autoReconnect = val; }

    public boolean isAutoUnban() { return autoUnban; }
    public void setAutoUnban(boolean val) { this.autoUnban = val; }

    @Override
    public void onEnable() {
        if (Config.stealthMode) {
            for (Module mod : ModuleManager.getModules()) {
                if (mod != this && mod.isEnabled()) {
                    mod.setEnabled(false);
                }
            }
        }
        hasAttemptedUnban = false;
    }

    @Override
    public void onDisable() {}

    @Override
    public void tick(MinecraftClient client) {
        if (Config.stealthMode && isEnabled()) {
            for (Module mod : ModuleManager.getModules()) {
                if (mod != this && mod.isEnabled()) {
                    mod.setEnabled(false);
                }
            }
        }

        if (isEnabled() && autoUnban && client.player != null && client.getNetworkHandler() != null && !hasAttemptedUnban) {
            String name = Config.targetName.isEmpty() ? client.player.getName().getString() : Config.targetName;
            client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/pardon " + name));
            client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/unban " + name));
            hasAttemptedUnban = true;
            new Thread(() -> {
                try { Thread.sleep(5000); } catch (InterruptedException e) {}
                hasAttemptedUnban = false;
            }).start();
        }
    }
}
