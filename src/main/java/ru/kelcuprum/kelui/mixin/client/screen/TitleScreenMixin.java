package ru.kelcuprum.kelui.mixin.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.RealmsMainScreen;
import com.terraformersmc.modmenu.gui.ModsScreen;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonSprite;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.components.PlayerHeadWidget;

import static ru.kelcuprum.kelui.KelUI.ICONS.*;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Shadow
    private long fadeInStart;
    @Final
    @Shadow
    private boolean fading;

    @Shadow protected abstract void renderPanorama(GuiGraphics guiGraphics, float f);

    protected TitleScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo cl) {
        if(!KelUI.config.getBoolean("MAIN_MENU", true)) return;
        int x = 10;
        //
        assert this.minecraft != null;
        int y = height/2-35;
        addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.singleplayer"), (OnPress) -> this.minecraft.setScreen(new SelectWorldScreen(this))));
        addRenderableWidget(new PlayerHeadWidget(x, y, 20, 20));
        y+=25;
        //
        addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.multiplayer"), (OnPress) -> this.minecraft.setScreen(new JoinMultiplayerScreen(this))));
        addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.VANILLA, InterfaceUtils.Icons.OPTIONS, Component.translatable("kelui.menu.options"), (OnPress) -> this.minecraft.setScreen(KelUI.getOptionScreen(this))));
        y+=25;
        //
        if(KelUI.config.getBoolean("MAIN_MENU.ENABLE_REALMS", false)){
            switch (KelUI.config.getNumber("MAIN_MENU.REALMS_SMALL_BUTTON", 0).intValue()){
                case 1 -> addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.VANILLA, MUSIC, Component.translatable("kelui.menu.music"), (OnPress) -> this.minecraft.setScreen(new SoundOptionsScreen(this, this.minecraft.options))));
                default -> addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.VANILLA, HAT_SMALL, Component.translatable("kelui.menu.skins"), (OnPress) -> this.minecraft.setScreen(new SkinCustomizationScreen(this, this.minecraft.options))));
            }
            addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.realms"), (OnPress) -> this.minecraft.setScreen(new RealmsMainScreen(this))));
            y+=25;
        }
        //
        addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.mods"), (OnPress) -> this.minecraft.setScreen(new ModsScreen(this))));
        addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.VANILLA, LANGUAGE, Component.translatable("kelui.menu.language"), (OnPress) -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()))));
        y+=25;
        //
        addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("kelui.menu.quit"), (OnPress) -> this.minecraft.stop()));
        addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.VANILLA, ACCESSIBILITY, Component.translatable("options.accessibility"), (OnPress) -> this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options))));
        if(KelUI.config.getBoolean("MAIN_MENU.INFO", true)) {
            int yT = height-20;
            if(KelUI.config.getBoolean("MAIN_MENU.CREDITS", true)) {
                addRenderableWidget(new TextBox(x, yT, 210, font.lineHeight, Component.literal(KelUI.getStringCredits()), false));
                yT-=10;
            }
            if(KelUI.config.getBoolean("MAIN_MENU.VERSION", true)){
                addRenderableWidget(new TextBox(x, yT, 210, font.lineHeight, Component.literal(KelUI.getStringVersion()), false));
            }
        }
        cl.cancel();
    }


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if(!KelUI.config.getBoolean("MAIN_MENU", true)) return;
        if (this.fadeInStart == 0L && this.fading) {
            this.fadeInStart = Util.getMillis();
        }
        this.renderPanorama(guiGraphics, f);
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
