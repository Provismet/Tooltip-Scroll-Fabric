package com.provismet.tooltipscroll;

import com.provismet.lilylib.util.json.JsonBuilder;
import com.provismet.lilylib.util.json.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import net.minecraft.util.Mth;

public abstract class Options {
    public static boolean canScroll = true;
    public static boolean useWASD = false;
    public static boolean startOnTop = false;
    public static boolean resetOnUnlock = true;
    public static boolean useLShift = true;
    public static boolean invertXScroll = false;
    public static boolean invertYScroll = false;
    public static boolean matrixMode = false;

    public static final String CAN_SCROLL = "canScroll";
    public static final String USE_WASD = "useWASD";
    public static final String START_ON_TOP = "startOnTop";
    public static final String RESET_ON_UNLOCK = "resetOnUnlock";
    public static final String USE_LEFT_SHIFT = "useLShift";
    public static final String INVERT_X_SCROLL = "invertXScroll";
    public static final String INVERT_Y_SCROLL = "invertYScroll";
    public static final String SCROLL_SPEED = "scrollSpeed";
    public static final String SCROLL_SPEED_KEYBOARD = "keyboardScrollSpeed";
    public static final String SMOOTHNESS = "scrollSmoothness";
    public static final String MATRIX_COMPAT = "matrixCompatibilityMode";

    public static void saveJSON () {
        String json = new JsonBuilder()
            .append(CAN_SCROLL, canScroll)
            .append(USE_WASD, useWASD)
            .append(START_ON_TOP, startOnTop)
            .append(RESET_ON_UNLOCK, resetOnUnlock)
            .append(USE_LEFT_SHIFT, useLShift)
            .append(INVERT_X_SCROLL, invertXScroll)
            .append(INVERT_Y_SCROLL, invertYScroll)
            .append(SCROLL_SPEED, ScrollTracker.scrollSize)
            .append(SCROLL_SPEED_KEYBOARD, ScrollTracker.scrollSizeKeyboard)
            .append(SMOOTHNESS, ScrollTracker.smoothnessModifier)
            .append(MATRIX_COMPAT, matrixMode)
            .toString();

        try (FileWriter writer = new FileWriter("config/tooltipscroll.json")) {
            writer.write(json);
        }
        catch (Exception e) {
            TooltipScrollClient.LOGGER.error("Encountered error whilst trying to save config JSON.", e);
        }
    }

    public static void readJSON () {
        try {
            JsonReader reader = JsonReader.file(new File("config/tooltipscroll.json"));
            if (reader != null) {
                reader.getBoolean(CAN_SCROLL).ifPresent(val -> canScroll = val);
                reader.getBoolean(USE_WASD).ifPresent(val -> useWASD = val);
                reader.getBoolean(START_ON_TOP).ifPresent(val -> startOnTop = val);
                reader.getBoolean(RESET_ON_UNLOCK).ifPresent(val -> resetOnUnlock = val);
                reader.getBoolean(USE_LEFT_SHIFT).ifPresent(val -> useLShift = val);
                reader.getBoolean(INVERT_X_SCROLL).ifPresent(val -> invertXScroll = val);
                reader.getBoolean(INVERT_Y_SCROLL).ifPresent(val -> invertYScroll = val);
                reader.getInteger(SCROLL_SPEED).ifPresent(val -> ScrollTracker.scrollSize = Math.max(1, val));
                reader.getInteger(SCROLL_SPEED_KEYBOARD).ifPresent(val -> ScrollTracker.scrollSizeKeyboard = Math.max(1, val));
                reader.getDouble(SMOOTHNESS).ifPresent(val -> ScrollTracker.smoothnessModifier = Mth.clamp(val, 0.05, 1.0));
                reader.getBoolean(MATRIX_COMPAT).ifPresent(val -> matrixMode = val);
            }
        }
        catch (FileNotFoundException e) {
            TooltipScrollClient.LOGGER.info("Failed to find TooltipScroll config, constructing default.");
        }
        saveJSON();
    }
}
