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
    public void onClose() {
        assert this.minecraft != null;

        if(this.oneshot$disconnectMenuEnable) this.changeMenuDisconnectState();
        else if(this.oneshot$otherMenuEnable) this.changeMenuState();
        else this.minecraft.setScreen(null);
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
    @Unique ArrayList<AbstractWidget> oneshot$mainButtons = new ArrayList<>();
    @Unique ArrayList<AbstractWidget> oneshot$otherButtons = new ArrayList<>();
    @Unique ArrayList<AbstractWidget> oneshot$disconnectButtons = new ArrayList<>();
    @Unique
    void kelui$oneShotStyle() {
        // 86
        int size = (width - 24 - 10)/3;
        AbstractWidget helloControlify = addRenderableWidget(new Button(-20, -20, 20, 20, Component.translatable("menu.returnToGame")));
        helloControlify.visible = helloControlify.active = false;
        AbstractWidget options = addRenderableWidget(new OneShotPauseButton(12, 12, size, 24, Component.translatable("menu.options"), (s) -> this.minecraft.setScreen(KelUI.getOptionScreen(this))));
        options.active = !this.oneshot$otherMenuEnable;
        options.visible = !this.oneshot$disconnectMenuEnable && !this.oneshot$otherMenuEnable;
        oneshot$mainButtons.add(options);
        AbstractWidget other = addRenderableWidget(new OneShotPauseButton(width/2-size/2, 12, size, 24, Component.translatable("kelui.config.title.other"), (s) -> {
            changeMenuState();
        }));
        other.active = !this.oneshot$otherMenuEnable;
        other.visible = !this.oneshot$disconnectMenuEnable && !this.oneshot$otherMenuEnable;
        oneshot$mainButtons.add(other);
        Component component = this.minecraft.isLocalServer() ? Component.translatable("menu.returnToMenu") : CommonComponents.GUI_DISCONNECT;
        this.disconnectButton = net.minecraft.client.gui.components.Button.builder(component, (s) -> {}).build();
        AbstractWidget disconnect = addRenderableWidget(new OneShotPauseButton(width-12-size, 12, size, 24, component, (s) -> changeMenuDisconnectState()));
        disconnect.active = !this.oneshot$otherMenuEnable;
        disconnect.visible = !this.oneshot$disconnectMenuEnable && !this.oneshot$otherMenuEnable;
        oneshot$mainButtons.add(disconnect);
        kelui$oneShotOtherStyle();
        kelui$oneShotDisconnectStyle();
    }

    @Unique
    void kelui$oneShotOtherStyle() {
        int bHeight = font.lineHeight+4;
        int bHeight2 = (bHeight+3);
        int y = 43+ 24;
        int bWidth = font.width("...");
        Component[] texts = {
                ModMenuApi.createModsButtonText(),
                Component.translatable("gui.advancements"),
                Component.translatable("gui.stats")
        };
        for(Component text : texts){
            int i = font.width(text)+5;
            bWidth = Math.max(bWidth, i);
        }

        AbstractWidget titleBox = addRenderableWidget(new OneShotTitle(20, y-34, width-30, font.lineHeight*3, Component.translatable("kelui.config.title.other")));
        titleBox.visible = oneshot$otherMenuEnable && !oneshot$disconnectMenuEnable;
        oneshot$otherButtons.add(titleBox);

        AbstractWidget mods = addRenderableWidget(new OneShotTitleButton(30, y+10, bWidth, bHeight, texts[0], (s) -> this.minecraft.setScreen(ModMenuApi.createModsScreen(this))));
        mods.visible = mods.active = oneshot$otherMenuEnable && !oneshot$disconnectMenuEnable;
        oneshot$otherButtons.add(mods);
        y+=bHeight2;
        AbstractWidget advancements = addRenderableWidget(new OneShotTitleButton(30, y+10, bWidth, bHeight, texts[1], (s) -> this.minecraft.setScreen(new AdvancementsScreen(Objects.requireNonNull(this.minecraft.getConnection()).getAdvancements()))));
        advancements.visible = advancements.active = oneshot$otherMenuEnable && !oneshot$disconnectMenuEnable;
        oneshot$otherButtons.add(advancements);
        y+=bHeight2;
        AbstractWidget stats = addRenderableWidget(new OneShotTitleButton(30, y+10, bWidth, bHeight, texts[2], (s) -> {
            assert this.minecraft.player != null;
            this.minecraft.setScreen(new StatsScreen(this, this.minecraft.player.getStats()));
        }));
        stats.visible = stats.active = oneshot$otherMenuEnable && !oneshot$disconnectMenuEnable;
        oneshot$otherButtons.add(stats);
    }
    @Unique
    void kelui$oneShotDisconnectStyle() {
        AbstractWidget textBox = addRenderableWidget(new TextBox(width/2-100, 50, 200, 20, Component.translatable("kelui.oneshot.disconnect"), true));
        textBox.active = textBox.visible = oneshot$disconnectMenuEnable;
        oneshot$disconnectButtons.add(textBox);

        AbstractWidget yes = addRenderableWidget(new OneShotPauseButton(width/2-80, height/2-26, 75, 24, CommonComponents.GUI_YES, (s) -> onDisconnect()));
        yes.active = yes.visible = this.oneshot$disconnectMenuEnable;
        oneshot$disconnectButtons.add(yes);

        AbstractWidget no = addRenderableWidget(new OneShotPauseButton(width/2+5, height/2-26, 75, 24, CommonComponents.GUI_NO, (s) -> changeMenuDisconnectState()));
        no.active = no.visible = this.oneshot$disconnectMenuEnable;
        oneshot$disconnectButtons.add(no);
    }
    @Unique
    void changeMenuState(){
        this.oneshot$otherMenuEnable = !this.oneshot$otherMenuEnable;
        for(AbstractWidget widget : oneshot$mainButtons){
            widget.active = widget.visible = !this.oneshot$otherMenuEnable;
        }
        for(AbstractWidget widget : oneshot$otherButtons){
            if(!(widget instanceof OneShotTitle)) widget.active = oneshot$otherMenuEnable;
            widget.visible = this.oneshot$otherMenuEnable;
        }
    }
    @Unique
    void changeMenuDisconnectState(){
        this.oneshot$disconnectMenuEnable = !this.oneshot$disconnectMenuEnable;
        this.oneshot$otherMenuEnable = !this.oneshot$otherMenuEnable;
        for(AbstractWidget widget : oneshot$mainButtons){
            widget.active = widget.visible = !this.oneshot$disconnectMenuEnable;
        }
        for(AbstractWidget widget : oneshot$disconnectButtons){
            if(!(widget instanceof TextBox)) widget.active = oneshot$disconnectMenuEnable;
            widget.visible = this.oneshot$disconnectMenuEnable;
        }
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
            if(!oneshot$disconnectMenuEnable && !oneshot$otherMenuEnable) guiGraphics.blitSprite(new ResourceLocation("kelui", "pause_menu/oneshot_pause_panel"), 5, 5,  width-10, 38);
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
