package ru.kelcuprum.kelui.mixin.screen;

import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.components.PlayerHeadWidget;

import static ru.kelcuprum.kelui.KelUI.ICONS.LANGUAGE;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {

    protected PauseScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo cl) {
        if(!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
        int x = 10;

        addRenderableWidget(new PlayerHeadWidget(x, height/2-60, 20, 20));
        addRenderableWidget(new Button(x+25, height/2-60, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("menu.returnToGame"), (OnPress) -> {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
        }));
        //
        addRenderableWidget(new ButtonSprite(x, height/2-35, 20, 20, InterfaceUtils.DesignType.FLAT, InterfaceUtils.Icons.LIST, Component.translatable("gui.stats"), (OnPress) -> {
            this.minecraft.setScreen(new StatsScreen(this, this.minecraft.player.getStats()));
        }));
        addRenderableWidget(new Button(x+25, height/2-35, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("gui.advancements"), (OnPress) -> {
            this.minecraft.setScreen(new AdvancementsScreen(this.minecraft.getConnection().getAdvancements()));
        }));
        //
        addRenderableWidget(new ButtonSprite(x, height/2-10, 20, 20, InterfaceUtils.DesignType.FLAT, InterfaceUtils.Icons.OPTIONS, Component.translatable("kelui.menu.options"), (OnPress) -> {
            this.minecraft.setScreen(KelUI.getOptionScreen(this));
        }));
        addRenderableWidget(new Button(x+25, height/2-10, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("kelui.menu.mods"), (OnPress) -> {
            this.minecraft.setScreen(new com.terraformersmc.modmenu.gui.ModsScreen(this));
        }));
        // Line
        boolean isShortCommand = KelUI.config.getBoolean("PAUSE_MENU.ENABLE_SHORT_COMMAND", false);
        boolean isSingle = this.minecraft.hasSingleplayerServer() && !this.minecraft.getSingleplayerServer().isPublished();
        addRenderableWidget(new ButtonSprite(x, height/2+15, 20, 20, InterfaceUtils.DesignType.FLAT, LANGUAGE, Component.translatable("kelui.menu.language"), (OnPress) -> {
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }));
        if(isSingle || !isShortCommand) addRenderableWidget(new Button(x+25, height/2+15, 185, 20, InterfaceUtils.DesignType.FLAT, Component.translatable("menu.shareToLan"), (OnPress) -> {
            this.minecraft.setScreen(new ShareToLanScreen(this));
        }).setActive(isSingle));
        else addRenderableWidget(new Button(x+25, height/2+15, 185, 20, InterfaceUtils.DesignType.FLAT, Localization.toText(KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby")), (OnPress) -> {
            KelUI.executeCommand(this.minecraft.player, KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.COMMAND", "/lobby"));
        }));
        //
        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        addRenderableWidget(new Button(x, height/2+40, 210, 20, InterfaceUtils.DesignType.FLAT, component, (OnPress) -> {
            OnPress.setActive(false);
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::onDisconnect, true);
        }));
        if(KelUI.config.getBoolean("PAUSE_MENU.VERSION", true)) {
            addRenderableWidget(new TextBox(x, height - 30, 210, font.lineHeight, Component.literal(KelUI.getStringCredits()), false));
            addRenderableWidget(new TextBox(x, height - 20, 210, font.lineHeight, Component.literal(KelUI.getStringVersion()), false));
        }
        cl.cancel();
    }
    @Shadow
    @Final
    protected abstract void onDisconnect();


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if(!KelUI.config.getBoolean("PAUSE_MENU", true)) return;
        if(KelUI.config.getBoolean("PAUSE_MENU.ALPHA", true)){
            InterfaceUtils.renderLeftPanel(guiGraphics, 230, this.height);
        } else {
            InterfaceUtils.renderTextureLeftPanel(guiGraphics, 230, this.height);
        }


        if(KelUI.config.getBoolean("PAUSE_MENU.PLAYER", true)) {
            int i1 = (width - 150) / 2;
            int x = i1 + 150;
            assert this.minecraft != null;
            assert this.minecraft.player != null;
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, x, 0, width, height, (width-x)/3, 0.0625F, (float) width /2, (float) height /2, this.minecraft.player);
        }

        super.render(guiGraphics, i, j, f);
        cl.cancel();
    }
}
