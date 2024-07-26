package com.provismet.tooltipscroll;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.provismet.tooltipscroll.mixin.KeyBindAccessor;
import com.provismet.tooltipscroll.mixin.OrderedTextTooltipComponentAccessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;

public class ScrollTracker {
    // render functions are called every frame, so the offset needs to be saved somewhere
    private static double currentXOffset = 0;
    private static double currentYOffset = 0;

    private static double trueXOffset = 0;
    private static double trueYOffset = 0;
    
    // save the currently selected item, the scroll offset will reset if the user hovers over a different item
    private static List<TooltipComponent> currentItem;

    private static long unlockTime = System.currentTimeMillis();
    private static final long RELOCK_AT = 100;
    
    public static int scrollSize = 10;
    public static int scrollSizeKeyboard = 5;
    public static double smoothnessModifier = 0.25;

    public static void update () {
        currentXOffset += (trueXOffset - currentXOffset) * smoothnessModifier;
        currentYOffset += (trueYOffset - currentYOffset) * smoothnessModifier;

        long mcHandle = MinecraftClient.getInstance().getWindow().getHandle();

		// An unbound key has a code of -1.
		int up = ((KeyBindAccessor)TooltipScrollClient.moveUp).getBoundKey().getCode();
		int down = ((KeyBindAccessor)TooltipScrollClient.moveDown).getBoundKey().getCode();
		int horizontal = ((KeyBindAccessor)TooltipScrollClient.horizontal).getBoundKey().getCode();
		int reset = ((KeyBindAccessor)TooltipScrollClient.reset).getBoundKey().getCode();

		if (Options.useWASD) {
			if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_W)) {
				ScrollTracker.scrollUp(scrollSizeKeyboard);
			}
			else if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_S)) {
				ScrollTracker.scrollDown(scrollSizeKeyboard);
			}

			if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_A)) {
				ScrollTracker.scrollLeft(scrollSizeKeyboard);
			}
			else if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_D)) {
				ScrollTracker.scrollRight(scrollSizeKeyboard);
			}
		}

		// Check for -1 codes first.
		// They don't cause Exceptions, but they do create a messy block of errors on the render thread when logging.
		if (up != -1 && InputUtil.isKeyPressed(mcHandle, up)) {
			if ((horizontal != -1 && InputUtil.isKeyPressed(mcHandle, horizontal)) || (Options.useLShift && InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT))) {
				ScrollTracker.scrollLeft(scrollSizeKeyboard);
			}
			else {
				ScrollTracker.scrollUp(scrollSizeKeyboard);
			}
		}
		else if (down != -1 && InputUtil.isKeyPressed(mcHandle, down)) {
			if ((horizontal != -1 && InputUtil.isKeyPressed(mcHandle, horizontal)) || (Options.useLShift && InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT))) {
				ScrollTracker.scrollRight(scrollSizeKeyboard);
			}
			else {
				ScrollTracker.scrollDown(scrollSizeKeyboard);
			}
		}
		else if (reset != -1 && InputUtil.isKeyPressed(mcHandle, reset)) {
			ScrollTracker.reset();
		}
    }

    public static int getXOffset () {
        return MathHelper.floor(currentXOffset);
    }

    public static int getYOffset () {
        return MathHelper.floor(currentYOffset);
    }

    public static void scrollUp () {
        scrollUp(scrollSize);
    }

    public static void scrollUp (int amount) {
        if (!isLocked()) trueYOffset -= amount;
    }

    public static void scrollDown () {
        scrollDown(scrollSize);
    }

    public static void scrollDown (int amount) {
        if (!isLocked()) trueYOffset += amount;
    }

    public static void scrollLeft () {
        scrollLeft(scrollSize);
    }

    public static void scrollLeft (int amount) {
        if (!isLocked()) trueXOffset -= amount;
    } 

    public static void scrollRight () {
        scrollRight(scrollSize);
    }

    public static void scrollRight (int amount) {
        if (!isLocked()) trueXOffset += amount;
    }

    private static void resetScroll () {
        currentXOffset = 0;
        currentYOffset = 0;
        trueXOffset = 0;
        trueYOffset = 0;
    }

    private static boolean isEqual (List<TooltipComponent> item1, List<TooltipComponent> item2) {
        if (item1 == null || item2 == null || item1.size() != item2.size()) return false;
        
        for (int i = 0; i < item1.size(); ++i) {
            if (item1.get(i) instanceof OrderedTextTooltipComponent && !(item2.get(i) instanceof OrderedTextTooltipComponent)) return false;
            if (item2.get(i) instanceof OrderedTextTooltipComponent && !(item1.get(i) instanceof OrderedTextTooltipComponent)) return false;
            if (!(item1.get(i) instanceof OrderedTextTooltipComponent) && !(item2.get(i) instanceof OrderedTextTooltipComponent)) continue; // Can't compare non-text.
            
            OrderedTextTooltipComponentAccessor accessible1 = (OrderedTextTooltipComponentAccessor) item1.get(i);
            OrderedTextTooltipComponentAccessor accessible2 = (OrderedTextTooltipComponentAccessor) item2.get(i);

            String text1 = OrderedTextReader.read(accessible1.getText());
            String text2 = OrderedTextReader.read(accessible2.getText());
            if (!text1.equals(text2)) return false;
        }
        return true;
    }

    private static boolean isLocked () {
        long difference = System.currentTimeMillis() - unlockTime;
        return difference > RELOCK_AT;
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