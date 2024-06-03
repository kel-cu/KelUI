package ru.kelcuprum.kelui.gui.screen.pause_screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.kelui.gui.components.OneShotPauseButton;

public class DisconnectScreen extends Screen {
    protected final Screen parent;
    protected final Runnable onDisconnect;
    public DisconnectScreen(Screen parent, Runnable onDisconnect) {
        super(Component.translatable("kelui.config.title.other"));
        this.parent = parent;
        this.onDisconnect = onDisconnect;
    }

    @Override
    protected void init() {
        addRenderableWidget(new TextBox(width / 2 - 100, 50, 200, 20, Component.translatable("kelui.oneshot.disconnect"), true));

        addRenderableWidget(new OneShotPauseButton(width / 2 - 80, height / 2 - 26, 75, 24, CommonComponents.GUI_YES, (s) -> onDisconnect.run()));

        addRenderableWidget(new OneShotPauseButton(width / 2 + 5, height / 2 - 26, 75, 24, CommonComponents.GUI_NO, (s) -> onClose()));
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
