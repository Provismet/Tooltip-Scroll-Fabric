package com.provismet.tooltipscroll;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.impl.lib.gson.JsonReader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class TooltipScrollClient implements ClientModInitializer{
    public static KeyBinding moveUp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.tooltipscroll.moveUp",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_PAGE_UP,
        "category.tooltipscroll.keys"
    ));
    public static KeyBinding moveDown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.tooltipscroll.moveDown",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_PAGE_DOWN,
        "category.tooltipscroll.keys"
    ));
    public static KeyBinding reset = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.tooltipscroll.reset",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        "category.tooltipscroll.keys"
    ));
    public static KeyBinding horizontal = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.tooltipscroll.horizontal",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_LEFT_SHIFT,
        "category.tooltipscroll.keys"
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
        try {
            FileReader reader = new FileReader("config/tooltipscroll.json");
            JsonReader parser = new JsonReader(reader);

            parser.beginObject();
            parser.nextName();
            Options.canScroll = parser.nextBoolean();
            parser.nextName();
            Options.useWASD = parser.nextBoolean();
            parser.nextName();
            Options.resetOnUnlock = parser.nextBoolean();
            parser.close();
        } catch (FileNotFoundException e) {
            try {
                FileWriter writer = new FileWriter("config/tooltipscroll.json");
                String simpleJSON = "{\"canScroll\": true, \"useWASD\": false, \"resetOnUnlock\": false}";
                writer.write(simpleJSON);
                writer.close();
            } catch (Exception e2) {
                // Do nothing.
            }
        } catch (Exception e) {
            // Do nothing.
        }
    }
}
