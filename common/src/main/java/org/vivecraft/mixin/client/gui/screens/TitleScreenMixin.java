package org.vivecraft.mixin.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.client.utils.UpdateChecker;
import org.vivecraft.client_vr.ClientDataHolderVR;
import org.vivecraft.client_vr.VRState;
import org.vivecraft.client.gui.screens.UpdateScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    //TODO Add config file
//    private final Properties vrConfig = new Properties();
//    private final Path vrConfigPath = Xplat.getConfigPath("vivecraft-config.properties");
    private boolean showError = false;

    private Button updateButton;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", shift = At.Shift.AFTER, ordinal = 1), method = "createNormalMenuOptions")
    public void initFullGame(CallbackInfo ci) {
        addVRModeButton();
    }

    @Inject(at = @At("TAIL"), method = "createDemoMenuOptions")
    public void initDemo(CallbackInfo ci) {
        addVRModeButton();
    }

    private void addVRModeButton() {
        updateButton = new Button.Builder(Component.translatable("vivecraft.gui.update"), (button) -> minecraft.setScreen(new UpdateScreen()))
                .size(56, 20)
                .pos(this.width / 2 + 104, this.height / 4 + 96)
                .build();

        updateButton.visible = UpdateChecker.hasUpdate;

        this.addRenderableWidget(updateButton);
    }

    private String getIcon() {
        return (showError ? "§c\u26A0§r " : "");
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void renderToolTip(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        updateButton.visible = UpdateChecker.hasUpdate;

        if (VRState.vrInitialized && !VRState.vrRunning) {
            Component hotswitchMessage = Component.translatable("vivecraft.messages.vrhotswitchinginfo");
            renderTooltip(poseStack, font.split(hotswitchMessage, 280), width / 2 - 140 - 12, 17);
        }
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PanoramaRenderer;render(FF)V"), method = "render", index = 1)
    public float vivecraft$maybeNoPanorama(float alpha) {
        return VRState.vrRunning && (ClientDataHolderVR.getInstance().menuWorldRenderer.isReady() || ClientDataHolderVR.getInstance().vrSettings.menuWorldFallbackPanorama) ? 0.0F : alpha;
    }
}
