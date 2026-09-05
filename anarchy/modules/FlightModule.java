package com.example.anarchy.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;

public class FlightModule extends Module {
    private float flySpeed = 0.1f;
    public FlightModule() { super("Flight"); }
    public float getFlySpeed() { return flySpeed; }
    public void setFlySpeed(float s) { this.flySpeed = s; }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            PlayerAbilities abilities = client.player.getAbilities();
            abilities.allowFlying = true;
            abilities.flying = true;
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            PlayerAbilities abilities = client.player.getAbilities();
            abilities.allowFlying = false;
            abilities.flying = false;
        }
    }

    @Override
    public void tick(MinecraftClient client) {
        if (client.player != null && isEnabled()) {
            client.player.getAbilities().setFlySpeed(flySpeed);
        }
    }
}
