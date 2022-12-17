package com.provismet.tooltipscroll.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

import com.provismet.tooltipscroll.Options;
import com.provismet.tooltipscroll.ScrollTracker;
import com.provismet.tooltipscroll.TooltipScrollClient;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class AlterPosition {
	// Reset the tracker whenever a GUI window closes.
	@Inject (method = "close()V", at = @At("HEAD"))
	public void resetTrackerOnScreenClose (CallbackInfo info) {
		ScrollTracker.reset();
	}

	// This inject allows the page-up and page-down buttons to perform scrolling.
	// It's just a QOL feature because some menus are scrollable and would be moved by the scrollwheel.
	@Inject (method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", at = @At("HEAD"))
	public void applyTracker (MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo info) {
		ScrollTracker.unlock();
		long mcHandle = MinecraftClient.getInstance().getWindow().getHandle();

		// An unbound key has a code of -1.
		int up = ((KeyBindAccessor)TooltipScrollClient.moveUp).getBoundKey().getCode();
		int down = ((KeyBindAccessor)TooltipScrollClient.moveDown).getBoundKey().getCode();
		int horizontal = ((KeyBindAccessor)TooltipScrollClient.horizontal).getBoundKey().getCode();
		int reset = ((KeyBindAccessor)TooltipScrollClient.reset).getBoundKey().getCode();

		if (Options.useWASD) {
			if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_W)) {
				ScrollTracker.scrollUp();
			}
			else if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_S)) {
				ScrollTracker.scrollDown();
			}

			if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_A)) {
				ScrollTracker.scrollLeft();
			}
			else if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_D)) {
				ScrollTracker.scrollRight();
			}
		}

		// Check for -1 codes first.
		// They don't cause Exceptions, but they do create a messy block of errors on the render thread when logging.
		if (up != -1 && InputUtil.isKeyPressed(mcHandle, up)) {
			if (horizontal != -1 && InputUtil.isKeyPressed(mcHandle, horizontal)) {
				ScrollTracker.scrollLeft();
			}
			else {
				ScrollTracker.scrollUp();
			}
		}
		else if (down != -1 && InputUtil.isKeyPressed(mcHandle, down)) {
			if (horizontal != -1 && InputUtil.isKeyPressed(mcHandle, horizontal)) {
				ScrollTracker.scrollRight();
			}
			else {
				ScrollTracker.scrollDown();
			}
		}
		else if (reset != -1 && InputUtil.isKeyPressed(mcHandle, reset)) {
			ScrollTracker.reset();
		}

		ScrollTracker.setItem(components);
	}

	// Using an invoke inject here because the tooltip coordinates get checked for out of bounds positions. I want the scroll offset to only apply after the bound check.
	// Targeting a method invoke because it was just conveniently placed after the bound check.

	// l is the variable that determines y-axis position of a tooltip.
	@ModifyVariable (method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", ordinal = 5, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE))
	private int modifyYAxis (int y) {
		return y + ScrollTracker.currentYOffset;
	}

	// k is the variable that determines x-axis position of a tooltip.
	@ModifyVariable (method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", ordinal = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE))
	private int modifyXAxis (int x) {
		return x + ScrollTracker.currentXOffset;
	}
}
