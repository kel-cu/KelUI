package ru.kelcuprum.kelui.gui.screen.config.demo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;

public class PreviewLoadingOverlay extends LoadingOverlay {

    private final Runnable onRemoved;

    public PreviewLoadingOverlay(long durationMs, Runnable onRemoved) {
        super(
                Minecraft.getInstance(), new FakeResourceLoading(durationMs),
                optional -> {}, true
        );
        this.onRemoved = onRemoved;
    }

    public void onRemoved() {
        if (onRemoved != null) onRemoved.run();
    }
}
