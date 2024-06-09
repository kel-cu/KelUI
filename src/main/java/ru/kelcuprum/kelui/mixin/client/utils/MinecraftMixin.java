package ru.kelcuprum.kelui.mixin.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.kelui.KelUI;

@Mixin({Minecraft.class})
public abstract class MinecraftMixin {

    @Shadow private static Minecraft instance;

    @Shadow @Nullable public Screen screen;

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    protected void renderSelection(CallbackInfoReturnable<Integer> cir) {
        if(!KelUI.config.getBoolean("UI.SMOOTH_MENU", false)) return;
        cir.setReturnValue(((Minecraft)(Object)this).getWindow().getFramerateLimit());
    }
    @Inject(method = "getVersionType", at = @At("HEAD"), cancellable = true)
    protected void getVersionType(CallbackInfoReturnable<String> cir) {
        if(!KelUI.config.getBoolean("GLOBAL.ENABLE_CUSTOM_VERSION_TYPE", false)) return;
        cir.setReturnValue(KelUI.config.getString("GLOBAL.CUSTOM_VERSION_TYPE", "KelUI"));
    }
    @Inject(method = "getLaunchedVersion", at = @At("HEAD"), cancellable = true)
    protected void getVersion(CallbackInfoReturnable<String> cir) {
        if(!KelUI.config.getBoolean("GLOBAL.ENABLE_CUSTOM_VERSION", false)) return;
        cir.setReturnValue(KelUI.config.getString("GLOBAL.CUSTOM_VERSION", KelUI.MINECRAFT_LAUNCHED_VERSION));
    }
    @ModifyArgs(
            method = "createTitle",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/StringBuilder;<init>(Ljava/lang/String;)V"
            )
    )
    protected void createTitle(Args args) {
        if(KelUI.config.getBoolean("GLOBAL.ENABLE_CUSTOM_NAME", false)) args.set(0, KelUI.config.getString("GLOBAL.CUSTOM_NAME", "KelUI"));
    }

    @Inject(method = "pauseGame", at = @At("TAIL"))
    void pauseGame(boolean pauseOnly, CallbackInfo ci){
        if(this.screen instanceof PauseScreen && !pauseOnly && KelUI.config.getBoolean("PAUSE_MENU", true) && KelUI.config.getNumber("PAUSE_MENU.TYPE", 0).intValue() == 1) Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(new ResourceLocation("kelui:oneshot_menu_decision")), 1.0F));
    }
}