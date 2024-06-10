package ru.kelcuprum.kelui.mixin.client.screen;

import com.mojang.realmsclient.RealmsMainScreen;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.gui.ModsScreen;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
import ru.kelcuprum.kelui.gui.components.OneShotButton;
import ru.kelcuprum.kelui.gui.components.PlayerHeadWidget;

import static ru.kelcuprum.kelui.KelUI.ICONS.*;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Shadow
    private long fadeInStart;
    @Shadow
    private boolean fading;

    @Shadow protected abstract void renderPanorama(GuiGraphics guiGraphics, float f);


    protected TitleScreenMixin() {
        super(null);
    }
    @Unique public int menuType = 0;
    @Unique boolean isGameStarted = false;
    @Unique boolean isPlayedSound = false;

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo cl) {
        if(!KelUI.config.getBoolean("MAIN_MENU", true)) return;
        menuType = KelUI.config.getNumber("MAIN_MENU.TYPE", 0).intValue();
        switch (menuType){
            case 1 -> {
                kelui$oneShotStyle();
                if(isGameStarted && !isPlayedSound){
                    isPlayedSound = true;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("kelui", "oneshot_menu_cancel")), 1.0F));
                }
            }
            case 2 -> KelUI.log("Чувак, ты думал тут что-то будет?");
            default -> kelui$defaultStyle();
        }
        cl.cancel();
    }
    @Unique
    public void kelui$defaultStyle(){
        int x = 10;
        //
        assert this.minecraft != null;
        int y = height/2-35;
        addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("menu.singleplayer"), (OnPress) -> this.minecraft.setScreen(new SelectWorldScreen(this))));
        addRenderableWidget(new PlayerHeadWidget(x, y, 20, 20));
        y+=25;
        //
        addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("menu.multiplayer"), (OnPress) -> this.minecraft.setScreen(new JoinMultiplayerScreen(this))));
        addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.FLAT, InterfaceUtils.Icons.OPTIONS, Component.translatable("menu.options"), (OnPress) -> this.minecraft.setScreen(KelUI.getOptionScreen(this))));
        y+=25;
        //
        if(KelUI.config.getBoolean("MAIN_MENU.ENABLE_REALMS", false)){
            if (KelUI.config.getNumber("MAIN_MENU.REALMS_SMALL_BUTTON", 0).intValue() == 1) addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.FLAT, InterfaceUtils.Icons.MUSIC, Component.translatable("options.sounds"), (OnPress) -> this.minecraft.setScreen(new SoundOptionsScreen(this, this.minecraft.options))));
            else addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.FLAT, HAT_SMALL, Component.translatable("options.skinCustomisation"), (OnPress) -> this.minecraft.setScreen(new SkinCustomizationScreen(this, this.minecraft.options))));
            addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("gui.toRealms"), (OnPress) -> this.minecraft.setScreen(new RealmsMainScreen(this))));
            y+=25;
        }
        //
        addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.FLAT, ModMenuApi.createModsButtonText(), (OnPress) -> this.minecraft.setScreen(new ModsScreen(this))));
        addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.FLAT, LANGUAGE, Component.translatable("options.language"), (OnPress) -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()))));
        y+=25;
        //
        addRenderableWidget(new Button(x+25, y, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("menu.quit"), (OnPress) -> this.minecraft.stop()));
        addRenderableWidget(new ButtonSprite(x, y, 20, 20, InterfaceUtils.DesignType.FLAT, ACCESSIBILITY, Component.translatable("options.accessibility"), (OnPress) -> this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options))));
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
    }

    @Unique
    public void kelui$oneShotStyle(){
        int bHeight = font.lineHeight+4;
        int bHeight2 = (bHeight+3);
        int y = height-(bHeight2*(KelUI.config.getBoolean("MAIN_MENU.ENABLE_REALMS", false) ? 6 : 5));
        int bWidth = font.width("...");
        Component[] texts = {
                Component.translatable("menu.singleplayer"),
                Component.translatable("menu.multiplayer"),
                Component.translatable("gui.toRealms"),
                ModMenuApi.createModsButtonText(),
                Component.translatable("menu.options"),
                Component.translatable("menu.quit")
        };
        for(Component text : texts){
            int i = font.width(text)+5;
            bWidth = Math.max(bWidth, i);
        }
        int x = width-bWidth-45;

        assert this.minecraft != null;
        addRenderableWidget(new OneShotButton(x, y, bWidth, bHeight, Component.translatable("menu.singleplayer"), true, (OnPress) -> {
            this.minecraft.setScreen(new SelectWorldScreen(this));
            isPlayedSound = false;
        }));
        y+=bHeight2;

        addRenderableWidget(new OneShotButton(x, y, bWidth, bHeight, Component.translatable("menu.multiplayer"), true, (OnPress) -> {
            this.minecraft.setScreen(new JoinMultiplayerScreen(this));
            isPlayedSound = false;
        }));
        y+=bHeight2;

        if(KelUI.config.getBoolean("MAIN_MENU.ENABLE_REALMS", false)) {
            addRenderableWidget(new OneShotButton(x, y, bWidth, bHeight, Component.translatable("gui.toRealms"), (OnPress) -> {
                this.minecraft.setScreen(new RealmsMainScreen(this));
                isPlayedSound = false;
            }));
            y += bHeight2;
        }

        addRenderableWidget(new OneShotButton(x, y, bWidth, bHeight, ModMenuApi.createModsButtonText(), (OnPress) -> {
            this.minecraft.setScreen(new ModsScreen(this));
            isPlayedSound = false;
        }));
        y+=bHeight2;

        addRenderableWidget(new OneShotButton(x, y, bWidth, bHeight, Component.translatable("menu.options"), (OnPress) -> {
            this.minecraft.setScreen(KelUI.getOptionScreen(this));
            isPlayedSound = false;
        }));
        y+=bHeight2;

        addRenderableWidget(new OneShotButton(x, y, bWidth, bHeight, Component.translatable("menu.quit"), true, (OnPress) -> this.minecraft.destroy()));
    }


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if(!isGameStarted) isGameStarted = true;
        if(!KelUI.config.getBoolean("MAIN_MENU", true)) return;
        if (this.fadeInStart == 0L && this.fading) {
            this.fadeInStart = Util.getMillis();
        }
        this.renderPanorama(guiGraphics, f);
        if(menuType == 0) InterfaceUtils.renderLeftPanel(guiGraphics, 230, this.height);
        if (KelUI.config.getBoolean("FIRST_START", true)) {
            KelUI.config.setBoolean("FIRST_START", false);
            KelUI.config.save();
            new ToastBuilder().setTitle(Localization.getText("kelui.name")).setMessage(Localization.getText("kelui.toast.hello")).setIcon(Items.CRAFTING_TABLE).show(Minecraft.getInstance().getToasts());
        }
        super.render(guiGraphics, i, j, f);
        cl.cancel();
    }

}
