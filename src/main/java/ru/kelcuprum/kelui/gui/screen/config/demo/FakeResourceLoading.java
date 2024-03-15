package ru.kelcuprum.kelui.gui.screen.config.demo;

import net.minecraft.Util;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/** {@link FakeResourceLoading} that automatically completes after some time */
public class FakeResourceLoading implements ReloadInstance {
    protected final long start = Util.getMillis();
    protected final long duration;

    public FakeResourceLoading(long durationMs) {
        this.duration = durationMs;
    }
    @Override

    public @NotNull CompletableFuture<Unit> done() {
        throw new UnsupportedOperationException();
    }
    @Override

    public float getActualProgress() {
        return Mth.clamp((float)(Util.getMillis() - this.start) / (float)this.duration, 0.0F, 1.0F);
    }

    @Override
    public boolean isDone() {
        return Util.getMillis() - this.start >= this.duration;
    }

    @Override
    public void checkExceptions() {
    }
}