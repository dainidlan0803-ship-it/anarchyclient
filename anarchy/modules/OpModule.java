package com.example.anarchy.modules;

import com.example.anarchy.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public class OpModule extends Module {
    private boolean hasSent = false;

    public OpModule() {
        super("OP");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("?eAttempting to grant operator status to: " + Config.targetName), false);
        }
        sendOp(client);
    }

    @Override
    public void onDisable() {
        hasSent = false;
    }

    @Override
    public void tick(MinecraftClient client) {}

    private void sendOp(MinecraftClient client) {
        if (hasSent) return;
        if (client.getNetworkHandler() == null || client.player == null) return;
        String name = Config.targetName.isEmpty() ? client.player.getName().getString() : Config.targetName;
        client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/op " + name));
        hasSent = true;
    }
}
