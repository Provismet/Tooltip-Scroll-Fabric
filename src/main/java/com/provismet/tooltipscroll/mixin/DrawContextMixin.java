package com.provismet.tooltipscroll.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.provismet.tooltipscroll.Options;
import com.provismet.tooltipscroll.ScrollTracker;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.Identifier;

@Mixin(value = GuiGraphics.class, priority = 1001)
public abstract class DrawContextMixin {
    @Shadow @Final
    private Matrix3x2fStack pose;

    // Allows tooltips to be moved with keybinds.
	// It's just a QOL feature because some menus are scrollable and would be moved by the scroll wheel.
	@Inject (
		method = "renderTooltip",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;")
	)
	public void applyTracker (Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo ci) {
		ScrollTracker.unlock();
		ScrollTracker.update();
		ScrollTracker.setItem(components);
	}

	@Inject(
        method = "renderTooltip",
        at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;")
	)
	private void editXY (Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo info, @Local(ordinal = 6) LocalIntRef effectiveX, @Local(ordinal = 7) LocalIntRef effectiveY) {
        if (Options.matrixMode) return;

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

    @Inject(
        method = "renderTooltip",
        at = @At("HEAD")
    )
    private void headMatrices(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, Identifier texture, CallbackInfo info) {
        if (!Options.matrixMode) return;

        this.pose.pushMatrix();
        this.pose.translate(ScrollTracker.getXOffset(), ScrollTracker.getYOffset());
    }

    @Inject(
        method = "renderTooltip",
        at = @At("TAIL")
    )
    private void tailMatrices (Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, Identifier texture, CallbackInfo info) {
        if (!Options.matrixMode) return;
        this.pose.popMatrix();
    }
}
