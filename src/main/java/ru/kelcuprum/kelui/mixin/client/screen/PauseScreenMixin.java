package ru.kelcuprum.kelui.mixin.client.screen;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
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
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.components.*;
import ru.kelcuprum.kelui.gui.screen.pause_screen.DisconnectScreen;
import ru.kelcuprum.kelui.gui.screen.pause_screen.OtherScreen;

import java.util.ArrayList;
import java.util.Objects;

import static ru.kelcuprum.kelui.KelUI.ICONS.LANGUAGE;

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

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo cl) {
        if (!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
        menuType = KelUI.config.getNumber("PAUSE_MENU.TYPE", 0).intValue();
        switch (menuType) {
            case 1 -> kelui$oneShotStyle();
            case 2 -> KelUI.log("Чувак, ты думал тут что-то будет?");
            default -> kelui$defaultStyle();
        }
        cl.cancel();
    }

    @Unique
    void kelui$defaultStyle() {
        int x = 10;

        assert this.minecraft != null;
        addRenderableWidget(new PlayerHeadWidget(x, height / 2 - 60, 20, 20));
        addRenderableWidget(new Button(x + 25, height / 2 - 60, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("menu.returnToGame"), (OnPress) -> {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
        }));
        //
        addRenderableWidget(new ButtonSprite(x, height / 2 - 35, 20, 20, InterfaceUtils.DesignType.FLAT, InterfaceUtils.Icons.LIST, Component.translatable("gui.stats"), (OnPress) -> {
            assert this.minecraft.player != null;
            this.minecraft.setScreen(new StatsScreen(this, this.minecraft.player.getStats()));
        }));
        addRenderableWidget(new Button(x + 25, height / 2 - 35, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("gui.advancements"), (OnPress) -> this.minecraft.setScreen(new AdvancementsScreen(Objects.requireNonNull(this.minecraft.getConnection()).getAdvancements()))));
        //
        addRenderableWidget(new ButtonSprite(x, height / 2 - 10, 20, 20, InterfaceUtils.DesignType.FLAT, InterfaceUtils.Icons.OPTIONS, Component.translatable("menu.options"), (OnPress) -> this.minecraft.setScreen(KelUI.getOptionScreen(this))));
        addRenderableWidget(new Button(x + 25, height / 2 - 10, 185, 20, InterfaceUtils.DesignType.FLAT, ModMenuApi.createModsButtonText(), (OnPress) -> this.minecraft.setScreen(new com.terraformersmc.modmenu.gui.ModsScreen(this))));
        // Line
        boolean isShortCommand = KelUI.config.getBoolean("PAUSE_MENU.ENABLE_SHORT_COMMAND", false);
        boolean isSingle = this.minecraft.hasSingleplayerServer() && !Objects.requireNonNull(this.minecraft.getSingleplayerServer()).isPublished();
        addRenderableWidget(new ButtonSprite(x, height / 2 + 15, 20, 20, InterfaceUtils.DesignType.FLAT, LANGUAGE, Component.translatable("options.language"), (OnPress) -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()))));
        if (isSingle || !isShortCommand)
            addRenderableWidget(new Button(x + 25, height / 2 + 15, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("menu.shareToLan"), (OnPress) -> this.minecraft.setScreen(new ShareToLanScreen(this))).setActive(isSingle));
        else
            addRenderableWidget(new Button(x + 25, height / 2 + 15, 185, 20, InterfaceUtils.DesignType.FLAT, Localization.toText(KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby")), (OnPress) -> KelUI.executeCommand(this.minecraft.player, KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.COMMAND", "/lobby"))));
        //
        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        this.disconnectButton = net.minecraft.client.gui.components.Button.builder(component, (s) -> {}).build();
        addRenderableWidget(new Button(x, height / 2 + 40, 210, 20, InterfaceUtils.DesignType.FLAT, component, (OnPress) -> {
            OnPress.setActive(false);
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::onDisconnect, true);
        }));
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
        AbstractWidget helloControlify = addRenderableWidget(new Button(-20, -20, 20, 20, Component.translatable("menu.returnToGame")));
        helloControlify.visible = helloControlify.active = false;

        addRenderableWidget(new OneShotPauseButton(12, 12, size, 24, Component.translatable("menu.options"), (s) -> this.minecraft.setScreen(KelUI.getOptionScreen(this))));

        addRenderableWidget(new OneShotPauseButton(width / 2 - size / 2, 12, size, 24, Component.translatable("kelui.config.title.other"), (s) -> this.minecraft.setScreen(new OtherScreen(this))));

        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        this.disconnectButton = net.minecraft.client.gui.components.Button.builder(component, (s) -> {}).build();

        addRenderableWidget(new OneShotPauseButton(width - 12 - size, 12, size, 24, component, (s) -> {
            if (KelUI.config.getBoolean("PAUSE_MENU.ONESHOT.QUIT_QUESTION", true)) this.minecraft.setScreen(new DisconnectScreen(this, this::onDisconnect));
            else onDisconnect();
        }));
    }
    @Shadow
    @Final
    protected abstract void onDisconnect();

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    void renderBackground(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        super.renderBackground(guiGraphics, i, j, f);
        if (!KelUI.config.getBoolean("PAUSE_MENU", true)) return;

        if (menuType == 0) {
            if (KelUI.config.getBoolean("PAUSE_MENU.ALPHA", true)) {
                InterfaceUtils.renderLeftPanel(guiGraphics, 230, this.height);
            } else {
                InterfaceUtils.renderTextureLeftPanel(guiGraphics, 230, this.height);
            }
        } else {
            if(!oneshot$disconnectMenuEnable && !oneshot$otherMenuEnable) {
                guiGraphics.blitSprite(new ResourceLocation("kelui", "pause_menu/oneshot_pause_panel"), 5, 5,  width-10, 38);
                int nikoSize = height/3;
                if(KelUI.isAprilFool() || KelUI.config.getBoolean("PAUSE_MENU.ONESHOT.NIKO_ROOMBA", false)) guiGraphics.blitSprite(new ResourceLocation("kelui", "pause_menu/niko_roomba"), width/2-nikoSize/2, height/2-nikoSize/2, nikoSize, nikoSize);
            }
            else guiGraphics.fill(0, 0, width, height, 0x7f000000);
        }
        cl.cancel();
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        super.render(guiGraphics, i, j, f);

        if (menuType == 0) {
            if (!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
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
