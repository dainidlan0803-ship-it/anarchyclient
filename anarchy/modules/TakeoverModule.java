package com.example.anarchy.modules;

import com.example.anarchy.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import java.util.ArrayList;
import java.util.List;

public class TakeoverModule extends Module {
    private boolean hasRun = false;
    private List<String> bannedPlayers = new ArrayList<>();
    private long banStartTime = 0;
    private boolean unbanScheduled = false;

    public TakeoverModule() {
        super("Takeover");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("?cStarting server takeover (temporary bans)..."), false);
        }
        executeTakeover(client);
    }

    @Override
    public void onDisable() {
        if (!bannedPlayers.isEmpty() && !unbanScheduled) {
            MinecraftClient client = MinecraftClient.getInstance();
            for (String name : bannedPlayers) {
                client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/pardon " + name));
            }
            bannedPlayers.clear();
            hasRun = false;
        }
    }

    @Override
    public void tick(MinecraftClient client) {
        if (!hasRun) return;
        if (!bannedPlayers.isEmpty() && !unbanScheduled) {
            long elapsed = (System.currentTimeMillis() - banStartTime) / 1000;
            if (elapsed >= Config.banDurationSeconds) {
                for (String name : bannedPlayers) {
                    client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/pardon " + name));
                }
                if (client.player != null) {
                    client.player.sendMessage(net.minecraft.text.Text.literal("?aTemporary bans lifted. Players can rejoin."), false);
                }
                bannedPlayers.clear();
                unbanScheduled = true;
            }
        }
    }

    private void executeTakeover(MinecraftClient client) {
        if (hasRun) return;
        if (client.getNetworkHandler() == null || client.player == null) return;

        String target = Config.targetName.isEmpty() ? client.player.getName().getString() : Config.targetName;

        client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/op " + target));
        client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/deop @a"));
        client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/op " + target));

        for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
            String name = entry.getProfile().getName();
            if (!name.equals(target)) {
                client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/ban " + name));
                bannedPlayers.add(name);
            }
        }

        client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/whitelist on"));
        client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/whitelist add " + target));

        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("?aTakeover complete! " + bannedPlayers.size() + " players banned for " + Config.banDurationSeconds + " seconds."), false);
        }

        banStartTime = System.currentTimeMillis();
        hasRun = true;
        unbanScheduled = false;
    }
}
