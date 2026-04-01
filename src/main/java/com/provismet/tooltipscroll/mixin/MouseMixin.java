package com.provismet.tooltipscroll.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.provismet.tooltipscroll.Options;
import com.provismet.tooltipscroll.ScrollTracker;
import com.provismet.tooltipscroll.TooltipScrollClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
    // This will affect *every* use of the mouse wheel and alter the tracker accordingly.
    // Has no impact from a blackbox perspective though since the tooltip position will be reset when selecting an item.
    @Inject(method = "onScroll", at = @At("HEAD"))
    private void trackWheel (long window, double horizontal, double vertical, CallbackInfo info) {
        if (!Options.canScroll) return;

        Window mcWindow = Minecraft.getInstance().getWindow();
        int horizontalMove = ((KeyBindAccessor)TooltipScrollClient.horizontal).getKey().getValue();

        if ((horizontalMove != -1 && InputConstants.isKeyDown(mcWindow, horizontalMove)) || (Options.useLShift && InputConstants.isKeyDown(mcWindow, GLFW.GLFW_KEY_LEFT_SHIFT))) {
            scrollX(vertical);
        }
        else {
            scrollY(vertical);
        }

        // Implementation for side scroll-wheels
        if (horizontal > 0) ScrollTracker.scrollLeft();
        else if (horizontal < 0) ScrollTracker.scrollRight();
    }

    @Unique
    private void scrollX(double vertical) {
        if (Options.invertXScroll) {
            if (vertical > 0) ScrollTracker.scrollRight();
            else if (vertical < 0) ScrollTracker.scrollLeft();
        }
        else {
            if (vertical > 0) ScrollTracker.scrollLeft();
            else if (vertical < 0) ScrollTracker.scrollRight();
        }
    }

    @Unique
    private void scrollY(double vertical) {
        if (Options.invertYScroll) {
            if (vertical > 0) ScrollTracker.scrollDown();
            else if (vertical < 0) ScrollTracker.scrollUp();
        }
        else {
            if (vertical > 0) ScrollTracker.scrollUp();
            else if (vertical < 0) ScrollTracker.scrollDown();
        }
    }
}
