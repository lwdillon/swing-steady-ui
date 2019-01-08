package com.lw.swing.utils;

import java.awt.*;

/**
 * 界面字体
 */
public  class SysFont {
    public static final Font APP_FONT;

    static {
        APP_FONT = FontLoader.loadFont("font/PingFang.ttc");
    }

    public static Font FONT_10_PLAIN = APP_FONT.deriveFont(Font.PLAIN, 10);//主要颜色
    public static Font FONT_12_PLAIN = APP_FONT.deriveFont(Font.PLAIN, 12);//主要颜色
    public static Font FONT_12_BOLD = APP_FONT.deriveFont(Font.BOLD, 12);//主要颜色
    public static Font FONT_13_PLAIN = APP_FONT.deriveFont(Font.PLAIN, 13);//主要颜色
    public static Font FONT_13_BOLD = APP_FONT.deriveFont(Font.BOLD, 13);//主要颜色
    public static Font FONT_14_BOLD = APP_FONT.deriveFont(Font.BOLD, 14);//主要颜色
    public static Font FONT_14_PLAIN = APP_FONT.deriveFont(Font.PLAIN, 14);//主要颜色
    public static Font FONT_16_BOLD = APP_FONT.deriveFont(Font.BOLD, 16);//主要颜色
    public static Font FONT_16_PLAN = APP_FONT.deriveFont(Font.PLAIN, 16);//主要颜色
    public static Font FONT_20_BOLD = APP_FONT.deriveFont(Font.BOLD, 20);//主要颜色
    public static Font FONT_20_PLAN = APP_FONT.deriveFont(Font.PLAIN, 20);//主要颜色
    public static Font FONT_18_PLAN = APP_FONT.deriveFont(Font.PLAIN, 18);//主要颜色
    public static Font FONT_18_BOLD = APP_FONT.deriveFont(Font.BOLD, 18);//主要颜色
    public static Font FONT_16_PLAIN_NORMAL = new Font("微软雅黑", Font.PLAIN, 16);
}