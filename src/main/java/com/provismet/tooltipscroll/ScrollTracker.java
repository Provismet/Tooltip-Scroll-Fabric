package com.provismet.tooltipscroll;

import java.util.List;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;

public class ScrollTracker {
	public static int currentXOffset = 0;
	public static int currentYOffset = 0;
	public static TextRenderer renderer; 

	private static List<TooltipComponent> currentItem;
	private static final int scrollSize = 5;

	// Various scroll functions to interact with private fields
	public static void scrollUp() {
		currentYOffset -= scrollSize;
	}

	public static void scrollDown() {
		currentYOffset += scrollSize;
	}

	public static void scrollLeft() {
		currentXOffset -= scrollSize;
	}

	public static void scrollRight() {
		currentXOffset += scrollSize;
	}

	private static void resetScroll() {
		currentXOffset = 0;
		currentYOffset = 0;
	}

	// Reset the scroll amount and the current item
	public static void reset() {
		resetScroll();
		currentItem = null;
	}

	// isEqual compares the width of each line to determine if a new tooltip is being loaded
	// this method is not as precise as comparing the strings directly but with the new
	// TooltipComponent system I could not find a better way to do it
	public static boolean isEqual(List<TooltipComponent> item1, List<TooltipComponent> item2) {
		if (renderer == null || item1 == null || item2 == null || item1.size() != item2.size()) {
			return false;
		}

		for (int i = 0; i < item1.size(); i++) {
			if (item1.get(i).getWidth(renderer) != item2.get(i).getWidth(renderer)) {
				return false;
			}
		}
		return true;
	}

	// Set currentItem if it is not the same tooltip as the saved one
	public static void setItem(List<TooltipComponent> item) {
		if (isEqual(currentItem, item) == false) {
			resetScroll();
			currentItem = item;
		}
	}
}