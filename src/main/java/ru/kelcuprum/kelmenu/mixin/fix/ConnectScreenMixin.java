package ru.kelcuprum.kelmenu.mixin.fix;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin extends Screen {

    protected ConnectScreenMixin() {
        super(null);
    }
    private long lastNarration = -1L;
    private Component status = Component.translatable("connect.connecting");
    @Inject(method = "updateStatus", at = @At("HEAD"), cancellable = true)
    private void updateStatus(Component component, CallbackInfo cl) {
        this.status = component;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        this.renderBackground(guiGraphics, i, j, f);
        long l = Util.getMillis();
        if (l - this.lastNarration > 2000L) {
            this.lastNarration = l;
            this.minecraft.getNarrator().sayNow(Component.translatable("narrator.joining"));
        }

        guiGraphics.drawCenteredString(this.font, this.status, this.width / 2, this.height / 3, 16777215);
        super.render(guiGraphics, i, j, f);
        cl.cancel();
    }
}
