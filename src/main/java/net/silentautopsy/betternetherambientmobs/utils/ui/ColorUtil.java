package net.silentautopsy.betternetherambientmobs.utils.ui;

public class ColorUtil
{
    private static final int ALPHA = 255 << 24;

    public static int color(int r, int g, int b)
    {
        return ALPHA | (r << 16) | (g << 8) | b;
    }
}