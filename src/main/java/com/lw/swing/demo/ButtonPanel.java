package com.lw.swing.demo;

import com.lw.swing.ui.WButtonGroup;
import com.lw.swing.ui.WIconButton;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * @description:
 * @className: ButtonPanel
 * @author: liwen
 * @date: 2019/1/8 11:05
 */
public class ButtonPanel extends JComponent {

    public ButtonPanel() {
        initSwing();
    }

    private void initSwing() {

        this.setLayout(new GridLayout(5, 1));

        JPanel panel1 = new JPanel(new GridLayout(2, 1));
        panel1.setOpaque(false);
        panel1.setBorder(BorderFactory.createTitledBorder("默认按钮"));
        panel1.add(createPanel(true, true, false));
        panel1.add(createPanel(true, true, true));

        JPanel panel2 = new JPanel(new GridLayout(2, 1));
        panel2.setOpaque(false);
        panel2.setBorder(BorderFactory.createTitledBorder("无边框按钮"));
        panel2.add(createPanel(true, false, false));
        panel2.add(createPanel(true, false, true));

        JPanel panel3 = new JPanel(new GridLayout(2, 1));
        panel3.setOpaque(false);
        panel3.setBorder(BorderFactory.createTitledBorder("无填充按钮"));
        panel3.add(createPanel(false, true, false));
        panel3.add(createPanel(false, true, true));


        WButtonGroup wButtonGroup1 = new WButtonGroup();
        wButtonGroup1.addButton("添加", "");
        wButtonGroup1.addButton("修改", "");
        wButtonGroup1.addButton("删除", "");
        wButtonGroup1.addButton("保存", "");

        WButtonGroup wButtonGroup2 = new WButtonGroup();
        wButtonGroup2.addButton("", "fa-plus");
        wButtonGroup2.addButton("", "fa-pencil-alt");
        wButtonGroup2.addButton("", "fa-minus");
        wButtonGroup2.addButton("", "fa-save");

        WButtonGroup wButtonGroup3 = new WButtonGroup();
        wButtonGroup3.addButton("添加", "fa-plus");
        wButtonGroup3.addButton("修改", "fa-pencil-alt");
        wButtonGroup3.addButton("删除", "fa-minus");
        wButtonGroup3.addButton("保存", "fa-save");

        JPanel panel4 = new JPanel(new GridLayout(1, 3));
        panel4.setOpaque(false);
        panel4.setBorder(BorderFactory.createTitledBorder("按钮组"));
        panel4.add(wButtonGroup1);
        panel4.add(wButtonGroup3);
        panel4.add(wButtonGroup2);


        this.add(panel1);
        this.add(panel2);
        this.add(panel3);
        this.add(panel4);
    }

    private JPanel createPanel(boolean isFill, boolean isborder, boolean color) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.add(createButton("默认按钮", null, null, isFill, false, isborder, color));
        panel.add(createButton("带图标默认按钮", "fa-laugh-beam", null, isFill, false, isborder, color));
        panel.add(createButton("圆角按钮", null, null, isFill, true, isborder, color));
        panel.add(createButton("带图标圆角按钮", "fa-laugh-beam", null, isFill, true, isborder, color));
        panel.add(createButton("", "fa-laugh-beam", "只有图标", isFill, false, isborder, color));
        panel.add(createButton("", "fa-laugh-beam", "只有图标", isFill, true, isborder, color));
        panel.add(createButton("", "fa-laugh-beam", "只有图标", false, false, false, color));
        panel.add(createButton("", "fa-laugh-beam", "只有图标", false, true, false, color));

        return panel;
    }

    private WIconButton createButton(String text, String iconfont, String tip, boolean isFill, boolean isRound, boolean isborder, boolean color) {
        WIconButton button = new WIconButton(text, iconfont, isRound);
        button.setFill(isFill);
        if (StringUtils.isNotBlank(tip)) {
            button.setToolTipText(tip);
        }
        button.setDrawBorder(isborder);
        if (color) {
            button.setBackground(getColor());
        }
        return button;
    }

    private Color getColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r, g, b);
    }
}
