package com.provismet.tooltipscroll.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import java.util.List;

import com.provismet.tooltipscroll.ScrollTracker;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
	// Allows tooltips to be moved with keybinds.
	// It's just a QOL feature because some menus are scrollable and would be moved by the scroll wheel.
	@Inject (
		method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/TooltipPositioner;getPosition(IIIIII)Lorg/joml/Vector2ic;", shift = At.Shift.BEFORE)
	)
	public void applyTracker (TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo ci) {
		ScrollTracker.unlock();
		ScrollTracker.update();
		ScrollTracker.setItem(components);
	}

	// Using an invoke inject here because the tooltip coordinates get checked for out of bounds positions. I want the scroll offset to only apply after the bound check.
	// Targeting a method invoke because it was just conveniently placed after the bound check.

	// l is the variable that determines y-axis position of a tooltip.
	@ModifyVariable(
		method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
		ordinal = 7,
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE)
	)
	private int modifyYAxis (int y) {
		return y + ScrollTracker.getYOffset();
	}

	// k is the variable that determines x-axis position of a tooltip.
	@ModifyVariable(
		method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
		ordinal = 6,
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE)
	)
	private int modifyXAxis (int x) {
		return x + ScrollTracker.getXOffset();
	}
}
