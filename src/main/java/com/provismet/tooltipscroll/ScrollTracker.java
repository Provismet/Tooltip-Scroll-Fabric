package com.provismet.tooltipscroll;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public class ScrollTracker {
    public static final MinecraftClient client = MinecraftClient.getInstance();

    private static int cx, cy;
    private static int w, h;
    public static int getX() {
        cx = Math.min(Math.max(cx, -6), client.currentScreen.width + w - 12);
        return cx;
    }
    public static int getY() {
        cy = Math.min(Math.max(cy, 16), client.currentScreen.height - h + 12);
        return cy;
    }
    public static void setDX(int dx) {
        cx += dx;
    }
    public static void setDY(int dy) {
        cy += dy;
    }
    public static void setBounds(int wi, int hi) {
        w = wi; h = hi;
    }
    public static void reset() {
        cx = cy = 0;
        w = h = 0;
        cur = null;
    }
    private static ItemStack cur;
    public static void setItem(ItemStack stack, int x, int y) {
        if (cur != null && cur.equals(stack)) return;
        w = h = 0;
        cx = x; cy = y; cur = stack;
    }
}