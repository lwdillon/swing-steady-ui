package com.lw.swing.ui.slider;

import com.lw.swing.utils.IconFont;

import javax.swing.*;
import java.awt.*;


/**
 * Code is from BasicArrowButton written by David Kloba.
 * <p>
 * Ripped out of BasicArrowButton from PLAF. Chopped out the button stuff. Now inside a Panel
 *
 * @author David Kloba
 */

public class ArrowPanel extends JLabel {

    private static final long serialVersionUID = 1L;

    protected int direction;

    private double angle = 0;

    public ArrowPanel(int direction) {

        setFont(IconFont.ICON_FONT);
        setHorizontalAlignment(JLabel.CENTER);
        setDirection(direction);
    }


    public void setDirection(int dir) {
        direction = dir;
        switch (direction) {
            case NORTH:
                setText(IconFont.getIcon("fa-chevron-up"));
                break;
            case SOUTH:
                setText(IconFont.getIcon("fa-chevron-down"));
                break;
            case WEST:
                setText(IconFont.getIcon("fa-chevron-left"));
                break;
            case EAST:
                setText(IconFont.getIcon("fa-chevron-right"));
                break;
        }
    }


    public void changeDirection(int d) {
        setDirection(d);
    }



}
