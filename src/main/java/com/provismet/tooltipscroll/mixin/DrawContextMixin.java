package com.provismet.tooltipscroll.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.provismet.tooltipscroll.ScrollTracker;
import com.provismet.tooltipscroll.Options;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
	// Allows tooltips to be moved with keybinds.
	// It's just a QOL feature because some menus are scrollable and would be moved by the scroll wheel.
	@Inject (
		method = "drawTooltipImmediately",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/TooltipPositioner;getPosition(IIIIII)Lorg/joml/Vector2ic;")
	)
	public void applyTracker (TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo ci) {
		ScrollTracker.unlock();
		ScrollTracker.update();
		ScrollTracker.setItem(components);
	}

	@Inject(
			method = "drawTooltipImmediately",
			at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;")
	)
	private void editXY (TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo info, @Local(ordinal = 6) LocalIntRef effectiveX, @Local(ordinal = 7) LocalIntRef effectiveY) {
		effectiveX.set(effectiveX.get() + ScrollTracker.getXOffset());
		effectiveY.set(effectiveY.get() + ScrollTracker.getYOffset());

    if (Options.startOnTop && !ScrollTracker.hasMoved()) {
			int originalY = effectiveY.get();
			if (effectiveY.get() < 4) {
				effectiveY.set(4);
				ScrollTracker.setInitialYOffset(4 - originalY);
			}
    }
	}
}
