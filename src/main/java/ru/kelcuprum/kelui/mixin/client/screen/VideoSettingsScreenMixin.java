package ru.kelcuprum.kelui.mixin.client.screen;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.screen.config.MenuConfigScreen;

import java.util.List;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoSettingsScreenMixin extends OptionsSubScreen {
    public VideoSettingsScreenMixin(Screen screen, Options options) {
        super(screen, options, Component.empty());
    }

    @Inject(method = "addOptions", at = @At("RETURN"))
    public void addOptions(CallbackInfo info) {
        assert this.list != null;
        this.list.addSmall(List.of(new ButtonBuilder(Component.translatable("kelui.name"))
                .setOnPress((s) -> AlinLib.MINECRAFT.setScreen(new MenuConfigScreen().build((Screen) (Object) this)))
                .setWidth(150)
                .setStyle(KelUI.config.getBoolean("UI.ALINLIB_STYLE", false) ? KelUI.vanillaLikeStyle : GuiUtils.getSelected())
                .build()));
    }
}
