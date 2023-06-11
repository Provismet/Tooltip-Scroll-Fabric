package com.provismet.tooltipscroll.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.provismet.tooltipscroll.ScrollTracker;

import net.minecraft.client.gui.screen.Screen;

// As of 1.20, tooltip logic has been separated from screen logic.
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // Reset the tracker whenever a GUI window closes.
	@Inject (method = "close()V", at = @At("HEAD"))
	public void resetTrackerOnScreenClose (CallbackInfo info) {
		ScrollTracker.reset();
	}
}
