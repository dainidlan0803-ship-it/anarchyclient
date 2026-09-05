package com.example.anarchy.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.sound.SoundEvents;
import java.util.Random;

public class FreezeModule extends Module {
    private static final String[] COMMANDS = {
        "/help",
        "/time set day",
        "/time set night",
        "/seed",
        "/list",
        "/me is lagging",
        "/say spam",
        "/tell @p hi",
        "/gamemode survival",
        "/gamemode creative",
        "/weather clear",
        "/weather rain",
        "/weather thunder"
    };
    private final Random random = new Random();
    private int soundTimer = 0;

    public FreezeModule() {
        super("Freeze");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("?bFreeze enabled ? spamming commands to cause lag."), false);
            client.player.playSound(SoundEvents.ENTITY_WARDEN_ROAR, 1.0f, 1.0f);
        }
        soundTimer = 0;
    }

    @Override
    public void onDisable() {}

    @Override
    public void tick(MinecraftClient client) {
        if (!isEnabled()) return;
        if (client.getNetworkHandler() == null) return;

        for (int i = 0; i < 20; i++) {
            String cmd = COMMANDS[random.nextInt(COMMANDS.length)];
            client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(cmd));
        }

        soundTimer++;
        if (soundTimer >= 100) {
            soundTimer = 0;
            if (client.player != null) {
                client.player.playSound(SoundEvents.ENTITY_WARDEN_ROAR, 1.0f, 1.0f);
            }
        }
    }
}
