package com.provismet.tooltipscroll;

import java.io.FileWriter;

public abstract class Options {
    public static boolean canScroll = true;
    public static boolean useWASD = false;
    public static boolean resetOnUnlock = false;
    public static boolean useLShift = true;

    public static void saveJSON () {
        try {
            FileWriter writer = new FileWriter("config/tooltipscroll.json");
            String simpleJSON = String.format("{\n\t\"%s\": %b,\n\t\"%s\": %b, \n\t\"%s\": %b, \n\t\"%s\": %b\n}",
                TooltipScrollClient.CAN_SCROLL, canScroll, TooltipScrollClient.USE_WASD, useWASD, TooltipScrollClient.RESET_ON_UNLOCK, resetOnUnlock, TooltipScrollClient.USE_LEFT_SHIFT, useLShift);
            writer.write(simpleJSON);
            writer.close();
        } catch (Exception e) {
            TooltipScrollClient.LOGGER.error("Encountered error whilst trying to save config JSON.");
        }
    }
}
