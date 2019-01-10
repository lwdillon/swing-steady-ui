package com.lw.swing.ui;

import javax.swing.*;
import java.awt.*;

/**
 * 按钮组
 * Created by liwen on 2017/6/5.
 */
public class WButtonGroup extends JComponent {

    private ButtonGroup group;

    public WButtonGroup() {
        this(false);
    }

    public WButtonGroup(boolean radio) {
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        group = new ButtonGroup();
    }


    public WItemButoon addButton(String text, String iconfont) {
        WItemButoon itemButton = new WItemButoon(text, iconfont);
        return addButton(itemButton);
    }

    public WItemButoon addButton(String text) {
        return addButton(text, null);
    }

    public WItemButoon addButton(WItemButoon itemButton) {
        group.add(itemButton);
        return (WItemButoon) this.add(itemButton);
    }


}
