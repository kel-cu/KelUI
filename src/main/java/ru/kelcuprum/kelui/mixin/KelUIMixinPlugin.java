package ru.kelcuprum.kelui.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import ru.kelcuprum.kelui.KelUI;

import java.util.List;
import java.util.Set;

public class KelUIMixinPlugin implements IMixinConfigPlugin {
    public static final Logger LOG = LogManager.getLogger("KelUI > Mixin");
    public static boolean isSodiumExtraEnable = FabricLoader.getInstance().isModLoaded("sodium-extra");
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(!mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.")){
            return false;
        }
        if(FabricLoader.getInstance().isModLoaded("betterpingdisplay") && mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.utils.PlayerListMixin")){
            LOG.error(String.format("Mixin %s for %s not loaded, Better Display Ping not compatibility", mixinClassName, targetClassName));
            return false;
        }
        if(isSodiumExtraEnable && mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.screen.sodium_extra.")){
            LOG.error(String.format("Mixin %s for %s loaded, %s", mixinClassName, targetClassName, "Sodium Extra installed"));
            return true;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
