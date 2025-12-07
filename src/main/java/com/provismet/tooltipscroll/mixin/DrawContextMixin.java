package com.provismet.tooltipscroll.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.provismet.tooltipscroll.Options;
import com.provismet.tooltipscroll.ScrollTracker;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = DrawContext.class, priority = 1001)
public abstract class DrawContextMixin {
    @Shadow
    @Final
    private MatrixStack matrices;

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

	@Inject(
        method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE)
	)
	private void editXY (TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, Identifier texture, CallbackInfo info, @Local(ordinal = 6) LocalIntRef effectiveX, @Local(ordinal = 7) LocalIntRef effectiveY) {
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
        method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
        at = @At("HEAD")
    )
    private void headMatrices(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo info) {
        if (!Options.matrixMode) return;

        this.matrices.push();
        this.matrices.translate(ScrollTracker.getXOffset(), ScrollTracker.getYOffset(), 0);
    }

    @Inject(
        method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
        at = @At("TAIL")
    )
    private void tailMatrices (TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo info) {
        if (!Options.matrixMode) return;
        this.matrices.pop();
    }
}
