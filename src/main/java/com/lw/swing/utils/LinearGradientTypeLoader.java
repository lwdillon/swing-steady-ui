package com.lw.swing.utils;

import org.jdesktop.fuse.TypeLoader;
import org.jdesktop.fuse.TypeLoaderFactory;
import org.jdesktop.fuse.TypeLoadingException;

import java.awt.*;
import java.util.Map;

public class LinearGradientTypeLoader extends TypeLoader<LinearGradientPaint> {
    public LinearGradientTypeLoader() {
        super(LinearGradientPaint.class);
    }

    @Override
    public LinearGradientPaint loadType(String name, String value, Class<?> resolver, Map<String, Object> map) {
        String[] parts = value.split("\\s?\\|\\s?");
        if (parts.length < 4) {
            throw new TypeLoadingException("Theme resource " + name +
                    " is not a valid gradient.");
        }

        String[] startPoint = parts[0].split(",");
        if (startPoint.length != 2) {
            throw new TypeLoadingException("Start point " + parts[0] +
                    " is not valid in " + name);
        }
        float startX;
        try {
            startX = Float.parseFloat(startPoint[0]);
        } catch (NumberFormatException e) {
            throw new TypeLoadingException("Gradient start point is invalid in " + name + ".", e);
        }
        float startY;
        try {
            startY = Float.parseFloat(startPoint[1]);
        } catch (NumberFormatException e) {
            throw new TypeLoadingException("Gradient start point is invalid in " + name + ".", e);
        }

        String[] endPoint = parts[1].split(",");
        if (endPoint.length != 2) {
            throw new TypeLoadingException("Start point " + parts[1] +
                    " is not valid in " + name);
        }

        float endX;
        try {
            endX = Float.parseFloat(endPoint[0]);
        } catch (NumberFormatException e) {
            throw new TypeLoadingException("Gradient end point is invalid in " + name + ".", e);
        }
        float endY;
        try {
            endY = Float.parseFloat(endPoint[1]);
        } catch (NumberFormatException e) {
            throw new TypeLoadingException("Gradient end point is invalid in " + name + ".", e);
        }

        float[] fractions = new float[parts.length - 2];
        Color[] colors = new Color[parts.length - 2];
        for (int i = 2; i < parts.length; i++) {
            String[] stop = parts[i].split(",");
            if (stop.length != 2) {
                throw new TypeLoadingException("Gradient stop " + parts[i] +
                        " is not valid in " + name);
            }
            try {
                fractions[i - 2] = Float.parseFloat(stop[0]);
            } catch (NumberFormatException e) {
                throw new TypeLoadingException("Gradient stop is invalid in " + name + ".", e);
            }
            TypeLoader<?> loader = TypeLoaderFactory.getLoaderForType(Color.class);
            colors[i - 2] = (Color) loader.loadType("gradient stop", stop[1], resolver, null);
        }

        return new LinearGradientPaint(startX, startY, endX, endY,
                fractions, colors);
    }
}
