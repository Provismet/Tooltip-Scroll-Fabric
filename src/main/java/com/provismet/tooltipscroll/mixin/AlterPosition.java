package com.provismet.tooltipscroll.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

import com.provismet.tooltipscroll.ScrollTracker;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class AlterPosition {
	// Reset the tracker whenever a GUI window closes.
	@Inject (method = "onClose()V", at = @At("HEAD"))
	public void resetTrackerOnScreenClose (CallbackInfo info) {
		ScrollTracker.reset();
	}

	// There are two different functions for renderTooltip, one with a list and one without, so just catch both and format the data as List regardless.
	@Inject (method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", at = @At("HEAD"))
	public void updateTracker (MatrixStack matrices, List<Text> lines, int x, int y, CallbackInfo info) {
		ScrollTracker.setItem(lines);
	}

	@Inject (method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;II)V", at = @At("HEAD"))
	public void updateTracker (MatrixStack matrices, Text text, int x, int y, CallbackInfo info) {
		List<Text> asList = Arrays.asList(text);
		ScrollTracker.setItem(asList);
	}

	@Inject (method = "renderOrderedTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
	public void applyTracker (MatrixStack matrices, List<? extends OrderedText> lines, int x, int y, CallbackInfo info) {
		long mcHandle = MinecraftClient.getInstance().getWindow().getHandle();
		if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_PAGE_UP) == true) { // Some use cases (such as creative inventory) might require button press instead of scroll wheel, so keep this here.
			if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
				ScrollTracker.scrollLeft();
			}
			else {
				ScrollTracker.scrollUp();
			}
		}
		else if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_PAGE_DOWN) == true) {
			if (InputUtil.isKeyPressed(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
				ScrollTracker.scrollRight();
			}
			else {
				ScrollTracker.scrollDown();
			}
		}
	}

	// Using an invoke inject here because the tooltip coordinates get checked for out of bounds positions. I want the scroll offset to only apply after the bound check.
	// Targetting a method invoke because it was just conveniently placed after the bound check.

	// l is the variable that determines y-axis position of a tooltip.
	@ModifyVariable (method = "renderOrderedTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", ordinal = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE))
	private int modifyYAxis (int y) {
		return y + ScrollTracker.currentYOffset;
	}

	// k is the variable that determines x-axis position of a tooltip.
	@ModifyVariable (method = "renderOrderedTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", ordinal = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE))
	private int modifyXAxis (int x) {
		return x + ScrollTracker.currentXOffset;
	}
}
