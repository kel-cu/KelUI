package ru.kelcuprum.kelui.mixin.screen;

import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.kelui.KelUI;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"
            )
    )
    private void fillEdgeless(Args args) {
        if(!KelUI.config.getBoolean("CHAT.EDGELESS_SCREEN", true)) return;
        args.set(0, (int) args.get(0) - 2);
        args.set(1, (int) args.get(1) - 2);
        args.set(2, (int) args.get(2) + 2);
        args.set(3, (int) args.get(3) + 2);
    }
}
