package com.provismet.tooltipscroll.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.InputUtil;

import com.provismet.tooltipscroll.Options;
import com.provismet.tooltipscroll.ScrollTracker;
import com.provismet.tooltipscroll.TooltipScrollClient;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class TrackScrollWheel {
    // This will affect *every* use of the mouse wheel and alter the tracker accordingly.
    // Has no impact from a blackbox perspective though since the tooltip position will be reset when selecting an item.
    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"))
    private void trackWheel (long window, double horizontal, double vertical, CallbackInfo info) {
        if (!Options.canScroll) return;

        long mcHandle = MinecraftClient.getInstance().getWindow().getHandle();
        int horizontalMove = ((KeyBindAccessor)TooltipScrollClient.horizontal).getBoundKey().getCode();

        if (vertical > 0) {
            if ((horizontalMove != -1 && InputUtil.isKeyPressed(mcHandle, horizontalMove)) || (Options.useLShift && InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT))) {
				ScrollTracker.scrollRight();
			}
            else {
                ScrollTracker.scrollDown();
            }
        }
        else if (vertical < 0) {
            if ((horizontalMove != -1 && InputUtil.isKeyPressed(mcHandle, horizontalMove)) || (Options.useLShift && InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT))) {
				ScrollTracker.scrollLeft();
			}
            else {
                ScrollTracker.scrollUp();
            }
        }
    }
}
