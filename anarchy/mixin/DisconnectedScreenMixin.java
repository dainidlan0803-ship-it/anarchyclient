package com.example.anarchy.mixin;

import com.example.anarchy.Config;
import com.example.anarchy.modules.AntiBanModule;
import com.example.anarchy.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin {
    private static long reconnectTimer = 0;

    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        AntiBanModule antiBan = (AntiBanModule) ModuleManager.getModules().stream()
                .filter(m -> m instanceof AntiBanModule).findFirst().orElse(null);
        if (antiBan != null && antiBan.isEnabled() && antiBan.isAutoReconnect()) {
            reconnectTimer = System.currentTimeMillis() + Config.reconnectDelaySeconds * 1000L;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        AntiBanModule antiBan = (AntiBanModule) ModuleManager.getModules().stream()
                .filter(m -> m instanceof AntiBanModule).findFirst().orElse(null);
        if (antiBan != null && antiBan.isEnabled() && antiBan.isAutoReconnect()) {
            if (reconnectTimer > 0 && System.currentTimeMillis() >= reconnectTimer) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.currentScreen != null && client.currentScreen instanceof DisconnectedScreen) {
                    client.currentScreen = null;
                    reconnectTimer = 0;
                }
            }
        }
    }
}
