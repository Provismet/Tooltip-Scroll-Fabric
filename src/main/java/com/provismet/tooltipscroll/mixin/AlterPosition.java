package com.provismet.tooltipscroll.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

import com.provismet.tooltipscroll.ScrollTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class AlterPosition {
	// Reset the tracker whenever a GUI window closes.
	@Inject (method = "close()V", at = @At("HEAD"))
	public void resetTrackerOnScreenClose (CallbackInfo info) {
		ScrollTracker.reset();
	}

	// This inject allows the page-up and page-down buttons to perform scrolling.
	// It's just a QOL feature because some menus are scrollable and would be moved by the scrollwheel.
	@Inject (method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"))
	public void applyTracker (MatrixStack matrices, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo info) {
		if (components.size() > 0) {
			ScrollTracker.unlock();
			ScrollTracker.update();
			ScrollTracker.setItem(components);
		}
	}

	// Using an invoke inject here because the tooltip coordinates get checked for out of bounds positions. I want the scroll offset to only apply after the bound check.
	// Targeting a method invoke because it was just conveniently placed after the bound check.

	// l is the variable that determines y-axis position of a tooltip.
	@ModifyVariable (method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", ordinal = 7, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE))
	private int modifyYAxis (int y) {
		return y + ScrollTracker.getYOffset();
	}

	// k is the variable that determines x-axis position of a tooltip.
	@ModifyVariable (method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", ordinal = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE))
	private int modifyXAxis (int x) {
		return x + ScrollTracker.getXOffset();
	}
}
