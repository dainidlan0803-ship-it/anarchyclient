package com.example.anarchy.modules;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    private boolean enabled;
    private final String name;

    public Module(String name) { this.name = name; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }
    public String getName() { return name; }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void tick(MinecraftClient client);
}
