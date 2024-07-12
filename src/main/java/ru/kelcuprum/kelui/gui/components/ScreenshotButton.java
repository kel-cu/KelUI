package ru.kelcuprum.kelui.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.GuiUtils;

public class ScreenshotButton extends AbstractButton {
    public ScreenshotButton(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        GuiUtils.getSelected().renderBackground$widget(guiGraphics, getX(), getY(), getWidth(), getHeight(), isActive(), isHoveredOrFocused());
    }

    @Override
    public void onPress() {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
