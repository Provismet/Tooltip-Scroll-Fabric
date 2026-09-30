package com.provismet.tooltipscroll.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.provismet.tooltipscroll.Options;
import com.provismet.tooltipscroll.ScrollTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GuiGraphicsExtractor.class, priority = 1001)
public abstract class GuiGraphicsExtractorMixin {
    @Shadow @Final
    private Matrix3x2fStack pose;

    // Allows tooltips to be moved with keybinds.
	// It's just a QOL feature because some menus are scrollable and would be moved by the scroll wheel.
	@Inject (
		method = "tooltip",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;")
	)
	public void applyTracker (Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @org.jspecify.annotations.Nullable Identifier style, boolean extraSpaceAfterFirstLine, CallbackInfo ci) {
		ScrollTracker.unlock();
		ScrollTracker.update();
		ScrollTracker.setItem(lines);
	}

	// Moves the tooltip once it has been positioned, because start-on-top needs to know where vanilla placed it.
	@Inject(
        method = "tooltip",
        at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;", shift = At.Shift.AFTER)
	)
	private void editXY (Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @org.jspecify.annotations.Nullable Identifier style, boolean extraSpaceAfterFirstLine, CallbackInfo ci, @Local(ordinal = 6) LocalIntRef effectiveX, @Local(ordinal = 7) LocalIntRef effectiveY) {
        ScrollTracker.alignToTop(effectiveY.get());

        if (Options.matrixMode) {
            this.pose.translate(ScrollTracker.getXOffset(), ScrollTracker.getYOffset()); // Reverted by vanilla's own popMatrix().
            return;
        }

		effectiveX.set(effectiveX.get() + ScrollTracker.getXOffset());
		effectiveY.set(effectiveY.get() + ScrollTracker.getYOffset());
	}
}
