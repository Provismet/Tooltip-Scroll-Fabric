package com.provismet.tooltipscroll;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.stream.JsonReader;

import net.minecraft.util.math.MathHelper;

public abstract class Options {
    public static boolean canScroll = true;
    public static boolean useWASD = false;
    public static boolean resetOnUnlock = false;
    public static boolean useLShift = true;

    public static final String CAN_SCROLL = "canScroll";
    public static final String USE_WASD = "useWASD";
    public static final String RESET_ON_UNLOCK = "resetOnUnlock";
    public static final String USE_LEFT_SHIFT = "useLShift";
    public static final String SCROLL_SPEED = "scrollSpeed";
    public static final String SCROLL_SPEED_KEYBOARD = "keyboardScrollSpeed";
    public static final String SMOOTHNESS = "scrollSmoothness";

    public static void saveJSON () {
        try {
            FileWriter writer = new FileWriter("config/tooltipscroll.json");
            String simpleJSON = String.format("{\n\t\"%s\": %b,\n\t\"%s\": %b,\n\t\"%s\": %b,\n\t\"%s\": %b,\n\t\"%s\": %d,\n\t\"%s\": %d,\n\t\"%s\": %f\n}",
                CAN_SCROLL, canScroll,
                USE_WASD, useWASD,
                RESET_ON_UNLOCK, resetOnUnlock,
                USE_LEFT_SHIFT, useLShift,
                SCROLL_SPEED, ScrollTracker.scrollSize,
                SCROLL_SPEED_KEYBOARD, ScrollTracker.scrollSizeKeyboard,
                SMOOTHNESS, ScrollTracker.smoothnessModifier);
            writer.write(simpleJSON);
            writer.close();
        } catch (Exception e) {
            TooltipScrollClient.LOGGER.error("Encountered error whilst trying to save config JSON.");
        }
    }

    public static void readJSON () {
        try {
            FileReader reader = new FileReader("config/tooltipscroll.json");
            JsonReader parser = new JsonReader(reader);

            parser.beginObject();
            while (parser.hasNext()) {
                final String label = parser.nextName();
                switch (label) {
                    case CAN_SCROLL:
                        Options.canScroll = parser.nextBoolean();
                        break;
                    
                    case USE_WASD:
                        Options.useWASD = parser.nextBoolean();
                        break;
                    
                    case RESET_ON_UNLOCK:
                        Options.resetOnUnlock = parser.nextBoolean();
                        break;

                    case USE_LEFT_SHIFT:
                        Options.useLShift = parser.nextBoolean();
                        break;
                    
                    case SCROLL_SPEED:
                        ScrollTracker.scrollSize = (int)MathHelper.absMax(1, parser.nextInt());
                        break;
                    
                    case SCROLL_SPEED_KEYBOARD:
                        ScrollTracker.scrollSizeKeyboard = (int)MathHelper.absMax(1, parser.nextInt());
                        break;

                    case SMOOTHNESS:
                        ScrollTracker.smoothnessModifier = MathHelper.absMax(0.05, parser.nextDouble());
                        if (ScrollTracker.smoothnessModifier > 1.0) ScrollTracker.smoothnessModifier = 1.0;
                        break;
                
                    default:
                        break;
                }
            }
            parser.close();
        } catch (FileNotFoundException e) {
            try {
                saveJSON();
            } catch (Exception e2) {
                // Do nothing.
            }
        } catch (Exception e) {
            // Do nothing.
        }
    }
}
