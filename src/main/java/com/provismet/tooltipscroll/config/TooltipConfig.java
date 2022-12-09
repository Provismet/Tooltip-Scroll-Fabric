package com.provismet.tooltipscroll.config;

import java.io.FileWriter;

import com.provismet.tooltipscroll.Options;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class TooltipConfig {
    public static Screen build (Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create();
        builder.setParentScreen(parent);
        builder.setTitle(Text.translatable("title.tooltipscroll.config"));
        
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.tooltipscroll.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.canScroll"), Options.canScroll)
            .setDefaultValue(true)
            .setSaveConsumer(newValue -> Options.canScroll = newValue)
            .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.usewasd"), Options.useWASD)
            .setDefaultValue(false)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.usewasd"))
            .setSaveConsumer(newValue -> Options.useWASD = newValue)
            .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("entry.tooltipscroll.resetonunlock"), Options.resetOnUnlock)
            .setDefaultValue(false)
            .setTooltip(Text.translatable("entrytooltip.tooltipscroll.resetonunlock"))
            .setSaveConsumer(newValue -> Options.resetOnUnlock = newValue)
            .build());

        builder.setSavingRunnable(() ->{
            try {
                FileWriter writer = new FileWriter("config/tooltipscroll.json");
                String simpleJSON = String.format("{\"canScroll\": %b, \"useWASD\": %b, \"resetOnUnlock\": %b}", Options.canScroll, Options.useWASD, Options.resetOnUnlock);
                writer.write(simpleJSON);
                writer.close();
            } catch (Exception e) {
                // Do nothing.
            }
        });

        return builder.build();
    }
}
