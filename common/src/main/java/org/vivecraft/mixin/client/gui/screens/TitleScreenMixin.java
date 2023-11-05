package org.vivecraft.mixin.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.client.gui.screens.UpdateScreen;
import org.vivecraft.client.utils.UpdateChecker;
import org.vivecraft.client_vr.ClientDataHolderVR;
import org.vivecraft.client_vr.VRState;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Unique
    private Button vivecraft$vrModeButton;
    @Unique
    private Button vivecraft$updateButton;

    @Inject(at = @At("TAIL"), method = "render")
    public void vivecraft$renderToolTip(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        vivecraft$updateButton.visible = UpdateChecker.hasUpdate;

        if (vivecraft$vrModeButton.visible && vivecraft$vrModeButton.isMouseOver(i, j)) {
            renderTooltip(poseStack, font.split(Component.translatable("vivecraft.options.VR_MODE.tooltip"), Math.max(width / 2 - 43, 170)), i, j);
        }
        if (VRState.vrInitialized && !VRState.vrRunning) {
            Component hotswitchMessage = Component.translatable("vivecraft.messages.vrhotswitchinginfo");
            renderTooltip(poseStack, font.split(hotswitchMessage, 280), width / 2 - 140 - 12, 17);
        }
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PanoramaRenderer;render(FF)V"), method = "render", index = 1)
    public float vivecraft$maybeNoPanorama(float alpha) {
        return VRState.vrRunning && ClientDataHolderVR.getInstance().menuWorldRenderer.isReady() ? 0.0F : alpha;
    }
}
