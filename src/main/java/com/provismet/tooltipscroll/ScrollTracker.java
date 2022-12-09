package com.provismet.tooltipscroll;

import java.util.List;

import net.minecraft.text.Text;

public class ScrollTracker {
    // render functions are called every frame, so the offset needs to be saved somewhere
    public static int currentXOffset = 0;
    public static int currentYOffset = 0;
    
    // save the currently selected item, the scroll offset will reset if the user hovers over a different item
    private static List<Text> currentItem;
    private static int currentOrderedSize;

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

    // Custom equality function because a standard .equals didn't work.
    private static boolean isEqual (List<Text> item1, List<Text> item2) {
        if (item1 == null || item2 == null || item1.size() != item2.size()) {
            return false;
        }
        
        for (int i = 0; i < item1.size(); ++i) {
            if (item1.get(i).getString().equals(item2.get(i).getString()) == false) {
                return false;
            }
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

    public static void setItem (List<Text> item) {
        if (isEqual(currentItem, item) == false) {
            resetScroll();
            currentItem = item;
        }
    }

    // Ordered items do not have any convenient means of being compared. This method will instead compare lengths.
    // Should not cause too many issues as this should only be a relevant comparison method when in contexts where renderOrderedTooltip is being called directly (menus).
    public static void setOrderedItemSize (int itemSize) {
        if (itemSize != currentOrderedSize) {
            currentOrderedSize = itemSize;
            resetScroll();
        }
    }

    public static void unlock () {
        if (Options.resetOnUnlock && isLocked()) {
            resetScroll();
        }
        unlockTime = System.currentTimeMillis();
    }
}