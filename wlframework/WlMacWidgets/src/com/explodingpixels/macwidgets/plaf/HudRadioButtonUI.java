package com.explodingpixels.macwidgets.plaf;

import javax.swing.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Creates a Heads Up Display (HUD) style radio button, similar to that seen in various iApps
 * (e.g. iPhoto).
 * <br>
 * <img src="../../../../../graphics/HUDRadioButtonUI.png">
 */
public class HudRadioButtonUI extends BasicRadioButtonUI {

    @Override
    protected void installDefaults(final AbstractButton b) {
        super.installDefaults(b);

        HudPaintingUtils.initHudComponent(b);
        b.setIconTextGap((int) (HudPaintingUtils.FONT_SIZE / 2));

        icon = new DotIcon();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        HudPaintingUtils.updateGraphicsToPaintDisabledControlIfNecessary((Graphics2D) g, c);
        super.paint(g, c);
    }

    @Override
    protected void paintText(Graphics g, AbstractButton button, Rectangle textRect, String text) {
        FontMetrics fontMetrics = g.getFontMetrics(button.getFont());
        int mnemonicIndex = button.getDisplayedMnemonicIndex();

        g.setColor(button.getForeground());
        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemonicIndex,
                textRect.x + getTextShiftOffset(),
                textRect.y + fontMetrics.getAscent() + getTextShiftOffset());
    }

// Dot icon implementation. ///////////////////////////////////////////////////////////////////

    private static class DotIcon implements Icon {

        private final int RADIO_BUTTON_SIZE = 13;

        private final float DOT_DIAMETER = 4.25f;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            AbstractButton button = (AbstractButton) c;

            Graphics2D graphics = (Graphics2D) g.create();
            // translate the graphics context so that 0,0 is the top/left of the radio button. this
            // allows us to then delegate the painting to the HudPaintingUtils method, which assumes
            // 0,0.
            graphics.translate(x, y);
            HudPaintingUtils.paintHudControlBackground(graphics, button, RADIO_BUTTON_SIZE,
                    RADIO_BUTTON_SIZE, HudPaintingUtils.Roundedness.RADIO);

            drawDotIfNecessary(graphics, button.getModel());
            graphics.dispose();
        }

        private void drawDotIfNecessary(Graphics2D graphics, ButtonModel model) {
            if (model.isSelected()) {
                drawSelected(graphics, model);
            }
        }

        private void drawSelected(Graphics2D graphics, ButtonModel model) {
            Color color = model.isPressed() ?
                    HudPaintingUtils.PRESSED_MARK_COLOR : HudPaintingUtils.FONT_COLOR;
            graphics.setColor(color);
            float offset = (getIconWidth() - DOT_DIAMETER) / 2.0f;
            Ellipse2D dot = new Ellipse2D.Float(offset, offset, DOT_DIAMETER, DOT_DIAMETER);
            graphics.fill(dot);
        }

        public int getIconWidth() {
            return RADIO_BUTTON_SIZE;
        }

        public int getIconHeight() {
            return RADIO_BUTTON_SIZE;
        }


    }
}
