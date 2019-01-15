package com.lw.swing.demo;

import com.lw.swing.ui.tabbedpanel.WTabbedPane;

import javax.swing.*;
import java.awt.*;

/**
 * @description:
 * @className: TabbedDemo
 * @author: liwen
 * @date: 2019/1/15 16:19
 */
public class TabbedDemo extends JComponent {

    public TabbedDemo() {



        this.setLayout(new GridLayout(2, 1,20,10));
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        this.add(createTabledPanel(JTabbedPane.TOP,false));
        this.add(createTabledPanel(JTabbedPane.TOP,true));

    }

    private WTabbedPane createTabledPanel(int tabPlacement,boolean isfill) {

        WTabbedPane tabbedPane = new WTabbedPane(tabPlacement);
        tabbedPane.setFillBackground(isfill);
        tabbedPane.addTab("默认选项", new JLabel("默认选项"));
        tabbedPane.addTab("fa-home", "带图标选项", new JLabel("带图标选项"));
        tabbedPane.addTab("","不可关闭选项", new JLabel("不可关闭选项"),false);

        return tabbedPane;
    }


}
