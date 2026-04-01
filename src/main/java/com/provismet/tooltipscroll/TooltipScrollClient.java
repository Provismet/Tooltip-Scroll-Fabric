package com.provismet.tooltipscroll;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class TooltipScrollClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Tooltip Scroll");
    public static final String MODID = "tooltipscroll";

    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MODID, "keys"));

    public static KeyMapping moveUp = KeyBindingHelper.registerKeyBinding(new KeyMapping(
        "key.tooltipscroll.moveUp",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_PAGE_UP,
        CATEGORY
    ));
    public static KeyMapping moveDown = KeyBindingHelper.registerKeyBinding(new KeyMapping(
        "key.tooltipscroll.moveDown",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_PAGE_DOWN,
        CATEGORY
    ));
    public static KeyMapping reset = KeyBindingHelper.registerKeyBinding(new KeyMapping(
        "key.tooltipscroll.reset",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        CATEGORY
    ));
    public static KeyMapping horizontal = KeyBindingHelper.registerKeyBinding(new KeyMapping(
        "key.tooltipscroll.horizontal",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        CATEGORY
    ));

    public static boolean hasCloth () {
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onInitializeClient () {
        Options.readJSON();
    }
}
