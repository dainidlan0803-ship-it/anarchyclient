package com.example.anarchy.mixin;

import com.example.anarchy.modules.ReachModule;
import com.example.anarchy.ModuleManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onGetBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
        ReachModule module = (ReachModule) ModuleManager.getModules().stream()
                .filter(m -> m instanceof ReachModule).findFirst().orElse(null);
        if (module != null && module.isEnabled()) {
            cir.setReturnValue(module.getReachDistance());
        }
    }

    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onGetEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
        ReachModule module = (ReachModule) ModuleManager.getModules().stream()
                .filter(m -> m instanceof ReachModule).findFirst().orElse(null);
        if (module != null && module.isEnabled()) {
            cir.setReturnValue(module.getReachDistance());
        }
    }
}
