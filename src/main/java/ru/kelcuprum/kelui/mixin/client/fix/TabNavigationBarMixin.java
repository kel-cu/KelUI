package ru.kelcuprum.kelui.mixin.client.fix;

import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({TabNavigationBar.class})
public class TabNavigationBarMixin {

    @ModifyConstant(
            method = {"render"},
            constant = {@Constant(
                    intValue = -16777216
            )}
    )
    private int modifyTabBackgroundColor(int constant) {
        return -1728053248;
    }
}