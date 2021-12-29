package com.example.examplemod;

import org.lwjgl.glfw.GLFW;
import org.vivecraft.provider.InputSimulator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;

public abstract class MethodHolder implements RenderTargetExtension {

	public static VRTextureTarget createTextureTarget(String name, int width, int height, boolean usedepth, boolean onMac, int texid, boolean depthtex, boolean linearFilter) {
		VRTextureTarget t = new VRTextureTarget(name, width, height, usedepth, onMac, texid, depthtex, linearFilter);
		return t;
	}
	
	public static boolean isKeyDown(int i) {
		return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), i) == 1 || InputSimulator.isKeyDown(i);
	}
	
	public static void notifyMirror(String text, boolean clear, int lengthMs)
	{
		DataHolder dataHolder = DataHolder.getInstance();
		dataHolder.mirroNotifyStart = System.currentTimeMillis();
		dataHolder.mirroNotifyLen = (long)lengthMs;
		dataHolder.mirrorNotifyText = text;
		dataHolder.mirrorNotifyClear = clear;
	}
	
	public static void rotateDeg(PoseStack pose, float angle, float x, float y, float z) {
		Vector3f vec = new Vector3f(x, y, z);
		Quaternion quat = vec.rotationDegrees(angle);
		pose.mulPose(quat);
	}

	public static void rotateDegXp(PoseStack matrix, int i) {
		matrix.mulPose(Vector3f.XP.rotationDegrees(i));
	}
}
