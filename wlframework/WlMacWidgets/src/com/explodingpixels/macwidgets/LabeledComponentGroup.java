package com.explodingpixels.macwidgets;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a group of components and provides a label underneath those components. The added
 * components will be placed side by side, with no spacing in between them, like this:
 * <br><br>
 * <img src="../../../../graphics/LabeledComponentGroup-anatomy.png">
 * <br><br>
 * Here are a couple more practical applications of {@code LabledComponentGroup}:
 * <br><br>
 * <img src="../../../../graphics/LabeledComponentGroup-view.png">&nbsp;&nbsp;&nbsp;&nbsp;<img src="../../../../graphics/LabeledComponentGroup-search.png">
 * <br><br>
 * Here's how to create a {@code LabeledComponentGroup} with two buttons:
 * <pre>
 * JToggleButton leftButton = new JToggleButton("Left Button");
 * leftButton.putClientProperty("JButton.buttonType", "segmentedTextured");
 * leftButton.putClientProperty("JButton.segmentPosition", "first");
 * <p/>
 * JToggleButton rightButton = new JToggleButton("Right Button");
 * rightButton.putClientProperty("JButton.buttonType", "segmentedTextured");
 * rightButton.putClientProperty("JButton.segmentPosition", "last");
 * <p/>
 * LabeledComponentGroup group = new LabeledComponentGroup("Group", leftButton, rightButton);
 * </pre>
 */
public class LabeledComponentGroup {

    private JComponent fComponent;

    /**
     * Creates a labeled component group using the given label and components.
     *
     * @param labelString the label of the group.
     * @param components  the components in the group.
     */
    public LabeledComponentGroup(String labelString, JComponent... components) {
        this(labelString, Arrays.asList(components));
    }

    /**
     * Creates a labeled component group using the given label and components.
     *
     * @param labelString the label of the group.
     * @param components  the components in the group.
     */
    public LabeledComponentGroup(String labelString, List<JComponent> components) {
        JComponent componentToAdd;
        if (components.size() == 1) {
            componentToAdd = components.get(0);
        } else {
            componentToAdd = new JPanel(new FlowLayout(0, 0, FlowLayout.CENTER));
            componentToAdd.setOpaque(false);
            for (JComponent component : components) {
                componentToAdd.add(component);
            }
        }

        // definte the FormLayout columns and rows.
        FormLayout layout = new FormLayout("p", "fill:p:grow, p");
        // create the cell constraints to use in the layout.
        CellConstraints cc = new CellConstraints();
        // create the builder with our panel as the component to be filled.
        PanelBuilder builder = new PanelBuilder(layout, new JPanel());

        builder.add(componentToAdd, cc.xy(1, 1, "center, center"));
        builder.add(createLabel(labelString), cc.xy(1, 2, "center, top"));

        fComponent = builder.getPanel();
        fComponent.setOpaque(false);
    }

    public JComponent getComponent() {
        return fComponent;
    }

    private JLabel createLabel(String labelString) {
        JLabel label = MacWidgetFactory.makeEmphasizedLabel(new JLabel(labelString));
        label.setFont(MacFontUtils.TOOLBAR_LABEL_FONT);
        return label;
    }
}
