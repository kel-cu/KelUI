package ru.kelcuprum.kelui.mixin.client.screen;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.Colors;
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.text.TextBuilder;
import ru.kelcuprum.alinlib.info.Player;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.components.*;
import ru.kelcuprum.kelui.gui.components.comp.CatalogueButtons;
import ru.kelcuprum.kelui.gui.components.comp.FlashbackButtons;
import ru.kelcuprum.kelui.gui.components.comp.ModMenuButtons;
import ru.kelcuprum.kelui.gui.components.omori.OMORIButton;
import ru.kelcuprum.kelui.gui.components.oneshot.OneShotPauseButton;
import ru.kelcuprum.kelui.gui.screen.pause_screen.OtherScreen;

import java.util.Objects;

import static net.minecraft.client.gui.screens.PauseScreen.disconnectFromWorld;
import static ru.kelcuprum.alinlib.gui.Colors.CPM_BLUE;
import static ru.kelcuprum.alinlib.gui.Colors.GROUPIE;
import static ru.kelcuprum.alinlib.gui.Icons.*;
import static ru.kelcuprum.kelui.KelUI.ICONS.*;

@Mixin(value = PauseScreen.class, priority = -1)
public abstract class PauseScreenMixin extends Screen {

    protected PauseScreenMixin() {
        super(null);
    }

    @Shadow
    @Nullable
    private net.minecraft.client.gui.components.Button disconnectButton;

    @Unique
    public int menuType = 0;
    @Unique
    boolean oneshot$otherMenuEnable = false;
    @Unique
    boolean oneshot$disconnectMenuEnable = false;
    @Unique
    Button statsButton;

    @Inject(method = "init", at = @At("RETURN"))
    void init(CallbackInfo cl) {
        if (!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
        statsButton = getStatsButton();
        clearWidgets();
        if (!showPauseMenu) return;
        menuType = KelUI.config.getNumber("PAUSE_MENU.TYPE", 0).intValue();
        switch (menuType) {
            case 1 -> kelui$oneShotStyle();
            case 2 -> kelui$omoriStyle();
            case 3 -> KelUI.log("Чувак, ты думал тут что-то будет?");
            default -> kelui$defaultStyle();
        }
    }

    @Unique
    Button getStatsButton(){
        Button button = null;
        for(GuiEventListener widget : this.children){
            if(widget instanceof AbstractWidget){
                if(((AbstractWidget) widget).getMessage().contains(Component.translatable("gui.stats")))
                    button = (Button) widget;
            }
        }
        return button;
    }

    @Unique
    public void onClose() {
        if (menuType == 1)
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("kelui", "oneshot_menu_cancel")), 1.0F));
        super.onClose();
    }
    @Unique int startY = 0;
    @Unique int endY = 0;

    @Unique
    void kelui$defaultStyle() {
        int x = 10;
        int y = height / 2 - 60;
        if (KelUI.isFlashbackInstalled()) {
            if (FlashbackButtons.isShow()) y -= 25;
        }

        startY = y;

        assert this.minecraft != null;
        addRenderableWidget(new PlayerHeadWidget(x, y, 20, 20));
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.returnToGame"), (OnPress) -> {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
        }).setPosition(x + 25, y).setSize(185, 20).build());
        y += 25;
        //

        addRenderableWidget(new ButtonBuilder(Component.translatable("gui.stats"), (OnPress) -> {
            assert this.minecraft.player != null;
            if(statsButton != null) statsButton.onPress();
            else this.minecraft.setScreen(new StatsScreen(this, this.minecraft.player.getStats()));
        }).setSprite(LIST).setPosition(x, y).setSize(20, 20).build());
        addRenderableWidget(new ButtonBuilder(Component.translatable("gui.advancements"), (OnPress) -> this.minecraft.setScreen(new AdvancementsScreen(Objects.requireNonNull(this.minecraft.getConnection()).getAdvancements())))
                .setPosition(x + 25, y).setSize(185, 20).build());
        y += 25;
        if (KelUI.isModMenuInstalled()) {
            addRenderableWidget(new ButtonBuilder(Component.translatable("menu.options"), (OnPress) -> this.minecraft.setScreen(KelUI.getOptionScreen(this)))
                    .setSprite(OPTIONS).setPosition(x, y).setSize(20, 20).build());
            addRenderableWidget(ModMenuButtons.getModMenuButton().setPosition(x + 25, y).setSize(185, 20).build());
        } else if (KelUI.isCatalogueInstalled()) {
            addRenderableWidget(new ButtonBuilder(Component.translatable("menu.options"), (OnPress) -> this.minecraft.setScreen(KelUI.getOptionScreen(this)))
                    .setSprite(OPTIONS).setPosition(x, y).setSize(20, 20).build());
            addRenderableWidget(CatalogueButtons.getModMenuButton().setPosition(x + 25, y).setSize(185, 20).build());
        } else {
            addRenderableWidget(new ButtonBuilder(Component.translatable("menu.options"), (OnPress) -> this.minecraft.setScreen(KelUI.getOptionScreen(this)))
                    .setIcon(OPTIONS).setPosition(x, y).setSize(210, 20).build());
        }
        y += 25;
        // Line
        boolean isShortCommand = KelUI.config.getBoolean("PAUSE_MENU.ENABLE_SHORT_COMMAND", false);
        boolean isSingle = this.minecraft.hasSingleplayerServer() && !Objects.requireNonNull(this.minecraft.getSingleplayerServer()).isPublished();
        addRenderableWidget(new ButtonBuilder(Component.translatable("options.language"), (OnPress) -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())))
                .setSprite(LANGUAGE).setPosition(x, y).setSize(20, 20).build());
        if (isSingle || !isShortCommand)
            addRenderableWidget(new ButtonBuilder(Component.translatable("menu.shareToLan"), (OnPress) -> this.minecraft.setScreen(new ShareToLanScreen(this))).setActive(isSingle)
                    .setPosition(x + 25, y).setSize(185, 20).build());
        else
            addRenderableWidget(new ButtonBuilder(Localization.toText(KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby")), (OnPress) -> KelUI.executeCommand(this.minecraft.player, KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.COMMAND", "/lobby")))
                    .setPosition(x + 25, y).setSize(185, 20).build());
        y += 25;
        //
        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        this.disconnectButton = net.minecraft.client.gui.components.Button.builder(component, (s) -> {
        }).build();
        addRenderableWidget(new ButtonBuilder(component, (OnPress) -> {
            OnPress.setActive(false);
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, () -> disconnectFromWorld(this.minecraft, ClientLevel.DEFAULT_QUIT_MESSAGE), true);
        }).setPosition(x, y).setSize(210, 20).build());
        if (KelUI.isFlashbackInstalled()) {
            if (FlashbackButtons.isShow()) {
                y += 26;
                addRenderableWidget(new TextBuilder(Component.literal("Flashback")).setPosition(x, y).setSize(210, 9).build());
                y += 14;
                if (FlashbackButtons.isRecord()) {
                    addRenderableWidget(FlashbackButtons.getStateButton().setSprite(RECORD).setWidth(20).setPosition(x, y).build());
                    addRenderableWidget(FlashbackButtons.getPauseStateButton().setSprite(FlashbackButtons.isPaused() ? PLAY : PAUSE).setWidth(20).setPosition(x + 25, y).build());
                    addRenderableWidget(FlashbackButtons.getCancelButton().setWidth(160).setPosition(x + 50, y).build());
                } else {
                    addRenderableWidget(FlashbackButtons.getStateButton().setWidth(210).setPosition(x, y).build());
                }
            }
        }
        y+= 25;
        endY = y;

        if (KelUI.config.getBoolean("PAUSE_MENU.INFO", true)) {
            int yT = height - 20;
            if (KelUI.config.getBoolean("PAUSE_MENU.CREDITS", false)) {
                addRenderableWidget(new TextBuilder(Component.literal(KelUI.getStringCredits())).setAlign(TextBuilder.ALIGN.LEFT).setPosition(x, yT).setSize(210, font.lineHeight).build());
                yT -= 10;
            }
            if (KelUI.config.getBoolean("PAUSE_MENU.VERSION", true)) {
                addRenderableWidget(new TextBuilder(Component.literal(KelUI.getStringVersion())).setAlign(TextBuilder.ALIGN.LEFT).setPosition(x, yT).setSize(210, font.lineHeight).build());
            }
        }
    }

    @Unique
    void kelui$oneShotStyle() {
        // 86
        int size = (width - 24 - 10) / 3;
        AbstractWidget helloControlify = addRenderableWidget(new ButtonBuilder(Component.translatable("menu.returnToGame")).setPosition(-20, -20).setSize(20, 20).build());
        helloControlify.visible = helloControlify.active = false;

        addRenderableWidget(new OneShotPauseButton(12, 12, size, 24, Component.translatable("menu.options"), (s) -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(KelUI.getOptionScreen(this));
        }));

        if (KelUI.config.getBoolean("PAUSE_MENU.ONESHOT.OTHER", true) || (!KelUI.isModMenuInstalled() && !KelUI.isCatalogueInstalled()))
            addRenderableWidget(new OneShotPauseButton(width / 2 - size / 2, 12, size, 24, Component.translatable("kelui.config.title.other"), (s) -> {
                assert this.minecraft != null;
                this.minecraft.setScreen(new OtherScreen(this));
            }));
        else if (KelUI.isModMenuInstalled())
            addRenderableWidget(ModMenuButtons.getModMenuOneShotButtonPause(width / 2 - size / 2, 12, size, 24, (s) -> {
                assert this.minecraft != null;
                this.minecraft.setScreen(ModMenuButtons.getModScreen());
            }));
        else if (KelUI.isCatalogueInstalled())
            addRenderableWidget(CatalogueButtons.getModMenuOneShotButtonPause(width / 2 - size / 2, 12, size, 24, (s) -> {
                assert this.minecraft != null;
                this.minecraft.setScreen(CatalogueButtons.getModScreen());
            }));

        assert this.minecraft != null;
        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        this.disconnectButton = net.minecraft.client.gui.components.Button.builder(component, (s) -> {
        }).build();

        addRenderableWidget(new OneShotPauseButton(width - 12 - size, 12, size, 24, component, (s) -> {
            if (KelUI.config.getBoolean("PAUSE_MENU.ONESHOT.QUIT_QUESTION", true)) {
                disconnectFromWorld(minecraft, ClientLevel.DEFAULT_QUIT_MESSAGE);
            } else disconnectFromWorld(minecraft, ClientLevel.DEFAULT_QUIT_MESSAGE);
        }));
    }

    @Unique
    void kelui$omoriStyle(){
        int widgetsMaxWidth = width - 18;
        int widgetWidth = widgetsMaxWidth/5;
        int y = 19 - (font.lineHeight / 2);
        int x = 9;
        boolean isShortCommand = KelUI.config.getBoolean("PAUSE_MENU.ENABLE_SHORT_COMMAND", false);
        boolean isSingle = this.minecraft.hasSingleplayerServer() && !Objects.requireNonNull(this.minecraft.getSingleplayerServer()).isPublished();
        OMORIButton b1 = addRenderableWidget(new OMORIButton(x, y, widgetWidth, font.lineHeight+2, Component.literal(!isSingle && isShortCommand ? KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby") : "???"), false, (s) -> {
            KelUI.executeCommand(this.minecraft.player, KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.COMMAND", "/lobby"));
        }));
        b1.isActive = !isSingle && isShortCommand;
        addRenderableWidget(new OMORIButton(x+widgetWidth, y, widgetWidth, font.lineHeight+2, Component.translatable("kelui.config.title.other"), false, (s) -> {
            this.minecraft.setScreen(new OtherScreen(this));
        }));
        addRenderableWidget(new OMORIButton(x+(widgetWidth*2), y, widgetWidth, font.lineHeight+2, Component.translatable("gui.advancements"), false, (s) -> {
            this.minecraft.setScreen(new AdvancementsScreen(Objects.requireNonNull(this.minecraft.getConnection()).getAdvancements()));
        }));
        addRenderableWidget(new OMORIButton(x+(widgetWidth*3), y, widgetWidth, font.lineHeight+2, Component.translatable("menu.options"), false, (s) -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));
        this.disconnectButton = net.minecraft.client.gui.components.Button.builder(this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT, (s) -> {
        }).build();
        addRenderableWidget(new OMORIButton(x+(widgetWidth*4), y, widgetWidth, font.lineHeight+2, this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT, false, (s) -> {
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, () -> disconnectFromWorld(this.minecraft, ClientLevel.DEFAULT_QUIT_MESSAGE), true);
        }));
    }


    @Shadow
    @Final
    private boolean showPauseMenu;

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    void renderBackground(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if (!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
        if (!showPauseMenu) return;
        if(menuType != 2) super.renderBackground(guiGraphics, i, j, f);
        else renderBlurredBackground(guiGraphics);

        if (menuType == 0) {
            boolean isWindows = GuiUtils.getSelected().id.equals("windows");
            GuiUtils.getSelected().renderBackground(guiGraphics, 5, startY-5-(isWindows ? 19 : 0), 225, endY);
            if(isWindows){
                GuiUtils.getSelected().renderTitleBackground(guiGraphics, 8, startY-21, 221, startY-3);
                guiGraphics.drawString(font, getTitle(), 13, startY-16, -1, false);
            }
        } else if(menuType == 1){
            if (!oneshot$disconnectMenuEnable && !oneshot$otherMenuEnable) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath("kelui", "pause_menu/oneshot_pause_panel"), 5, 5, width - 10, 38);
                int nikoSize = height / 3;
                if (KelUI.isAprilFool() || KelUI.config.getBoolean("PAUSE_MENU.ONESHOT.NIKO_ROOMBA", false))
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath("kelui", "pause_menu/niko_roomba"), width / 2 - nikoSize / 2, height / 2 - nikoSize / 2, nikoSize, nikoSize);
            } else guiGraphics.fill(0, 0, width, height, 0x7f000000);
        } else {
            int x = 5;
            int y = 5;
            int x1 = guiGraphics.guiWidth()-5;
            int y1 = 35;
            renderPanel(guiGraphics, x, y, x1, y1);
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, x, height-225, 150, height, 100, 0.0625F, 112.5F, height-100, this.minecraft.player);
            int x2 = 5;
            int y2 = height-111;
            int x3 = 200;
            int y3 = height;
            guiGraphics.fill(x2, y2, x3, y3, 0xFF000000);
            guiGraphics.fill(x2+2, y2+2, x3-2, y3-2, 0xFFd3d3d3);
            guiGraphics.renderOutline(x2+4, y2+3, x3-13, 20, 0xFFf3f3f3);
            guiGraphics.renderOutline(x2+4, y2+4, x3-13, 20, 0xFF3d3d3d);
            guiGraphics.drawString(font, Player.getName(), x2+10, y2+10, 0xFF000000, false);
            guiGraphics.fill(x2+2, y2+26, x3-2, y2+27, 0xFFf3f3f3);
            guiGraphics.fill(x2, y2+27, x3, y2+47, 0xFF000000);
            guiGraphics.drawString(font, String.format("LVL. %s", minecraft.player.experienceLevel), x2+10, y2+33, 0xFFffffff, false);
            // y2+47 -- конец черной рамки
            // y2+77 -- конец секции здоровья
            // x2+32 -- начало индикатора
            int indicatorHealth = (int) (((x3-7)-(x2+32)) * (AlinLib.MINECRAFT.player.getHealth()/AlinLib.MINECRAFT.player.getAttributeValue(Attributes.MAX_HEALTH)));
            guiGraphics.fill(x2+32, y2+52, x2+32+indicatorHealth, y2+72, GROUPIE);
            guiGraphics.renderOutline(x2+32, y2+52, ((x3-7)-(x2+32)), 20, 0xFF000000);
            String health = String.format("%s/%s", Localization.getRounding(AlinLib.MINECRAFT.player.getHealth(), true), Localization.getRounding(AlinLib.MINECRAFT.player.getAttributeValue(Attributes.MAX_HEALTH), true));
            guiGraphics.drawString(font, health, (x3-7)-5-font.width(health), y2+58, 0xFFffffff);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiUtils.getResourceLocation("kelui", "textures/gui/omori/health.png"), x2+7, y2+52, 0, 0, 20, 20, 20, 20);
            guiGraphics.fill(x2+2, y2+76, x3-2, y2+77, 0xFFf3f3f3);
            guiGraphics.fill(x2+2, y2+77, x3-2, y2+78, 0xFF000000);
            // y2+79 -- начало секции здоровья
            int indicatorHunger = (int) (((x3-7)-(x2+32)) * ((double) Player.getHunger()/20));
            guiGraphics.fill(x2+32, y2+84, x2+32+indicatorHunger, y2+104, CPM_BLUE);
            guiGraphics.renderOutline(x2+32, y2+84, ((x3-7)-(x2+32)), 20, 0xFF000000);
            String hunger = String.format("%s/%s", Localization.getRounding(Player.getHunger(), true), 20);
            guiGraphics.drawString(font, hunger, (x3-7)-5-font.width(hunger), y2+90, 0xFFffffff);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiUtils.getResourceLocation("kelui", "textures/gui/omori/hunger.png"), x2+7, y2+84, 0, 0, 20, 20, 20, 20);
        }
        cl.cancel();
    }
    @Unique
    private static void renderPanel(GuiGraphics guiGraphics, int x, int y, int x1, int y1){
        guiGraphics.fill(x, y, x1, y1, 0xFFFFFFFF);
        guiGraphics.renderOutline(x, y, x1-x, y1-y, 0xFF000000);
        guiGraphics.fill(x+4, y+4, x1-4, y1-4, 0xFF000000);
    };
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if (!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
        if (!showPauseMenu) return;
        super.render(guiGraphics, i, j, f);

        if (menuType == 0) {
            if (KelUI.config.getBoolean("PAUSE_MENU.PLAYER", true)) {
                int i1 = (width - 150) / 2;
                int x = i1 + 150;
                assert this.minecraft != null;
                assert this.minecraft.player != null;
                InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, x, 0, width, height, (width - x) / 3, 0.0625F, (float) width / 2, (float) height / 2, this.minecraft.player);
            }
        }
        cl.cancel();
    }
}
