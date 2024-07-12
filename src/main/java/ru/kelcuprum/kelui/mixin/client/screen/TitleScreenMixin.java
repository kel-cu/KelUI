package ru.kelcuprum.kelui.mixin.client.screen;

import com.mojang.realmsclient.RealmsMainScreen;
import net.fabricmc.loader.api.FabricLoader;
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
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.Colors;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.components.*;

import static ru.kelcuprum.kelui.KelUI.ICONS.*;
import static ru.kelcuprum.alinlib.gui.Icons.*;

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

    @Inject(method = "init", at = @At("RETURN"))
    void init(CallbackInfo cl) {
        if(!KelUI.config.getBoolean("MAIN_MENU", true)) return;
        clearWidgets();
        menuType = KelUI.config.getNumber("MAIN_MENU.TYPE", 0).intValue();
        switch (menuType){
            case 1 -> kelui$defaultStyleV2();
            case 2 -> {
                kelui$oneShotStyle();
                if(isGameStarted && !isPlayedSound){
                    isPlayedSound = true;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("kelui", "oneshot_menu_cancel")), 1.0F));
                }
            }
            default -> kelui$defaultStyle();
        }
    }
    @Unique
    public void kelui$defaultStyle(){
        int x = 10;
        //
        assert this.minecraft != null;
        int y = height/2-35;
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.singleplayer"), (OnPress) -> this.minecraft.setScreen(new SelectWorldScreen(this)))
                .setPosition(x+25, y).setSize(185, 20).build());
        addRenderableWidget(new PlayerHeadWidget(x, y, 20, 20));
        y+=25;
        //
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.multiplayer"), (OnPress) -> this.minecraft.setScreen(new JoinMultiplayerScreen(this)))
                .setPosition(x+25, y).setSize(185, 20).build());
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.options"), (OnPress) -> this.minecraft.setScreen(KelUI.getOptionScreen(this)))
                .setSprite(OPTIONS).setPosition(x, y).setSize(20,20).build());
        y+=25;
        //
        if(KelUI.config.getBoolean("MAIN_MENU.ENABLE_REALMS", false)){
            if (KelUI.config.getNumber("MAIN_MENU.REALMS_SMALL_BUTTON", 0).intValue() == 1) addRenderableWidget(new ButtonBuilder(Component.translatable("options.sounds"), (OnPress) -> this.minecraft.setScreen(new SoundOptionsScreen(this, this.minecraft.options)))
                    .setSprite(MUSIC).setPosition(x, y).setSize(20,20).build());
            else addRenderableWidget(new ButtonBuilder(Component.translatable("options.skinCustomisation"), (OnPress) -> this.minecraft.setScreen(new SkinCustomizationScreen(this, this.minecraft.options)))
                    .setSprite(HAT_SMALL).setPosition(x, y).setSize(20,20).build());
            addRenderableWidget(new ButtonBuilder(Component.translatable("menu.online"), (OnPress) -> this.minecraft.setScreen(new RealmsMainScreen(this)))
                    .setPosition(x+25, y).setSize(185, 20).build());
            y+=25;
        }
        //
        if(FabricLoader.getInstance().isModLoaded("modmenu")) {
            addRenderableWidget(ModMenuButtons.getModMenuButton().setPosition(x + 25, y).setSize(185, 20).build());
            addRenderableWidget(new ButtonBuilder(Component.translatable("options.language"), (OnPress) -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())))
                    .setSprite(LANGUAGE).setPosition(x, y).setSize(20, 20).build());
        } else {
            addRenderableWidget(new ButtonBuilder(Component.translatable("options.language"), (OnPress) -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())))
                    .setIcon(LANGUAGE).setPosition(x, y).setSize(210, 20).build());
        }
        y+=25;
        //
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.quit"), (OnPress) -> this.minecraft.stop())
                .setPosition(x+25, y).setSize(185, 20).build());
        addRenderableWidget(new ButtonBuilder(Component.translatable("options.accessibility"), (OnPress) -> this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options)))
                .setSprite(ACCESSIBILITY).setPosition(x, y).setSize(20,20).build());
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

        if(KelUI.config.getBoolean("MAIN_MENU.PLAYER", true)){
            int yt = height-((height/3)*2);
            int xt = ((width-230) / 2)+230-45;
            PlayerButton pb = new PlayerButton(xt, yt, height/3);
            if(FabricLoader.getInstance().isModLoaded("skinshuffle")){
                pb.setOnPress((s) -> AlinLib.MINECRAFT.setScreen(SSButtons.getScreen()));
            };
            addRenderableWidget(pb);
        }
    }
    @Unique
    public void kelui$defaultStyleV2(){
        int x = 10;
        //
        assert this.minecraft != null;
        int y = height/2-60;
        if(KelUI.config.getBoolean("MAIN_MENU.ENABLE_REALMS", false)) y-=12;
        if(!FabricLoader.getInstance().isModLoaded("modmenu")) y+=12;

        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.singleplayer"))
                .setOnPress((s) -> this.minecraft.setScreen(new SelectWorldScreen(this)))
                .setIcon(SINGLEPLAYER)
                .setWidth(210).setPosition(x, y).build());
        y+=25;
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.multiplayer"))
                .setOnPress((s) -> this.minecraft.setScreen(new JoinMultiplayerScreen(this)))
                .setIcon(MULTIPLAYER)
                .setWidth(210).setPosition(x, y).build());
        y+=25;

        if(FabricLoader.getInstance().isModLoaded("modmenu")) {
            addRenderableWidget(ModMenuButtons.getModMenuButton()
                    .setIcon(LIST)
                    .setWidth(210).setPosition(x, y).build());
            y += 25;
        }
        if(KelUI.config.getBoolean("MAIN_MENU.ENABLE_REALMS", false)) {
            addRenderableWidget(new ButtonBuilder(Component.translatable("menu.online"))
                    .setIcon(REALMS)
                    .setOnPress((s) -> this.minecraft.setScreen(new RealmsMainScreen(this)))
                    .setWidth(210).setPosition(x, y).build());
            y += 25;
        }
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.options"))
                .setIcon(OPTIONS)
                .setOnPress((s) -> this.minecraft.setScreen(KelUI.getOptionScreen(this)))
                .setWidth(210).setPosition(x, y).build());
        y+=25;
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.quit"))
                .setIcon(CLOWNFISH)
                .setOnPress((s) -> this.minecraft.stop())
                .setWidth(210).setPosition(x, y).build());

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
        if(KelUI.config.getBoolean("MAIN_MENU.PLAYER", true)){
            int yt = height-((height/3)*2);
            int xt = ((width-230) / 2)+230-45;
            addRenderableWidget(new PlayerButton(xt, yt, height/3));
        }
    }

    @Unique
    public void kelui$oneShotStyle(){
        int bHeight = font.lineHeight+4;
        int bHeight2 = (bHeight+3);
        int size = FabricLoader.getInstance().isModLoaded("modmenu") ? 5 : 4;
        int y = height-(bHeight2*(KelUI.config.getBoolean("MAIN_MENU.ENABLE_REALMS", false) ? size+1 : size));
        int bWidth = font.width("...");
        Component[] texts = {
                Component.translatable("menu.singleplayer"),
                Component.translatable("menu.multiplayer"),
                Component.translatable("menu.online"),
                FabricLoader.getInstance().isModLoaded("modmenu") ? ModMenuButtons.getModText() : Component.empty(),
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
            addRenderableWidget(new OneShotButton(x, y, bWidth, bHeight, Component.translatable("menu.online"), (OnPress) -> {
                this.minecraft.setScreen(new RealmsMainScreen(this));
                isPlayedSound = false;
            }));
            y += bHeight2;
        }

        if(FabricLoader.getInstance().isModLoaded("modmenu")) {
            addRenderableWidget(ModMenuButtons.getModMenuOneShotButton(x, y, bWidth, bHeight, (OnPress) -> {
                this.minecraft.setScreen(ModMenuButtons.getModScreen());
                isPlayedSound = false;
            }));
            y += bHeight2;
        }

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
        if(menuType == 0 || menuType == 1) guiGraphics.fill(0, 0, 230, this.height, Colors.BLACK_ALPHA);
        if (KelUI.config.getBoolean("FIRST_START", true)) {
            KelUI.config.setBoolean("FIRST_START", false);
            KelUI.config.save();
            new ToastBuilder().setTitle(Localization.getText("kelui.name")).setMessage(Localization.getText("kelui.toast.hello")).setIcon(Items.CRAFTING_TABLE).show(Minecraft.getInstance().getToasts());
        }
        super.render(guiGraphics, i, j, f);
        cl.cancel();
    }

}
