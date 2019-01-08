package com.lw.swing.ui.tabbedpanel;



import com.lw.swing.ui.WIconButton;
import com.lw.swing.utils.IconFont;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import java.awt.*;

public class WTabbedPaneUI extends MetalTabbedPaneUI {

    private WTabbedPane tab = null;
    private int firstTabIndent = 0;

    public WTabbedPaneUI(WTabbedPane tab) {
        this.tab = tab;
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        tabInsets = new Insets(0, 0, 0, 0);
        contentBorderInsets = new Insets(2, 2, 2, 2);
        selectedTabPadInsets = new Insets(0, 0, 0, 0);
        tabAreaInsets = new Insets(0, 0, 0, 0);

    }

    @Override
    protected Rectangle getTabBounds(int tabIndex, Rectangle dest) {
        Rectangle bounds = super.getTabBounds(tabIndex, dest);
        //give first tab a leading space.
        bounds.x += firstTabIndent;
        return bounds;
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement,
                                  int tabIndex, int x, int y, int w, int h,
                                  boolean isSelected) {

        //paint nothing, expect the bottom line in the leading indent.

//        int lineY = tab.getPreferredTabHeight() ;
//        Graphics2D g2= (Graphics2D) g.create();
//        g2.setColor(Color.red);
//        g2.setStroke(new BasicStroke(2f));
//        g2.drawLine(0, lineY, tab.getWidth(), lineY);
//        g2.dispose();
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        g.setColor(tab.getBackground());
        g.fillRect(0, 0, tab.getWidth(), tab.getPreferredTabHeight());

        if (tab.isFillBackground()) {
            if (selectedIndex == tab.getSelectedIndex()) {
                g.setColor(tab.getSelectedBackground());
            }else {
                g.setColor(tab.getBackground());
            }
            g.fillRect(0, tab.getPreferredTabHeight(), tab.getWidth(), tab.getHeight());
        }

    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement,
                                      int tabIndex, int x, int y, int w, int h,
                                      boolean isSelected) {
//        g.setColor(tabBackground);
//        g.fillRect(0, 0, tab.getWidth(), tab.getHeight());
    }


    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        int width = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
        //use this to remove the tab between tabs.
        return width - 5;
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return tab.getPreferredTabHeight();
    }

    @Override
    protected JButton createScrollButton(int direction) {


        String iconFontText = "";
        if (direction == SOUTH) {
            iconFontText = "fa-caret-down";
        } else if (direction == NORTH) {
            iconFontText = "fa-caret-up";
        } else if (direction == EAST) {
            iconFontText = "fa-caret-right";
        } else if (direction == WEST) {
            iconFontText = "fa-caret-left";
        }
        ScrollButton iconButton = new ScrollButton(iconFontText);
        return iconButton;
    }

    class ScrollButton extends WIconButton implements UIResource {

        public ScrollButton(String icontext) {
            super(IconFont.getIcon(icontext));
            this.setFont(IconFont.ICON_FONT.deriveFont(18f));
            this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(25, 50);
        }
    }
}
