package ru.kelcuprum.kelui.mixin.client.fix;

import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.kelui.KelUI;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin extends Screen {

    protected ConnectScreenMixin() {
        super(null);
    }
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawCenteredString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"
            )
    )
    private void changeTitleY(Args args) {
        args.set(3, (this.height/2)-(KelUI.MINECRAFT.font.lineHeight/2)-40);

    }
    @ModifyArgs(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;"
            )
    )
    private void changeButtonY(Args args) {
        args.set(1, ((this.height/2)-(KelUI.MINECRAFT.font.lineHeight/2)));
    }
}
