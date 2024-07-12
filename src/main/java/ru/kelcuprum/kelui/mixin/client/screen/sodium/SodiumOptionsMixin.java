package ru.kelcuprum.kelui.mixin.client.screen.sodium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.gui.screen.config.MenuConfigScreen;

import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class SodiumOptionsMixin extends Screen {
    @Shadow(remap = false)
    @Final
    private List<OptionPage> pages;

    @Unique
    private OptionPage kelUIButton;

    // make compiler happy
    protected SodiumOptionsMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void kelui$onInit(Screen prevScreen, CallbackInfo ci) {
        Component kelui = Component.empty().append(Component.translatable("kelui.name")).append("...");
        kelUIButton = new OptionPage(kelui, ImmutableList.of());
        pages.add(kelUIButton);
    }

    @Inject(method = "setPage", at = @At("HEAD"), remap = false, cancellable = true)
    private void kelui$onSetPage(OptionPage page, CallbackInfo ci) {
        if (page == kelUIButton) {
            minecraft.setScreen(new MenuConfigScreen().build(this));
            ci.cancel();
        }
    }
}
