package com.lw.swing.ui;

import javax.swing.*;
import java.awt.*;

public class ReflectionRepaintManager extends RepaintManager {
		@Override
		public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
			Rectangle dirtyRegion = getDirtyRegion(c);

			int lastDeltaX = c.getX();
			int lastDeltaY = c.getY();

			Container parent = c.getParent();

			Container layeredPane = null;

			while (parent instanceof JComponent) {
				if (!parent.isVisible()) {
					return;
				}

				if (parent instanceof WIconButton) {
					x += lastDeltaX;
					y += lastDeltaY;

					lastDeltaX = lastDeltaY = 0;

					c = (JComponent) parent;
				}

				if (parent instanceof JLayeredPane) {

					layeredPane = parent;
				}

				lastDeltaX += parent.getX();
				lastDeltaY += parent.getY();

				parent = parent.getParent();
			}

			super.addDirtyRegion(c, x, y, w, h);

			if (layeredPane != null) {

				super.addDirtyRegion((JComponent) layeredPane, 0, 0, layeredPane.getWidth(), layeredPane.getHeight());
			}
		}
	}