package ru.kelcuprum.kelui.gui.screen.pause_screen;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.components.OneShotTitle;
import ru.kelcuprum.kelui.gui.components.OneShotTitleButton;

import java.util.Objects;

public class OtherScreen extends Screen {
    protected final Screen parent;
    public OtherScreen(Screen parent) {
        super(Component.translatable("kelui.config.title.other"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int bHeight = font.lineHeight+4;
        int bHeight2 = (bHeight+3);
        int y = 43+ 24;
        int bWidth = font.width("...");
        Component[] texts = {
                ModMenuApi.createModsButtonText(),
                Component.translatable("gui.advancements"),
                Component.translatable("gui.stats"),
                Component.translatable("options.language"),
                Component.translatable("menu.shareToLan"),
                Localization.toText(KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby"))
        };
        for(Component text : texts){
            int i = font.width(text)+5;
            bWidth = Math.max(bWidth, i);
        }

        addRenderableWidget(new OneShotTitle(20, y - 34, width - 30, font.lineHeight * 3, getTitle()));

        addRenderableWidget(new OneShotTitleButton(30, y + 10, bWidth, bHeight, texts[0], (s) -> this.minecraft.setScreen(ModMenuApi.createModsScreen(this))));
        y+=bHeight2;

        addRenderableWidget(new OneShotTitleButton(30, y + 10, bWidth, bHeight, texts[1], (s) -> this.minecraft.setScreen(new AdvancementsScreen(Objects.requireNonNull(this.minecraft.getConnection()).getAdvancements()))));
        y+=bHeight2;

        addRenderableWidget(new OneShotTitleButton(30, y + 10, bWidth, bHeight, texts[2], (s) -> {
            assert this.minecraft.player != null;
            this.minecraft.setScreen(new StatsScreen(this, this.minecraft.player.getStats()));
        }));
        y+=bHeight2;

        addRenderableWidget(new OneShotTitleButton(30, y + 10, bWidth, bHeight, texts[3], (s) -> {
            assert this.minecraft.player != null;
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }));
        y+=bHeight2;

        if(this.minecraft.hasSingleplayerServer() && !Objects.requireNonNull(this.minecraft.getSingleplayerServer()).isPublished()){
            addRenderableWidget(new OneShotTitleButton(30, y + 10, bWidth, bHeight, texts[4], (s) -> {
                assert this.minecraft.player != null;
                this.minecraft.setScreen(new ShareToLanScreen(this));
            }));
            y+=bHeight2;
        }

        if(KelUI.config.getBoolean("PAUSE_MENU.ENABLE_SHORT_COMMAND", false)) {
            addRenderableWidget(new OneShotTitleButton(30, y + 10, bWidth, bHeight, texts[5], (s) -> KelUI.executeCommand(this.minecraft.player, KelUI.config.getString("PAUSE_MENU.SHORT_COMMAND.COMMAND", "/lobby"))));
        }
    }

    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.setScreen(parent);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fill(0, 0, width, height, 0x7f000000);
    }
}
