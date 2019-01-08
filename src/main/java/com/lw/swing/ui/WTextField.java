package com.lw.swing.ui;

import com.lw.swing.utils.IconFont;
import com.lw.swing.utils.RegExpValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class WTextField extends JPanel {
    private JTextField textField;
    private JLabel iconLable;
    private JLabel wranLable;
    private JLabel titleLable;
    private JPanel titlePanel;
    private PulsatingBorder pulsatingBorder;
    private Animator animationBoder;
    private JLabel clearBut;
    private String regex;
    private boolean regexcheck;

    private String iconfont;
    private String wranText;
    private String hintText;
    private String titleText;

    private Border textFiledBorder;
    private Border errorBorder;

    @InjectedResource
    private Color borderColor = new Color(1f, 1f, 1f, .5f);
    @InjectedResource
    private Color textColor = new Color(1f, 1f, 1f, .7f);
    @InjectedResource
    private Color errorColor = Color.RED;
    @InjectedResource
    private Font textFont = new Font("宋体", Font.PLAIN, 16);

    public WTextField() {

        this("");
    }

    public WTextField(String titleText) {

        this("", titleText);
    }

    public WTextField(String iconfont, String titleText) {

        this(iconfont, titleText, "", "");
    }

    public WTextField(String iconfont, String titleText, String hintText, String wranText) {

        ResourceInjector.get().inject(this);
        this.titleText = titleText;
        this.iconfont = iconfont;
        this.hintText = hintText;
        this.wranText = wranText;

        initSwing();
        initListener();
    }

    private void initSwing() {

        pulsatingBorder = new PulsatingBorder(this);

        textFiledBorder = BorderFactory.createLineBorder(borderColor);
        errorBorder = BorderFactory.createLineBorder(errorColor);

        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBorder(textFiledBorder);
        this.add(getTextField(), BorderLayout.CENTER);
        this.add(getTitlePanel(), BorderLayout.WEST);
        this.setPreferredSize(new Dimension(240, 35));


        initFontColor();

    }


    public void initFontColor() {
        getIconLable().setFont(getIconLable().getFont().deriveFont(textFont.getSize2D() + 2.0f));
        getTitleLable().setFont(textFont);
        getTextField().setFont(textFont);

        getIconLable().setForeground(textColor);
        getTitleLable().setForeground(textColor);
        getTextField().setForeground(textColor);
        getTextField().setCaretColor(textColor);
    }

    private void initListener() {
        getClearBut().addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                getTextField().setText("");

            }


        });
        getTextField().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (isEnabled() && StringUtils.isNoneBlank(getRegex())) {
                    if (isRegexcheck()) {
                        stop();
                    } else {
                        start();
                    }
                }
            }
        });
    }

    private Animator getAnimationBoder() {
        if (animationBoder == null) {
            animationBoder = new Animator.Builder().setRepeatCount(5).setDuration(900, MILLISECONDS).setRepeatBehavior(Animator.RepeatBehavior.REVERSE).setInterpolator(new AccelerationInterpolator(.5, .5)).build();
            animationBoder.addTarget(PropertySetter.getTarget(pulsatingBorder, "thickness", 0.0f, 1.0f));
            animationBoder.addTarget(new TimingTargetAdapter() {
                @Override
                public void end(Animator source) {
                    super.end(source);
                    getWranLable().setVisible(false);
                    if (isRegexcheck()) {
                        setBorder(textFiledBorder);
                    } else {
                        setBorder(errorBorder);

                    }
                }


                @Override
                public void begin(Animator source) {
                    setBorder(new CompoundBorder(BorderFactory.createLineBorder(borderColor), pulsatingBorder));

                    getWranLable().setVisible(true);
                }
            });
        }
        return animationBoder;
    }

    public void start() {
        getAnimationBoder().restart();
    }

    public void stop() {
        if (getAnimationBoder().isRunning()) {
            getAnimationBoder().stop();
        } else {
            if (isRegexcheck()) {
                setBorder(textFiledBorder);
            } else {
                setBorder(errorBorder);

            }
        }
    }

    public String getText() {
        return getTextField().getText();
    }

    public void setText(String text) {
        getTextField().setText(text);
    }

    public JTextField getTextField() {

        if (textField == null) {
            textField = new JTextField(20) {

                @Override
                protected void paintComponent(Graphics g) {


                    if (isEnabled()) {
                        if (getText().isEmpty()) {
                            getClearBut().setVisible(false);
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setColor(getForeground());
                            FontMetrics fontMetrics = g2.getFontMetrics();
                            int fontY = fontMetrics.getAscent() + fontMetrics.getLeading();
                            int y = (getHeight() - fontMetrics.getHeight()) / 2 + fontY;

                            g2.drawString(hintText, 0, y);
                            g2.dispose();
                        } else {

                            if (!getClearBut().isVisible()) {
                                getClearBut().setVisible(true);
                            }
                        }
                    } else {
                        getClearBut().setVisible(false);
                    }

                    super.paintComponent(g);

                }


            };

            textField.setCaretColor(getForeground());
            textField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
            textField.setLayout(new BorderLayout());
            textField.setOpaque(false);
            textField.add(getClearBut(), BorderLayout.EAST);
            textField.add(getWranLable(), BorderLayout.SOUTH);

        }
        return textField;
    }


    public JLabel getClearBut() {
        if (clearBut == null) {
            clearBut = new JLabel(
                    IconFont.getIcon("fa-times-circle"), JLabel.CENTER);
            clearBut.setPreferredSize(new Dimension(20, 20));

            clearBut.setFont(IconFont.ICON_FONT.deriveFont(14f));
            clearBut.setToolTipText("消除");
            clearBut.setForeground(Color.gray);
        }
        return clearBut;
    }

    public JPanel getTitlePanel() {
        if (titlePanel == null) {
            titlePanel = new JPanel(new BorderLayout(7, 0));
            titlePanel.setOpaque(false);
            titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
            if (StringUtils.isNoneBlank(getIconfont()) && StringUtils.isNoneBlank(getTitleText())) {
                titlePanel.add(getIconLable(), BorderLayout.WEST);
                titlePanel.add(getTitleLable(), BorderLayout.CENTER);
            } else if (StringUtils.isNoneBlank(getIconfont()) && !StringUtils.isNoneBlank(getTitleText())) {
                titlePanel.add(getIconLable(), BorderLayout.CENTER);
            } else if (!StringUtils.isNoneBlank(getIconfont()) && StringUtils.isNoneBlank(getTitleText())) {
                titlePanel.add(getTitleLable(), BorderLayout.CENTER);
            }


        }
        return titlePanel;
    }

    public JLabel getWranLable() {
        if (wranLable == null) {
            wranLable = new JLabel(getWranText(), JLabel.RIGHT);
            wranLable.setFont(textFont.deriveFont(12f));
            wranLable.setForeground(errorColor);
            wranLable.setVisible(false);
        }
        return wranLable;
    }

    public JLabel getIconLable() {
        if (iconLable == null) {
            iconLable = new JLabel();
            iconLable.setFont(IconFont.ICON_FONT);
            iconLable.setText(IconFont.getIcon(getIconfont()));

        }
        return iconLable;
    }

    public JLabel getTitleLable() {
        if (titleLable == null) {
            titleLable = new JLabel();
            titleLable.setText(getTitleText());

        }
        return titleLable;
    }


    public void setTextFiledBorder(Border textFiledBorder) {
        this.textFiledBorder = textFiledBorder;
        setBorder(textFiledBorder);
    }

    public PulsatingBorder getPulsatingBorder() {
        return pulsatingBorder;
    }

    public String getIconfont() {
        return iconfont;
    }


    public String getWranText() {
        return wranText;
    }

    public void setWranText(String wranText) {
        this.wranText = wranText;
    }


    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }


    public void setTextFont(Font textFont) {
        this.textFont = textFont;
        initFontColor();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getTextField().setEnabled(enabled);
        getTitleLable().setEnabled(enabled);
        getIconLable().setEnabled(enabled);

    }

    public boolean isRegexcheck() {
        regexcheck = RegExpValidatorUtils.match(getRegex(), getTextField().getText());
        return regexcheck;
    }


}
