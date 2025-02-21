package ru.kelcuprum.kelui.gui;

import net.minecraft.world.effect.MobEffectInstance;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.loading.AbstractLoadingOverlay;
import ru.kelcuprum.kelui.gui.loading.NonUseRefOverlay;
import ru.kelcuprum.kelui.gui.loading.LiteOverlay;
import ru.kelcuprum.kelui.gui.loading.SafeOverlay;

import java.util.HashMap;
import java.util.Objects;

public class Util {
    public static String getDurationAsString(int durationTicks) {
        if (durationTicks == MobEffectInstance.INFINITE_DURATION) {
            return "∞";
        }
        int seconds = durationTicks / 20;
        if (seconds >= 360000) { // 100 hours
            return "∞";
        } else if (seconds >= 3600) {
            return seconds / 3600 + "h";
        } else if (seconds >= 60) {
            return seconds / 60 + "m";
        } else {
            return String.valueOf(seconds);
        }
    }

    // -=-=-=- Хранилище оверлев -=-=-=-
    public static HashMap<String, AbstractLoadingOverlay> loadingOverlays = new HashMap<>();
    public static AbstractLoadingOverlay safeOverlay = new SafeOverlay();
    public static String firstID = "";

    public static void registerDefaultOverlays() {
        registerOverlay(new NonUseRefOverlay(), new LiteOverlay());
    }

    public static void registerOverlay(AbstractLoadingOverlay... overlays) {
        for (AbstractLoadingOverlay overlay : overlays) registerOverlay(overlay);
    }

    public static void registerOverlay(AbstractLoadingOverlay overlay) {
        if (loadingOverlays.containsKey(overlay.id))
            throw new RuntimeException(String.format("Overlay by id %s already registered", overlay.id));
        else {
            if (firstID.isEmpty()) firstID = overlay.id;
            KelUI.log(String.format("Overlay by id %s has been registered", overlay.id));
            loadingOverlays.put(overlay.id, overlay);
        }
    }

    public static String[] getOverlaysName() {
        String[] list = new String[loadingOverlays.size()];
        int i = 0;
        for (String id : loadingOverlays.keySet()) {
            list[i] = loadingOverlays.getOrDefault(id, safeOverlay).title.getString();
            i++;
        }
        return list;
    }

    public static int getPositionOnOverlaysID(String name) {
        int i = 0;
        for (String id : getOverlaysName()) {
            if (id.equals(name)) break;
            else i++;
        }
        return i;
    }

    public static AbstractLoadingOverlay getOverlayByName(String name) {
        AbstractLoadingOverlay style = safeOverlay;
        for (String id : loadingOverlays.keySet()) {
            AbstractLoadingOverlay styleById = getOverlayByID(id);
            if (Objects.equals(styleById.title.getString(), name)) {
                style = styleById;
            }
        }
        return style;
    }

    public static AbstractLoadingOverlay getOverlayByID(String id) {
        return loadingOverlays.getOrDefault(id, safeOverlay);
    }

    public static AbstractLoadingOverlay getSelectedOverlay() {
        String id = KelUI.config.getString("LOADING_OVERLAY.TYPE", loadingOverlays.isEmpty() || firstID.isEmpty() ? safeOverlay.id : firstID);
        return getOverlayByID(id);
    }
}