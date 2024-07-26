package com.provismet.tooltipscroll.config;

import com.provismet.tooltipscroll.Options;
import com.provismet.tooltipscroll.ScrollTracker;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class TooltipConfig {
    public static Screen build (Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create();
        builder.setParentScreen(parent);
        builder.setTitle(Text.translatable("title.tooltipscroll.config"));
        
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.tooltipscroll.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.canscroll"), Options.canScroll)
            .setDefaultValue(true)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.canscroll"))
            .setSaveConsumer(newValue -> Options.canScroll = newValue)
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.usewasd"), Options.useWASD)
            .setDefaultValue(false)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.usewasd"))
            .setSaveConsumer(newValue -> Options.useWASD = newValue)
            .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.resetonunlock"), Options.resetOnUnlock)
            .setDefaultValue(true)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.resetonunlock"))
            .setSaveConsumer(newValue -> Options.resetOnUnlock = newValue)
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.uselshift"), Options.useLShift)
            .setDefaultValue(true)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.uselshift"))
            .setSaveConsumer(newValue -> Options.useLShift = newValue)
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.invertxscroll"), Options.invertXScroll)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("entrytooltip.tooltipscroll.invertxscroll"))
                .setSaveConsumer(newValue -> Options.invertXScroll = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.invertyscroll"), Options.invertYScroll)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("entrytooltip.tooltipscroll.invertyscroll"))
                .setSaveConsumer(newValue -> Options.invertYScroll = newValue)
                .build());

        general.addEntry(entryBuilder.startIntField(Text.translatable("entry.tooltipscroll.scrolldistance"), ScrollTracker.scrollSize)
            .setDefaultValue(10)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.scrolldistance"))
            .setSaveConsumer(newValue -> ScrollTracker.scrollSize = (int)MathHelper.absMax(1, newValue))
            .build());
        
        general.addEntry(entryBuilder.startIntField(Text.translatable("entry.tooltipscroll.scrolldistancekeys"), ScrollTracker.scrollSizeKeyboard)
            .setDefaultValue(5)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.scrolldistancekeys"))
            .setSaveConsumer(newValue -> ScrollTracker.scrollSizeKeyboard = (int)MathHelper.absMax(1, newValue))
            .build());

        general.addEntry(entryBuilder.startDoubleField(Text.translatable("entry.tooltipscroll.smoothness"), ScrollTracker.smoothnessModifier)
            .setDefaultValue(0.25)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.smoothness"))
            .setSaveConsumer(newValue -> {
                ScrollTracker.smoothnessModifier = MathHelper.absMax(0.05, newValue);
                if (ScrollTracker.smoothnessModifier > 1.0) ScrollTracker.smoothnessModifier = 1.0;
            })
            .build());

        builder.setSavingRunnable(() ->{
            Options.saveJSON();
        });

        return builder.build();
    }
}
