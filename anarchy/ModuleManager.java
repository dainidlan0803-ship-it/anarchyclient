package com.example.anarchy;

import com.example.anarchy.modules.*;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.add(new EspModule());
        modules.add(new ReachModule());
        modules.add(new FlightModule());
        modules.add(new CrashModule());
        modules.add(new ConsoleSpammerModule());
        modules.add(new OpModule());
        modules.add(new TakeoverModule());
        modules.add(new FreezeModule());
        modules.add(new AntiBanModule());
    }

    public static List<Module> getModules() { return modules; }

    public static void tickModules(MinecraftClient client) {
        for (Module mod : modules) {
            if (mod.isEnabled()) mod.tick(client);
        }
    }
}
