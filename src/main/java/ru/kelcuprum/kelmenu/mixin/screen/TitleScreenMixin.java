package ru.kelcuprum.kelmenu.mixin.screen;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.components.buttons.vanilla.VanillaButton;
import ru.kelcuprum.alinlib.gui.toast.AlinaToast;
import ru.kelcuprum.kelmenu.KelMenu;
import ru.kelcuprum.kelmenu.gui.components.PlayerInfoWidget;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    CubeMap CUBE_MAP = new CubeMap(new ResourceLocation("textures/gui/title/background/panorama"));
    ResourceLocation PANORAMA_OVERLAY = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
    private final PanoramaRenderer panorama = new PanoramaRenderer(CUBE_MAP);
    private long fadeInStart;

    protected TitleScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo cl) {
        if(!KelMenu.config.getBoolean("MAIN_MENU", true)) return;
        int x = KelMenu.config.getInt("MAIN_MENU.POSITION", 1) == 0 ? 10 : KelMenu.config.getInt("MAIN_MENU.POSITION", 1) == 1 ? this.width/2-105 : this.width-230;
        addRenderableWidget(new PlayerInfoWidget(x, height/2-60, 210, 20, this.minecraft.getUser().getName(), true, Component.translatable("kelmenu.player.login")));
        addRenderableWidget(new VanillaButton(x, height/2-35, 210, 20, Component.translatable("kelmenu.menu.singleplayer"), (OnPress) -> {
            this.minecraft.setScreen(new SelectWorldScreen(this));
        }));
        addRenderableWidget(new VanillaButton(x, height/2-10, 210, 20, Component.translatable("kelmenu.menu.multiplayer"), (OnPress) -> {
            this.minecraft.setScreen(new JoinMultiplayerScreen(this));
        }));
        addRenderableWidget(new VanillaButton(x, height/2+15, KelMenu.modmenu ? 100 : 210, 20, Component.translatable("kelmenu.menu.options"), (OnPress) -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));
        addRenderableWidget(new VanillaButton(x+110, height/2+15, 100, 20, Component.translatable("kelmenu.menu.mods"), (OnPress) -> {
            this.minecraft.setScreen(new com.terraformersmc.modmenu.gui.ModsScreen(this));
        }));
        addRenderableWidget(new VanillaButton(x, height/2+40, 100, 20, Component.translatable("kelmenu.menu.language"), (OnPress) -> {
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }));
        addRenderableWidget(new VanillaButton(x+110, height/2+40, 100, 20, Component.translatable("kelmenu.menu.quit"), (OnPress) -> {
            this.minecraft.stop();
        }));
        cl.cancel();
    }


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if(!KelMenu.config.getBoolean("MAIN_MENU", true)) return;
        if(KelMenu.config.getBoolean("MAIN_MENU.PANORAMA", true)){
            if (this.fadeInStart == 0L && !KelMenu.isLoaded) {
                KelMenu.isLoaded = true;
                this.fadeInStart = Util.getMillis();
            }
            float g = (float)(Util.getMillis() - this.fadeInStart) / 1000.0F;
            this.panorama.render(f, Mth.clamp(g, 0.0F, 1.0F));
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, (float)Mth.ceil(Mth.clamp(g, 0.0F, 1.0F)));
            guiGraphics.blit(PANORAMA_OVERLAY, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            renderDirtBackground(guiGraphics);
        }
        if (KelMenu.config.getBoolean("FIRST_START", true)) {
            KelMenu.config.setBoolean("FIRST_START", false);
            KelMenu.config.save();
            Minecraft.getInstance().getToasts().addToast(new AlinaToast(Localization.getText("kelmenu.name"), Localization.getText("kelmenu.toast.hello"), false));
        }
        if(KelMenu.config.getBoolean("MAIN_MENU.VERSION", true)) {
            guiGraphics.drawString(this.font, String.format("Minecraft %s", Minecraft.getInstance().getLaunchedVersion()), 12, this.height - (10 * 2)-10, 0xFFFFFF);
            guiGraphics.drawString(this.font, Component.translatable("title.credits"), 12, this.height - 20, 0xFFFFFF);
        }
        super.render(guiGraphics, i, j, f);
        cl.cancel();
    }
}
