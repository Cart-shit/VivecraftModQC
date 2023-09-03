package org.vivecraft.utils;

public class VLoader {
    static {
        System.loadLibrary("openvr_api");
    }

    public static native long createEGLImage(long buffer);
    public static native int createGLImage(long eglImage, int width, int height);
    public static native void flushFrame(int nativeImage, long eglImage);
}
