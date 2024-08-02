package ru.kelcuprum.kelui.mixin.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.kelcuprum.kelui.KelUI;

@Mixin(Gui.class)
public class GuiMixin$TabList {
    // TabList
    @Redirect(method = "renderTabList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z"))
    private boolean renderTabList(Minecraft instance) {
        if(KelUI.config.getBoolean("TAB.SINGLEPLAYER", true)) return false;
        else return instance.isLocalServer();
    }
}
