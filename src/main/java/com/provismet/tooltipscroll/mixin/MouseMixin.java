package com.provismet.tooltipscroll.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;

import com.provismet.tooltipscroll.ScrollTracker;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"), cancellable = false)
    private void trackWheel(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        long mcHandle = client.getWindow().getHandle();
        if (window != mcHandle) return;
        if (Screen.hasShiftDown()) ScrollTracker.setDX((int)(vertical * 4));
        else ScrollTracker.setDY((int)(vertical * 4));
    }
}
