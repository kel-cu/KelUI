package ru.kelcuprum.kelui.gui.cicada;

import traben.entity_texture_features.features.ETFRenderContext;

public class ETFCompat {
    public static void preventRenderLayerIssue() {
        ETFRenderContext.preventRenderLayerTextureModify();
    }
}
