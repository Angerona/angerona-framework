package com.explodingpixels.macwidgets.plaf;

import javax.swing.*;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.*;

/**
 * Creates a Heads Up Display (HUD) style check box button, similar to that seen in various iApps
 * (e.g. iPhoto).
 * <br>
 * <img src="../../../../../graphics/HUDCheckBoxUI.png">
 */
public class HudCheckBoxUI extends BasicCheckBoxUI {

    @Override
    protected void installDefaults(final AbstractButton b) {
        super.installDefaults(b);

        HudPaintingUtils.initHudComponent(b);
        b.setIconTextGap((int) (HudPaintingUtils.FONT_SIZE / 2));

        icon = new CheckIcon();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        HudPaintingUtils.updateGraphicsToPaintDisabledControlIfNecessary((Graphics2D) g, c);
        super.paint(g, c);
    }

    @Override
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        AbstractButton button = (AbstractButton) c;
        FontMetrics fontMetrics = g.getFontMetrics(button.getFont());
        int mnemonicIndex = button.getDisplayedMnemonicIndex();

        g.setColor(button.getForeground());
        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemonicIndex,
                textRect.x + getTextShiftOffset(),
                textRect.y + fontMetrics.getAscent() + getTextShiftOffset());
    }

    // Check icon implementation. /////////////////////////////////////////////////////////////////

    private static class CheckIcon implements Icon {

        private final int CHECK_BOX_SIZE = 12;

        public void paintIcon(Component c, Graphics g, int x, int y) {

            AbstractButton button = (AbstractButton) c;

            Graphics2D graphics = (Graphics2D) g.create();
            // translate the graphics context so that 0,0 is the top/left of the check box. this
            // allows us to then delegate the painting to the HudPaintingUtils method, which assumes
            // 0,0.
            graphics.translate(x, y);
            HudPaintingUtils.paintHudControlBackground(graphics, button, CHECK_BOX_SIZE,
                    CHECK_BOX_SIZE, HudPaintingUtils.Roundedness.CHECK_BOX);
            drawCheckMarkIfNecessary(graphics, button.getModel());
            graphics.dispose();
        }

        /**
         * Draws the check mark if {@link ButtonModel#isSelected} returns true.
         */
        private void drawCheckMarkIfNecessary(Graphics2D graphics, ButtonModel model) {
            if (model.isSelected()) {
                drawCheckMark(graphics, model);
            }
        }

        /**
         * Draws the check in the check box using the appropriate color based on the
         * {@link ButtonModel#isPressed} state. Note that part of the check will be drawn outside
         * it's bounds. Because this icon is actually being drawn inside a larger component (a
         * {@link javax.swing.JCheckBox}), this shouldn't be an issue.
         */
        private void drawCheckMark(Graphics2D graphics, ButtonModel model) {
            int x1 = CHECK_BOX_SIZE / 4;
            int y1 = CHECK_BOX_SIZE / 3;
            int x2 = x1 + CHECK_BOX_SIZE / 6;
            int y2 = y1 + CHECK_BOX_SIZE / 4;
            int x3 = CHECK_BOX_SIZE - 2;
            int y3 = -1;

            Color color = model.isPressed() ?
                    HudPaintingUtils.PRESSED_MARK_COLOR : HudPaintingUtils.FONT_COLOR;

            graphics.setStroke(new BasicStroke(1.65f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
            graphics.setColor(color);
            graphics.drawLine(x1, y1, x2, y2);
            graphics.drawLine(x2, y2, x3, y3);
        }

        public int getIconWidth() {
            return CHECK_BOX_SIZE;
        }

        public int getIconHeight() {
            return CHECK_BOX_SIZE;
        }

    }

}
