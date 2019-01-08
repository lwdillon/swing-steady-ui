package com.lw.swing.ui;

import com.lw.swing.utils.GraphicsUtil;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @description: 欢迎动画
 * @className: IntroductionPanel
 * @author: liwen
 * @date: 2018/12/17 09:27
 */
public class IntroductionPanel extends JComponent {

    @InjectedResource
    private LinearGradientPaint backgroundGradient;
    @InjectedResource
    private BufferedImage logo;

    private final JComponent clip;
    private float fade = 0.0f;
    private float fadeOut = 0.0f;
    private BufferedImage gradientImage;

    IntroductionPanel(final JComponent clip) {
        ResourceInjector.get().inject(this);
        this.clip = clip;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            startFadeIn();
        }
    }

    private void startFadeIn() {

        Animator timer = new Animator.Builder().setStartDelay(1,SECONDS).setDuration(800, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.7, .3)).build();
        timer.addTarget(PropertySetter.getTarget(this, "fade", 0.0f, 1.0f));
        timer.addTarget(new TimingTargetAdapter() {
            @Override
            public void end(Animator source) {
                startFadeOut();
            }

        });
        timer.start();
    }

    private void startFadeOut() {

        Animator timer = new Animator.Builder().setDuration(800, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.7, .3)).build();
        timer.addTarget(PropertySetter.getTarget(this, "fadeOut", 0.0f, 1f));
        timer.addTarget(new TimingTargetAdapter() {
            @Override
            public void end(Animator source) {
                TransitionManager.showLoginOverlay();
            }

        });
        timer.start();

    }

    public void setFade(float fade) {
        this.fade = fade;
        repaint();
    }

    public float getFade() {
        return fade;
    }

    public void setFadeOut(float fadeOut) {
        this.fadeOut = fadeOut;
        repaint();
    }

    public float getFadeOut() {
        return fadeOut;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        setupGraphics(g2);
        paintBackground(g2);
        paintLogo(g2);
        paintForeground(g2);
    }

    private void paintForeground(Graphics2D g2) {
        if (fadeOut > 0.0f) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setColor(new Color(1, 1f, 1f, fadeOut));
            Rectangle rect = g2.getClipBounds().intersection(getClipBounds());
            g2.fillRect(rect.x, rect.y, rect.width, rect.height);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }

    private static void setupGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void paintLogo(Graphics2D g2) {
        int x = (getWidth() - logo.getWidth()) / 2;
        int y = (getHeight() - logo.getHeight()) / 2;

        Composite composite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fade));
        g2.drawImage(logo, x, y, null);
        g2.setComposite(composite);
    }

    private Rectangle getClipBounds() {
        Point point = clip.getLocation();
        point = SwingUtilities.convertPoint(clip, point, this);
        return new Rectangle(point.x, point.y, clip.getWidth(), clip.getHeight());
    }

    private void paintBackground(Graphics2D g2) {
        Rectangle rect = getClipBounds();
        if (gradientImage == null) {
            gradientImage = GraphicsUtil.createCompatibleImage(rect.width, rect.height);
            Graphics2D g2d = (Graphics2D) gradientImage.getGraphics();
            g2d.setPaint(backgroundGradient);
            g2d.fillRect(0, 0, rect.width, rect.height);
            g2d.dispose();
        }

        g2.drawImage(gradientImage, rect.x, rect.y, null);
    }
}
