package com.lw.swing.ui.panel;

import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;
import org.jdesktop.swingx.JXPanel;

import java.awt.*;

/**
 * @description: 效果panel
 * @className: WBlurEffectPanel
 * @author: liwen
 * @date: 2018/12/24 14:33
 */
public class WBlurEffectPanel extends JXPanel {


    @InjectedResource
    private Color background = new Color(100, 100, 100, 87);


    public WBlurEffectPanel(LayoutManager layout) {
        super(layout);
        ResourceInjector.get().inject(this);
        setBackground(background);
    }

    public WBlurEffectPanel() {

        this(new BorderLayout());

    }


}
