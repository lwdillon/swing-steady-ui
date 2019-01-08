package com.lw.swing.ui;



import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;

import javax.swing.*;
import java.awt.*;

public class WPasswordTextField extends WTextField {

    private JTextField textField;

    @InjectedResource
    private Color borderColor = new Color(1f, 1f, 1f, .5f);
    @InjectedResource
    private Color textColor = new Color(1f, 1f, 1f, .7f);
    @InjectedResource
    private Color errorColor = Color.RED;
    @InjectedResource
    private Font textFont = new Font("宋体", Font.PLAIN, 16);

    public WPasswordTextField() {

        this("");
    }

    public WPasswordTextField(String titleText) {

        this("", titleText);
    }

    public WPasswordTextField(String iconfont, String titleText) {

        this(iconfont, titleText, "", "");
    }

    public WPasswordTextField(String iconfont, String titleText, String hintText, String wranText) {

        super(iconfont, titleText, hintText, wranText);

        ResourceInjector.get().inject(this);

    }

    @Override
    public void initFontColor() {
        getIconLable().setFont(getIconLable().getFont().deriveFont(textFont.getSize2D() + 2.0f));
        getTitleLable().setFont(textFont);
        getTextField().setFont(textFont);

        getIconLable().setForeground(textColor);
        getTitleLable().setForeground(textColor);
        getTextField().setForeground(textColor);
        getTextField().setCaretColor(textColor);
    }

    @Override
    public JTextField getTextField() {

        if (textField == null) {
            textField = new JPasswordField() {

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    if(isEnabled()){
                    if (getText().isEmpty()) {
                        getClearBut().setVisible(false);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(getForeground());

                        FontMetrics fontMetrics = g2.getFontMetrics();
                        int fontY = fontMetrics.getAscent()+fontMetrics.getLeading();
                        int y=(getHeight()-fontMetrics.getHeight())/2+fontY;

                        g2.drawString(getHintText(), 0, y);
                        g2.dispose();
                    } else {

                        if (!getClearBut().isVisible()) {
                            getClearBut().setVisible(true);
                        }
                    }}else {
                        getClearBut().setVisible(false);
                    }
                }


            };

        }
        textField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        textField.setOpaque(false);
        textField.setLayout(new BorderLayout());
        textField.add(getClearBut(), BorderLayout.EAST);
        textField.add(getWranLable(), BorderLayout.SOUTH);
        return textField;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
        initFontColor();
    }
}
