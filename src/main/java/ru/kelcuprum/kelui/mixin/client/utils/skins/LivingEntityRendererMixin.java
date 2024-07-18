package ru.kelcuprum.kelui.mixin.client.utils.skins;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kelcuprum.kelui.gui.cicada.DummyClientPlayerEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(
            method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z",
            cancellable = true,
            at = {@At("HEAD")}
    )
    private <T extends LivingEntity> void fakeHasLabel(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof DummyClientPlayerEntity) {
            cir.setReturnValue(false);
        }
    }
}
