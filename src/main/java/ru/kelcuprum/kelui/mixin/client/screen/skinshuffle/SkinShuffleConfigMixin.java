package ru.kelcuprum.kelui.mixin.client.screen.skinshuffle;

import com.mineblock11.skinshuffle.client.gui.widgets.OpenCarouselButton;
import com.mineblock11.skinshuffle.client.gui.widgets.WarningIndicatorButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;

@Mixin(Screen.class)
public class SkinShuffleConfigMixin {
    @Inject(method = "addRenderableWidget", at = @At("HEAD"), cancellable = true)
    private <T extends GuiEventListener & Renderable & NarratableEntry>void addRenderableWidget(T guiEventListener, CallbackInfoReturnable<T> cir){
        if(((Screen) (Object) this instanceof TitleScreen && KelUI.config.getBoolean("MAIN_MENU", true)) || ((Screen) (Object) this instanceof PauseScreen  && KelUI.config.getBoolean("PAUSE_MENU", true))){
            if(guiEventListener instanceof OpenCarouselButton || guiEventListener instanceof WarningIndicatorButton) {
                cir.setReturnValue(guiEventListener);
                cir.cancel();
            }
            AlinLib.log(guiEventListener.getClass().getName());
        }
    }
}
