package com.lw.swing.ui;

import com.lw.swing.utils.IconFont;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class WItemButoon extends JRadioButton {
    /**
     * 是否填充
     */
    private boolean fill = true;
    /**
     * 是否绘制边框
     */
    private boolean drawBorder = true;
    private float alpha = 0f;
    private Animator animationStart;

    private JLabel iconLabel;
    private JLabel textLabel;
    private String iconFont;


    @InjectedResource
    private Color background = new Color(1f, 1f, 1f, .2f);
    @InjectedResource
    private Color foreground = new Color(0xffffff);

    public WItemButoon(String text, String iconFont) {
        super(text);
        ResourceInjector.get().inject(this);
        this.iconFont = iconFont;
        initSwing();
        initListener();

    }

    public WItemButoon(String text) {
        this(text, "");
    }

    private void initSwing() {
        this.setLayout(new BorderLayout(5, 0));
        this.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        if (StringUtils.isNotEmpty(getText()) && StringUtils.isNotEmpty(getIconFont())) {
            this.add(getTextLabel(), BorderLayout.CENTER);
            this.add(getIconLabel(), BorderLayout.WEST);
        } else {
            if (StringUtils.isNotEmpty(getText())) {
                this.add(getTextLabel(), BorderLayout.CENTER);
                getTextLabel().setHorizontalAlignment(JLabel.CENTER);
            }
            if (StringUtils.isNotEmpty(getIconFont())) {
                this.add(getIconLabel(), BorderLayout.CENTER);
                getIconLabel().setHorizontalAlignment(JLabel.CENTER);

            }
        }
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setBackground(background);
        this.setForeground(foreground);

        getIconLabel().setText(IconFont.getIcon(getIconFont()));

    }


    private void initListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled() && !isSelected()) {
                    getAnimationStart().restart();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled() && !isSelected()) {
                    getAnimationStart().restartReverse();
                }
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int x = 0;
        int y = 0;

        g2.setColor(getBackground());
        if (isSelected()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER).derive(1f));
            g2.fillRect(x, y, w, h);
            alpha = 0f;
        } else {

            if (isFill()) {//填充
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER).derive(.5f));
                g2.fillRect(x, y, w, h);
            }

            if (alpha > 0.0000001f) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER).derive(alpha));
                g2.fillRect(x, y, w, h);
            }
            if (isDrawBorder()) {//绘制边框
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER).derive(1f));
                g2.drawRect(x, y, w - 1, h - 1);
            }
        }

        g2.dispose();
    }


    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (textLabel != null) {
            textLabel.setFont(font);
        }
        if (iconLabel != null) {
            iconLabel.setFont(IconFont.ICON_FONT.deriveFont(font.getSize2D() + 2f));

        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        getIconLabel().setForeground(fg);
        getTextLabel().setForeground(fg);
    }

    public JLabel getIconLabel() {
        if (iconLabel == null) {
            iconLabel = new JLabel(IconFont.getIcon(getIconFont()), JLabel.CENTER);
            iconLabel.setFont(IconFont.ICON_FONT);
        }
        return iconLabel;
    }

    public JLabel getTextLabel() {
        if (textLabel == null) {
            textLabel = new JLabel();
        }
        return textLabel;
    }



    @Override
    public void setText(String text) {
        super.setText(text);
        getTextLabel().setText(text);
    }

    public String getIconFont() {
        return iconFont;
    }

    public void setIconFont(String iconFont) {
        this.iconFont = iconFont;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public boolean isDrawBorder() {
        return drawBorder;
    }

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    private Animator getAnimationStart() {
        if (animationStart == null) {
            animationStart = new Animator.Builder().setDuration(300, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.5, .5)).build();
            animationStart.addTarget(PropertySetter.getTarget(WItemButoon.this, "alpha", 0f, 1f));
        }
        return animationStart;
    }


    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }
}
