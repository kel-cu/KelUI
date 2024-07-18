package ru.kelcuprum.kelui.gui;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.Screen;
import ru.kelcuprum.kelui.gui.screen.config.MenuConfigScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CatalogueConfigFactory {
    public static Map<String, BiFunction<Screen, ModContainer, Screen>> createConfigProvider()
    {
        Map<String, BiFunction<Screen, ModContainer, Screen>> providers = new HashMap<>();
        providers.put("kelui", ((screen, modContainer) -> new MenuConfigScreen().build(screen)));
        return providers;
    }
}
