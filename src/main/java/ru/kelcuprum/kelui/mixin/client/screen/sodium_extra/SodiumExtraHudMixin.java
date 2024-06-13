package ru.kelcuprum.kelui.mixin.client.screen.sodium_extra;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraHud;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.KelUI;

import java.util.List;

@Mixin(SodiumExtraHud.class)
public class SodiumExtraHudMixin {
    @Final
    @Shadow
    private Minecraft client;
    @Final
    @Shadow
    private List<Component> textList;
    @Inject(method = "onHudRender", at = @At("HEAD"), cancellable = true)
    public void onHudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!KelUI.config.getBoolean("HUD.SODIUM_EXTRA_DEBUG", false)) return;
        if (!this.client.getDebugOverlay().showDebugScreen() && !this.client.options.hideGui) {
            if (!textList.isEmpty()) {
                SodiumExtraGameOptions.OverlayCorner overlayCorner = SodiumExtraClientMod.options().extraSettings.overlayCorner;
                int pos = overlayCorner == SodiumExtraGameOptions.OverlayCorner.TOP_LEFT ? 0 : overlayCorner == SodiumExtraGameOptions.OverlayCorner.TOP_RIGHT ? 1 : overlayCorner == SodiumExtraGameOptions.OverlayCorner.BOTTOM_LEFT ? 2  : 3;
                int l = pos == 0 || pos == 1 ? 0 : textList.size() - 1;
                int border = 4;
                int f = KelUI.MINECRAFT.font.lineHeight + (border*2);
                for (Component text : textList) {
                    int x = pos == 0 || pos == 2 ? 2 : guiGraphics.guiWidth() - 11 - (KelUI.MINECRAFT.font.width(text));
                    int y = pos == 0 || pos == 1 ? 2 + (l * f) : guiGraphics.guiHeight() - 11 - KelUI.MINECRAFT.font.lineHeight - (l * f);
                    guiGraphics.fill(x, y, x+KelUI.MINECRAFT.font.width(text)+(border*2), y+KelUI.MINECRAFT.font.lineHeight+(border*2), 0x7f000000);
                    guiGraphics.drawString(KelUI.MINECRAFT.font, text, x+border, y+border, -1);
                    if (pos == 0 || pos == 1) l++;
                    else l--;
                }
            }
        }
        ci.cancel();
    }
}
