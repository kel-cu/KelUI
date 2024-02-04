package ru.kelcuprum.kelui.mixin.screen;

import com.terraformersmc.modmenu.gui.ModsScreen;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonSprite;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.components.PlayerHeadWidget;

import static ru.kelcuprum.kelui.KelUI.ICONS.ACCESSIBILITY;
import static ru.kelcuprum.kelui.KelUI.ICONS.LANGUAGE;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Final
    @Shadow
    private static ResourceLocation PANORAMA_OVERLAY;
    @Final
    @Shadow
    private PanoramaRenderer panorama;
    @Shadow
    private long fadeInStart;
    @Final
    @Shadow
    private boolean fading;

    protected TitleScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo cl) {
        if(!KelUI.config.getBoolean("MAIN_MENU", true)) return;
        int x = 10;
        //

        addRenderableWidget(new Button(x+25, height/2-35, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.singleplayer"), (OnPress) -> {
            this.minecraft.setScreen(new SelectWorldScreen(this));
        }));
        addRenderableWidget(new PlayerHeadWidget(x, height/2-35, 20, 20));
        //
        addRenderableWidget(new Button(x+25, height/2-10, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.multiplayer"), (OnPress) -> {
            this.minecraft.setScreen(new JoinMultiplayerScreen(this));
        }));
        addRenderableWidget(new ButtonSprite(x, height/2-10, 20, 20, InterfaceUtils.DesignType.VANILLA, InterfaceUtils.Icons.OPTIONS, Component.translatable("kelui.menu.options"), (OnPress) -> {
            this.minecraft.setScreen(KelUI.getOptionScreen(this));
        }));
        //
        addRenderableWidget(new Button(x+25, height/2+15, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.mods"), (OnPress) -> {
            this.minecraft.setScreen(new ModsScreen(this));
        }));
        addRenderableWidget(new ButtonSprite(x, height/2+15, 20, 20, InterfaceUtils.DesignType.VANILLA, LANGUAGE, Component.translatable("kelui.menu.language"), (OnPress) -> {
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }));
        //
        addRenderableWidget(new Button(x+25, height/2+40, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.quit"), (OnPress) -> {
            this.minecraft.stop();
        }));
        addRenderableWidget(new ButtonSprite(x, height/2+40, 20, 20, InterfaceUtils.DesignType.VANILLA, ACCESSIBILITY, Component.translatable("options.accessibility"), (OnPress) -> {
            this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options));
        }));
        if(KelUI.config.getBoolean("MAIN_MENU.VERSION", true)) {
            addRenderableWidget(new TextBox(x, height - 30, 210, font.lineHeight, Component.literal(KelUI.getStringCredits()), false));
            addRenderableWidget(new TextBox(x, height - 20, 210, font.lineHeight, Component.literal(KelUI.getStringVersion()), false));
        }
        cl.cancel();
    }


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if(!KelUI.config.getBoolean("MAIN_MENU", true)) return;
        if(KelUI.config.getBoolean("MAIN_MENU.PANORAMA", true)){
            if (this.fadeInStart == 0L && this.fading) {
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
        InterfaceUtils.renderLeftPanel(guiGraphics, 230, this.height);
        if (KelUI.config.getBoolean("FIRST_START", true)) {
            KelUI.config.setBoolean("FIRST_START", false);
            KelUI.config.save();
            new ToastBuilder().setTitle(Localization.getText("kelui.name")).setMessage(Localization.getText("kelui.toast.hello")).setIcon(Items.CRAFTING_TABLE).show(Minecraft.getInstance().getToasts());
        }
        super.render(guiGraphics, i, j, f);
        cl.cancel();
    }
}
