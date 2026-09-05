package com.example.anarchy.modules;

import net.minecraft.client.MinecraftClient;

public class ReachModule extends Module {
    private double reachDistance = 6.0;
    public ReachModule() { super("Reach"); }
    public double getReachDistance() { return reachDistance; }
    public void setReachDistance(double d) { this.reachDistance = d; }
    @Override public void onEnable() {}
    @Override public void onDisable() {}
    @Override public void tick(MinecraftClient client) {}
}
