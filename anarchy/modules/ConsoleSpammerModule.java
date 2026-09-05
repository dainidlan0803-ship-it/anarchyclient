package com.example.anarchy.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public class ConsoleSpammerModule extends Module {
    public ConsoleSpammerModule() { super("ConsoleSpammer"); }

    @Override public void onEnable() {}
    @Override public void onDisable() {}

    @Override
    public void tick(MinecraftClient client) {
        if (!isEnabled()) return;
        System.out.println("[AnarchyClient] SPAM");
        if (client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/help"));
        }
    }
}
