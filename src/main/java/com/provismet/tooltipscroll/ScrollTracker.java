package com.provismet.tooltipscroll;

import java.util.List;

import com.provismet.tooltipscroll.mixin.OrderedTextTooltipComponentAccessor;

import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;

public class ScrollTracker {
    // render functions are called every frame, so the offset needs to be saved somewhere
    public static int currentXOffset = 0;
    public static int currentYOffset = 0;
    
    // save the currently selected item, the scroll offset will reset if the user hovers over a different item
    private static List<TooltipComponent> currentItem;

    private static long unlockTime = System.currentTimeMillis();
    private static final long relockAt = 100;

    private static final int scrollSize = 5;

    // move the tooltip upwards
    public static void scrollUp () {
        if (!isLocked()) currentYOffset -= scrollSize;
    }

    // move the tooltip downwards
    public static void scrollDown () {
        if (!isLocked()) currentYOffset += scrollSize;
    }

    public static void scrollLeft () {
        if (!isLocked()) currentXOffset -= scrollSize;
    }

    public static void scrollRight () {
        if (!isLocked()) currentXOffset += scrollSize;
    }

    private static void resetScroll () {
        currentXOffset = 0;
        currentYOffset = 0;
    }

    private static boolean isEqual (List<TooltipComponent> item1, List<TooltipComponent> item2) {
        if (item1 == null || item2 == null || item1.size() != item2.size()) return false;
        
        for (int i = 0; i < item1.size(); ++i) {
            if (item1.get(i) instanceof OrderedTextTooltipComponent && !(item2.get(i) instanceof OrderedTextTooltipComponent)) return false;
            if (item2.get(i) instanceof OrderedTextTooltipComponent && !(item1.get(i) instanceof OrderedTextTooltipComponent)) return false;
            if (!(item1.get(i) instanceof OrderedTextTooltipComponent) && !(item2.get(i) instanceof OrderedTextTooltipComponent)) continue; // Can't compare non-text.
            
            OrderedTextTooltipComponentAccessor accessible1 = (OrderedTextTooltipComponentAccessor)((OrderedTextTooltipComponent)item1.get(i));
            OrderedTextTooltipComponentAccessor accessible2 = (OrderedTextTooltipComponentAccessor)((OrderedTextTooltipComponent)item2.get(i));

            String text1 = OrderedTextReader.read(accessible1.getText());
            String text2 = OrderedTextReader.read(accessible2.getText());
            if (!text1.equals(text2)) return false;
        }
        return true;
    }

    private static boolean isLocked () {
        long difference = System.currentTimeMillis() - unlockTime;
        return difference > relockAt;
    }

    // Reset the tracker to default values.
    public static void reset () {
        resetScroll();
        currentItem = null; // Using null instead of just clearing the list because not all of Minecraft's Text Lists can be cleared for some reason and that can cause an error.
    }

    public static void setItem (List<TooltipComponent> item) {
        if (!isEqual(currentItem, item)) {
            resetScroll();
            currentItem = item;
        }
    }

    public static void unlock () {
        if (Options.resetOnUnlock && isLocked()) {
            resetScroll();
        }
        unlockTime = System.currentTimeMillis();
    }
}