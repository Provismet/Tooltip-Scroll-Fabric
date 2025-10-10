package com.provismet.tooltipscroll;

import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class TooltipScrollClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Tooltip Scroll");
    public static final String MODID = "tooltipscroll";

    public static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of(MODID, "keys"));

    public static KeyBinding moveUp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.tooltipscroll.moveUp",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_PAGE_UP,
        CATEGORY
    ));
    public static KeyBinding moveDown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.tooltipscroll.moveDown",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_PAGE_DOWN,
        CATEGORY
    ));
    public static KeyBinding reset = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.tooltipscroll.reset",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        CATEGORY
    ));
    public static KeyBinding horizontal = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.tooltipscroll.horizontal",
        InputUtil.Type.KEYSYM,
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
