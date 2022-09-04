package com.provismet.tooltipscroll.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.provismet.tooltipscroll.ScrollTracker;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Mixin(Screen.class)
class ScreenMixin {
	@Inject(method = "close", at = @At("HEAD"))
	private void onClose(CallbackInfo info) {ScrollTracker.reset();}

	@Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"))
	private void onStack(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {ScrollTracker.setItem(stack, x, y);}

	@ModifyArg(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/gui/screen/Screen;renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;Ljava/util/Optional;II)V"
	), index = 3)
	private int onX(int x) {return ScrollTracker.getX();}

	@ModifyArg(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/gui/screen/Screen;renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;Ljava/util/Optional;II)V"
	), index = 4)
	private int onY(int y) {return ScrollTracker.getY();}

	@Inject(method = "renderTooltipFromComponents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onDone(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo ci,
		int width, int height, int l, int m
	) {
		ScrollTracker.setBounds(width, height);
	}
}
