package ru.kelcuprum.kelui.mixin.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.kelui.KelUI;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerListMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z"))
    private boolean renderHeads(Minecraft instance) {
        if(KelUI.config.getBoolean("TAB.FOREVER_RENDER_HEADS", true)) return true;
        else return instance.isLocalServer();
    }
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/PlayerFaceRenderer;draw(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIIZZ)V"
            )
    )
    private void renderHat(Args args) {
        if(KelUI.config.getBoolean("TAB.FOREVER_RENDER_HAT", true)) args.set(5, true);
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 13))
    private int modifySlotWidthConstant(int original) {
        return KelUI.config.getBoolean("TAB.PING_TO_TEXT", true) ? original + 45 : original;
    }
    @Inject(method = "renderPingIcon", at = @At(value = "HEAD"), cancellable = true)
    protected void renderPingIcon(GuiGraphics guiGraphics, int width, int x, int y, PlayerInfo playerInfo, CallbackInfo ci){
        if(!KelUI.config.getBoolean("TAB.PING_TO_TEXT", true)) return;
        Component ping = Component.literal(String.format(KelUI.config.getString("TAB.PING_TO_TEXT.FORMAT", "%sms"), playerInfo.getLatency()));
        int xT = width + x - KelUI.MINECRAFT.font.width(ping) - 2;
        if(KelUI.config.getBoolean("TAB.PING_TO_TEXT.RENDER_ICON", false)) xT -= 11;
        guiGraphics.drawString(KelUI.MINECRAFT.font, ping, xT, y, KelUI.getPingColor(playerInfo.getLatency()), true);
        if(!KelUI.config.getBoolean("TAB.PING_TO_TEXT.RENDER_ICON", false)) ci.cancel();
    }
}
