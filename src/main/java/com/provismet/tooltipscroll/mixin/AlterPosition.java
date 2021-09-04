package com.provismet.tooltipscroll.mixin;

import com.provismet.tooltipscroll.ScrollTracker;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class AlterPosition {
	// Reset the ScrollTracker when the current Tooltip is closed
	@Inject(method = "onClose()V", at = @At("HEAD"))
	public void resetTrackerOnScreenClose(CallbackInfo info) {
		ScrollTracker.reset();
	}
	
	// Set the current item in ScrollTracker to the component
	@Inject(method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", at = @At("HEAD"))
	public void updateTracker(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo info) {
		ScrollTracker.setItem(components);
	}

	// Because `renderTooltipFromComponents` tries to clamp the "x" and "y" by setting them to "l" and "m" and then comparing them
	// to "this.width" and "this.height", we need to modify these two after the clamping has been done. the method after this clamp is "matricies.push()"
	@ModifyVariable(method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", ordinal = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE))
	private int modifyXAxis(int l) {
		return l + ScrollTracker.currentXOffset;
	}

	@ModifyVariable(method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", ordinal = 5, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE))
	private int modifyYAxis(int m) {
		return m + ScrollTracker.currentYOffset;
	}
}
