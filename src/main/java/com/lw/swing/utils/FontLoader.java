package com.lw.swing.utils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description:
 * @className: IconFont
 * @author: liwen
 * @date: 2018/11/8 18:20
 */
public class FontLoader {

    static final float FONT_SIZE = 16f;


    public static Font loadFont(String fontFileName)  //第一个参数是外部字体名，第二个是字体大小
    {
        InputStream in = null;
        try {
            in = IconFont.class.getClassLoader().getResourceAsStream(fontFileName);
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, in);
            Font dynamicFontPt = dynamicFont.deriveFont(FONT_SIZE);
            return dynamicFontPt;
        } catch (Exception e)//异常处理
        {
            e.printStackTrace();
            return new Font("宋体", Font.PLAIN, (int) FONT_SIZE);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getIcon(String icon) {
        return IconEnum.getIcon(icon);
    }
}
