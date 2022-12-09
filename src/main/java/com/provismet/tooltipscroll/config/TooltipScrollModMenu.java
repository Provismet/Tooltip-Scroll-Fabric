package com.provismet.tooltipscroll.config;

import com.provismet.tooltipscroll.TooltipScrollClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class TooltipScrollModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory () {
        if (TooltipScrollClient.hasCloth()) {
            return parent -> {
                return TooltipConfig.build(parent);
            };
        }
        else {
            return parent -> null;
        }
    }
}
