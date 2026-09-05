package com.example.anarchy;

import com.example.anarchy.events.HudRenderer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AnarchyClient implements ModInitializer {
    public static KeyBinding openGuiKey;

    @Override
    public void onInitialize() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.anarchyclient.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.anarchyclient"
        ));

        ModuleManager.init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> ModuleManager.tickModules(client));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                client.setScreen(ClickGUI.getConfigScreen(client.currentScreen));
            }
        });
        HudRenderCallback.EVENT.register(new HudRenderer());
    }
}
