package ru.kelcuprum.kelui.mixin.utils;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.kelcuprum.kelui.KelUI;

@Mixin(ChatComponent.class)
public abstract class ChatOverlayMixin {
    @ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", at = @At("HEAD"), argsOnly = true)
    private Component onAddMessage(Component message) {
        if(!KelUI.config.getBoolean("CHAT.TIMESTAMP", true)) return message;
        return Component.empty().append(KelUI.createTimestamp()).append(" ").append(message);
    }
}
