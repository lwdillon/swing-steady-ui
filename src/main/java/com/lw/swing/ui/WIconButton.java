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

/**
 * Created by liwen on 2017/5/6.
 */
public class WIconButton extends JButton {


    @InjectedResource
    private Color buttonForeground, buttonBackground;
    @InjectedResource
    private Font buttonFont;


    /**
     * 图标
     */
    private JLabel iconLabel;

    /**
     * text
     */
    private JLabel textLabel;

    /**
     * 直角&圆角 true为直角，flase为圆角
     */
    private boolean corner = true;

    private float alpha = 0f;

    private Animator animationStart;

    /**
     * 是否填充
     */
    private boolean fill = true;

    /**
     * 是否绘制边框
     */
    private boolean drawBorder = true;

    public WIconButton(String text) {
        this(text, null);
    }

    public WIconButton(String text, String icontext) {

        super(text);
        ResourceInjector.get().inject(this);
        initSwing();
        initListener();

        if (StringUtils.isNotEmpty(text) && StringUtils.isNotEmpty(icontext)) {
            this.add(getTextLabel(), BorderLayout.CENTER);
            this.add(getIconLabel(), BorderLayout.WEST);
        } else {
            if (StringUtils.isNotEmpty(text)) {
                this.add(getTextLabel(), BorderLayout.CENTER);
                getTextLabel().setHorizontalAlignment(JLabel.CENTER);
            }
            if (StringUtils.isNotEmpty(icontext)) {
                this.add(getIconLabel(), BorderLayout.CENTER);
                getIconLabel().setHorizontalAlignment(JLabel.CENTER);

            }
        }
        getIconLabel().setText(IconFont.getIcon(icontext));
        getTextLabel().setText(text);

    }


    private void initSwing() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout(5, 0));
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setForeground(buttonForeground);
        this.setBackground(buttonBackground);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setFont(buttonFont);
        this.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
    }


    private void initListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    getAnimationStart().restart();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    getAnimationStart().restartReverse();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
        });
    }

    @Override
    public void setFont(Font font) {
        if (font == null) {
            return;
        }
        super.setFont(font);
        getTextLabel().setFont(font);
        getIconLabel().setFont(IconFont.ICON_FONT.deriveFont(font.getSize2D()));
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        getIconLabel().setForeground(fg);
        getTextLabel().setForeground(fg);
    }

    public JLabel getIconLabel() {
        if (iconLabel == null) {
            iconLabel = new JLabel("", JLabel.CENTER);
            iconLabel.setFont(IconFont.ICON_FONT);
        }
        return iconLabel;
    }

    public JLabel getTextLabel() {
        if (textLabel == null) {
            textLabel = new JLabel();
            textLabel.setFont(buttonFont);
        }
        return textLabel;
    }

    public void setIconFont(String iconFont) {
        getIconLabel().setText(IconFont.getIcon(iconFont));
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }


    public boolean isCorner() {
        return corner;
    }

    public void setCorner(boolean corner) {
        this.corner = corner;
    }

    public boolean isDrawBorder() {
        return drawBorder;
    }

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
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
        if (isFill()) {//填充
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
            if (isCorner()) {
                g2.fillRect(x, y, w, h);
            } else {
                g2.fillRoundRect(x, y, w, h, 5, 5);
            }
        }

        if (alpha > 0.0000001f) {

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER).derive(alpha));
            if (!isCorner()) {
                g2.fillRoundRect(x, y, w, h, 5, 5);
            } else {
                g2.fillRect(x, y, w, h);
            }
        }

        if (isDrawBorder()) {//绘制边框
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER).derive(1f));
            if (isCorner()) {
                g2.drawRect(x, y, w - 1, h - 1);
            } else {
                g2.drawRoundRect(x, y, w - 1, h - 1, 5, 5);
            }

        }

        g2.dispose();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (b) {
            setForeground(buttonForeground);
        } else {
            setForeground(Color.GRAY);
        }
    }

    @Override
    public String getText() {
        return getTextLabel().getText();
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        getTextLabel().setText(text);
    }

    private Animator getAnimationStart() {
        if (animationStart == null) {
            animationStart = new Animator.Builder().setDuration(300, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.5, .5)).build();
            animationStart.addTarget(PropertySetter.getTarget(WIconButton.this, "alpha", 0f, .8f));
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


    @Override
    public String getName() {
        return super.getName();
    }
}
