package org.vivecraft.util;

public class VLoader {
    static {
        System.loadLibrary("openvr_api");
    }

    public static native void setEGLGlobal();
    public static native int getImage(int width, int height);
}
