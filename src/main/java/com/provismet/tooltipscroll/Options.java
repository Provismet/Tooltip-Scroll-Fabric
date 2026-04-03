package com.provismet.tooltipscroll;

import com.provismet.lilylib.util.json.JsonConfig;
import com.provismet.lilylib.util.json.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Mth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public abstract class Options {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("tooltipscroll.json");

    public static boolean canScroll = true;
    public static boolean useWASD = false;
    public static boolean startOnTop = false;
    public static boolean resetOnUnlock = true;
    public static boolean useLShift = true;
    public static boolean invertXScroll = false;
    public static boolean invertYScroll = false;
    public static boolean matrixMode = false;

    private static final JsonConfig SERIALISER = new JsonConfig()
        .addBoolean("canScroll", () -> canScroll, val -> canScroll = val)
        .addBoolean("useWASD", () -> useWASD, val -> useWASD = val)
        .addBoolean("startOnTop", () -> startOnTop, val -> startOnTop = val)
        .addBoolean("resetOnUnlock", () -> resetOnUnlock, val -> resetOnUnlock = val)
        .addBoolean("useLShift", () -> useLShift, val -> useLShift = val)
        .addBoolean("invertXScroll", () -> invertXScroll, val -> invertXScroll = val)
        .addBoolean("invertYScroll", () -> invertYScroll, val -> invertYScroll = val)
        .addInteger("scrollSpeed", () -> ScrollTracker.scrollSize, val -> ScrollTracker.scrollSize = Math.max(1, val))
        .addInteger("keyboardScrollSpeed", () -> ScrollTracker.scrollSizeKeyboard, val -> ScrollTracker.scrollSizeKeyboard = Math.max(1, val))
        .addDouble("scrollSmoothness", () -> ScrollTracker.smoothnessModifier, val -> ScrollTracker.smoothnessModifier = Mth.clamp(val, 0.05, 1.0))
        .addBoolean("matrixCompatibilityMode", () -> matrixMode, val -> matrixMode = val);

    public static void saveJSON () {
        try {
            SERIALISER.saveToFile(FILE);
        }
        catch (IOException e) {
            TooltipScrollClient.LOGGER.error("Encountered error whilst trying to save config JSON.", e);
        }
    }

    public static void readJSON () {
        try {
            JsonReader reader = JsonReader.file(FILE);
            if (reader != null) {
                SERIALISER.loadFromJson(reader);
            }
        }
        catch (FileNotFoundException e) {
            TooltipScrollClient.LOGGER.info("Failed to find TooltipScroll config, constructing default.");
        }
        saveJSON();
    }
}
