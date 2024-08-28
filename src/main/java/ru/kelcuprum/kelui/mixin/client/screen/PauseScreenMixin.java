package ru.kelcuprum.kelui.mixin.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.Colors;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.components.*;
import ru.kelcuprum.kelui.gui.components.comp.CatalogueButtons;
import ru.kelcuprum.kelui.gui.components.comp.FlashbackButtons;
import ru.kelcuprum.kelui.gui.components.comp.ModMenuButtons;
import ru.kelcuprum.kelui.gui.screen.pause_screen.DisconnectScreen;
import ru.kelcuprum.kelui.gui.screen.pause_screen.OtherScreen;

import java.util.Objects;

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
    @Unique boolean oneshot$otherMenuEnable = false;
    @Unique boolean oneshot$disconnectMenuEnable = false;

    @Inject(method = "init", at = @At("RETURN"))
    void init(CallbackInfo cl) {
        if (!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
        clearWidgets();
        if(!showPauseMenu) return;
        menuType = KelUI.config.getNumber("PAUSE_MENU.TYPE", 0).intValue();
        switch (menuType) {
            case 1 -> kelui$oneShotStyle();
            case 2 -> KelUI.log("Чувак, ты думал тут что-то будет?");
            default -> kelui$defaultStyle();
        }
    }
    @Unique
    public void onClose(){
        if(menuType == 1) Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("kelui", "oneshot_menu_cancel")), 1.0F));
        super.onClose();
    }
    @Unique
    void kelui$defaultStyle() {
        int x = 10;
        int y = height / 2 - 60;
        if(KelUI.isFlashbackInstalled()){
            if(FlashbackButtons.isShow()) y -= 25;
        }

        assert this.minecraft != null;
        addRenderableWidget(new PlayerHeadWidget(x, y, 20, 20));
        addRenderableWidget(new ButtonBuilder(Component.translatable("menu.returnToGame"), (OnPress) -> {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
        }).setPosition(x + 25, y).setSize(185, 20).build());
        y+=25;
        //

        addRenderableWidget(new ButtonBuilder(Component.translatable("gui.stats"), (OnPress) -> {
            assert this.minecraft.player != null;
            this.minecraft.setScreen(new StatsScreen(this, this.minecraft.player.getStats()));
        }).setSprite(LIST).setPosition(x, y).setSize(20, 20).build());
        addRenderableWidget(new ButtonBuilder(Component.translatable("gui.advancements"), (OnPress) -> this.minecraft.setScreen(new AdvancementsScreen(Objects.requireNonNull(this.minecraft.getConnection()).getAdvancements())))
                .setPosition(x + 25, y).setSize(185, 20).build());
        y += 25;
        if(KelUI.isModMenuInstalled()) {
            addRenderableWidget(new ButtonBuilder(Component.translatable("menu.options"), (OnPress) -> this.minecraft.setScreen(KelUI.getOptionScreen(this)))
                    .setSprite(OPTIONS).setPosition(x, y).setSize(20, 20).build());
            addRenderableWidget(ModMenuButtons.getModMenuButton().setPosition(x + 25, y).setSize(185, 20).build());
        } else if(KelUI.isCatalogueInstalled()) {
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
                    .setPosition(x+25, y).setSize(185, 20).build());
        else
            addRenderableWidget(new ButtonBuilder(Localization.toText(KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby")), (OnPress) -> KelUI.executeCommand(this.minecraft.player, KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.COMMAND", "/lobby")))
                    .setPosition(x+25, y).setSize(185, 20).build());
        y+=25;
        //
        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        this.disconnectButton = net.minecraft.client.gui.components.Button.builder(component, (s) -> {}).build();
        addRenderableWidget(new ButtonBuilder(component, (OnPress) -> {
            OnPress.setActive(false);
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::onDisconnect, true);
        }).setPosition(x, y).setSize(210, 20).build());
        y+=25;
        if(KelUI.isFlashbackInstalled()){
            if(FlashbackButtons.isShow()){
                y+=1;
                addRenderableWidget(new TextBox(x, y, 210, 9, Component.literal("Flashback"), true));
                y+=14;
                if(FlashbackButtons.isRecord()){
                    addRenderableWidget(FlashbackButtons.getStateButton().setSprite(RECORD).setWidth(20).setPosition(x, y).build());
                    addRenderableWidget(FlashbackButtons.getPauseStateButton().setSprite(FlashbackButtons.isPaused() ? PLAY : PAUSE).setWidth(20).setPosition(x+25, y).build());
                    addRenderableWidget(FlashbackButtons.getCancelButton().setWidth(160).setPosition(x+50, y).build());
                } else {
                    addRenderableWidget(FlashbackButtons.getStateButton().setWidth(210).setPosition(x, y).build());
                }
            }
        }

        if (KelUI.config.getBoolean("PAUSE_MENU.INFO", true)) {
            int yT = height - 20;
            if (KelUI.config.getBoolean("PAUSE_MENU.CREDITS", false)) {
                addRenderableWidget(new TextBox(x, yT, 210, font.lineHeight, Component.literal(KelUI.getStringCredits()), false));
                yT -= 10;
            }
            if (KelUI.config.getBoolean("PAUSE_MENU.VERSION", true)) {
                addRenderableWidget(new TextBox(x, yT, 210, font.lineHeight, Component.literal(KelUI.getStringVersion()), false));
            }
        }
    }
    @Unique
    void kelui$oneShotStyle() {
        // 86
        int size = (width - 24 - 10)/3;
        AbstractWidget helloControlify = addRenderableWidget(new ButtonBuilder(Component.translatable("menu.returnToGame")).setPosition(-20, -20).setSize(20, 20).build());
        helloControlify.visible = helloControlify.active = false;

        addRenderableWidget(new OneShotPauseButton(12, 12, size, 24, Component.translatable("menu.options"), (s) -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(KelUI.getOptionScreen(this));
        }));

        if(KelUI.config.getBoolean("PAUSE_MENU.ONESHOT.OTHER", true) || (!KelUI.isModMenuInstalled() && !KelUI.isCatalogueInstalled())) addRenderableWidget(new OneShotPauseButton(width / 2 - size / 2, 12, size, 24, Component.translatable("kelui.config.title.other"), (s) -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(new OtherScreen(this));
        }));
        else if(KelUI.isModMenuInstalled()) addRenderableWidget(ModMenuButtons.getModMenuOneShotButtonPause(width / 2 - size / 2, 12, size, 24, (s) -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(ModMenuButtons.getModScreen());
        }));
        else if(KelUI.isCatalogueInstalled()) addRenderableWidget(CatalogueButtons.getModMenuOneShotButtonPause(width / 2 - size / 2, 12, size, 24, (s) -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(CatalogueButtons.getModScreen());
        }));

        assert this.minecraft != null;
        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        this.disconnectButton = net.minecraft.client.gui.components.Button.builder(component, (s) -> {}).build();

        addRenderableWidget(new OneShotPauseButton(width - 12 - size, 12, size, 24, component, (s) -> {
            if (KelUI.config.getBoolean("PAUSE_MENU.ONESHOT.QUIT_QUESTION", true)) {
                this.minecraft.setScreen(new DisconnectScreen(this, this::onDisconnect));
            }
            else onDisconnect();
        }));
    }
    @Shadow
    @Final
    protected abstract void onDisconnect();

    @Shadow @Final private boolean showPauseMenu;

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    void renderBackground(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if (!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
        if (!showPauseMenu) return;
        super.renderBackground(guiGraphics, i, j, f);

        if (menuType == 0) {
            guiGraphics.fill(0, 0, 230, this.height, Colors.BLACK_ALPHA);
        } else {
            if(!oneshot$disconnectMenuEnable && !oneshot$otherMenuEnable) {
                guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath("kelui", "pause_menu/oneshot_pause_panel"), 5, 5,  width-10, 38);
                int nikoSize = height/3;
                if(KelUI.isAprilFool() || KelUI.config.getBoolean("PAUSE_MENU.ONESHOT.NIKO_ROOMBA", false)) guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath("kelui", "pause_menu/niko_roomba"), width/2-nikoSize/2, height/2-nikoSize/2, nikoSize, nikoSize);
            }
            else guiGraphics.fill(0, 0, width, height, 0x7f000000);
        }
        cl.cancel();
    }

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
