package com.provismet.tooltipscroll.config;

import com.provismet.tooltipscroll.Options;
import com.provismet.tooltipscroll.ScrollTracker;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TooltipConfig {
    public static Screen build (Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create();
        builder.setParentScreen(parent);
        builder.setTitle(Component.translatable("title.tooltipscroll.config"));
        
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("category.tooltipscroll.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("entry.tooltipscroll.canscroll"), Options.canScroll)
            .setDefaultValue(true)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.canscroll"))
            .setSaveConsumer(newValue -> Options.canScroll = newValue)
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("entry.tooltipscroll.usewasd"), Options.useWASD)
            .setDefaultValue(false)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.usewasd"))
            .setSaveConsumer(newValue -> Options.useWASD = newValue)
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("entry.tooltipscroll.startontop"), Options.startOnTop)
            .setDefaultValue(false)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.startontop"))
            .setSaveConsumer(newValue -> Options.startOnTop = newValue)
            .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("entry.tooltipscroll.resetonunlock"), Options.resetOnUnlock)
            .setDefaultValue(true)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.resetonunlock"))
            .setSaveConsumer(newValue -> Options.resetOnUnlock = newValue)
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("entry.tooltipscroll.uselshift"), Options.useLShift)
            .setDefaultValue(true)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.uselshift"))
            .setSaveConsumer(newValue -> Options.useLShift = newValue)
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("entry.tooltipscroll.invertxscroll"), Options.invertXScroll)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("entrytooltip.tooltipscroll.invertxscroll"))
                .setSaveConsumer(newValue -> Options.invertXScroll = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("entry.tooltipscroll.invertyscroll"), Options.invertYScroll)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("entrytooltip.tooltipscroll.invertyscroll"))
                .setSaveConsumer(newValue -> Options.invertYScroll = newValue)
                .build());

        general.addEntry(entryBuilder.startIntField(Component.translatable("entry.tooltipscroll.scrollspeed"), ScrollTracker.scrollSize)
            .setDefaultValue(10)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.scrollspeed"))
            .setSaveConsumer(newValue -> ScrollTracker.scrollSize = (int)Mth.absMax(1, newValue))
            .build());
        
        general.addEntry(entryBuilder.startIntField(Component.translatable("entry.tooltipscroll.scrollspeedkeys"), ScrollTracker.scrollSizeKeyboard)
            .setDefaultValue(5)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.scrollspeedkeys"))
            .setSaveConsumer(newValue -> ScrollTracker.scrollSizeKeyboard = (int)Mth.absMax(1, newValue))
            .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("entry.tooltipscroll.smoothness"), ScrollTracker.smoothnessModifier)
            .setDefaultValue(0.25)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.smoothness"))
            .setSaveConsumer(newValue -> {
                ScrollTracker.smoothnessModifier = Mth.absMax(0.05, newValue);
                if (ScrollTracker.smoothnessModifier > 1.0) ScrollTracker.smoothnessModifier = 1.0;
            })
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("entry.tooltipscroll.matrix_compatibility"), Options.matrixMode)
            .setDefaultValue(false)
            .setTooltip(Component.translatable("entrytooltip.tooltipscroll.matrix_compatibility"))
            .setSaveConsumer(val -> Options.matrixMode = val)
            .build());

        builder.setSavingRunnable(Options::saveJSON);
        return builder.build();
    }
}
