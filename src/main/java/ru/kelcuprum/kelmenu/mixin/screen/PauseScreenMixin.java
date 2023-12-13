package ru.kelcuprum.kelmenu.mixin.screen;

import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.vanilla.VanillaButton;
import ru.kelcuprum.alinlib.gui.toast.AlinaToast;
import ru.kelcuprum.kelmenu.KelMenu;
import ru.kelcuprum.kelmenu.gui.components.PlayerInfoWidget;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {

    protected PauseScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo cl) {
        if(!KelMenu.config.getBoolean("PAUSE_MENU", true)) return;
        int x = 10;

        addRenderableWidget(new PlayerInfoWidget(x, height/2-60, 210, 20, this.minecraft.getUser().getName(), true, Component.translatable("kelmenu.player.login")));
        addRenderableWidget(new VanillaButton(x, height/2-35, 210, 20, Component.translatable("menu.returnToGame"), (OnPress) -> {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
        }));
        //
        addRenderableWidget(new VanillaButton(x, height/2-10, 210, 20, Component.translatable("gui.stats"), (OnPress) -> {
            this.minecraft.setScreen(new StatsScreen(this, this.minecraft.player.getStats()));
        }));
        //
        addRenderableWidget(new VanillaButton(x, height/2+15, 100, 20, Component.translatable("menu.options"), (OnPress) -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));
        addRenderableWidget(new VanillaButton(x+110, height/2+15, 100, 20, com.terraformersmc.modmenu.api.ModMenuApi.createModsButtonText(), (OnPress) -> {
            this.minecraft.setScreen(new com.terraformersmc.modmenu.gui.ModsScreen(this));
        }));
        //
        boolean isShortCommand = KelMenu.config.getBoolean("PAUSE_MENU.ENABLE_SHORT_COMMAND", false);
        boolean isSingle = this.minecraft.hasSingleplayerServer() && !this.minecraft.getSingleplayerServer().isPublished();
        addRenderableWidget(new VanillaButton(x, height/2+40, isSingle || isShortCommand ? 100 : 210, 20, Component.translatable("kelmenu.menu.language"), (OnPress) -> {
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }));
        if(isSingle) addRenderableWidget(new VanillaButton(x+110, height/2+40, 100, 20, Component.translatable("menu.shareToLan"), (OnPress) -> {
            this.minecraft.setScreen(new ShareToLanScreen(this));
        }));
        else if(isShortCommand) addRenderableWidget(new VanillaButton(x+110, height/2+40, 100, 20, Localization.toText(KelMenu.config.getString("PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby")), (OnPress) -> {
            KelMenu.executeCommand(this.minecraft.player, KelMenu.config.getString("PAUSE_MENU.SHORT_COMMAND.COMMAND", "/lobby"));
        }));
        //
        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        addRenderableWidget(new VanillaButton(x, height/2+65, 210, 20, component, (OnPress) -> {
            OnPress.setActive(false);
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::onDisconnect, true);
        }));
        cl.cancel();
    }
    private void onDisconnect() {
        boolean bl = this.minecraft.isLocalServer();
        ServerData serverData = this.minecraft.getCurrentServer();
        this.minecraft.level.disconnect();
        if (bl) {
            this.minecraft.disconnect(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
        } else {
            this.minecraft.disconnect();
        }

        TitleScreen titleScreen = new TitleScreen();
        if (bl) {
            this.minecraft.setScreen(titleScreen);
        } else if (serverData != null && serverData.isRealm()) {
            this.minecraft.setScreen(new RealmsMainScreen(titleScreen));
        } else {
            this.minecraft.setScreen(new JoinMultiplayerScreen(titleScreen));
        }

    }


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo cl) {
        if(!KelMenu.config.getBoolean("PAUSE_MENU", true)) return;
        if(KelMenu.config.getBoolean("PAUSE_MENU.ALPHA", true)){
            InterfaceUtils.renderLeftPanel(guiGraphics, 230, this.height);
        } else {
            InterfaceUtils.renderTextureLeftPanel(guiGraphics, 230, this.height);
        }
        if(KelMenu.config.getBoolean("PAUSE_MENU.VERSION", true)) {
            guiGraphics.drawString(this.font, String.format("Minecraft %s", Minecraft.getInstance().getLaunchedVersion()), 12, this.height - (10 * 2)-10, 0xFFFFFF);
            guiGraphics.drawString(this.font, Component.translatable("title.credits"), 12, this.height - 20, 0xFFFFFF);
        }


        if(KelMenu.config.getBoolean("PAUSE_MENU.PLAYER", true)) {
            int k = width-225;
            int l = height/2-112;
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, k, l, k + 224, l + 224, 75, 0.0625F, i, j, this.minecraft.player);
        }

        super.render(guiGraphics, i, j, f);
        cl.cancel();
    }
}
