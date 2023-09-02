package org.vivecraft.utils;

public class VLoader {
    static {
        System.loadLibrary("openvr_api");
    }

    public static native int createGLImage(long buffer);
}
