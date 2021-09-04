package com.provismet.tooltipscroll.mixin;

import com.provismet.tooltipscroll.ScrollTracker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.InputUtil;

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
	private void trackWheel(long window, double horizontal, double vertical, CallbackInfo info) {
		MinecraftClient inst = MinecraftClient.getInstance();
		long mcHandle = inst.getWindow().getHandle();

		// We need the text renderer to get the width, so might as well check for that here
		if (ScrollTracker.renderer == null) {
			ScrollTracker.renderer = inst.textRenderer;
		}
	
		if (vertical > 0) {
			if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
				ScrollTracker.scrollRight();
			}
			else {
				ScrollTracker.scrollUp();
			}
		}
		else if (vertical < 0) {
			if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
				ScrollTracker.scrollLeft();
			}
			else {
				ScrollTracker.scrollDown();
			}
		}
	}
}
