package com.explodingpixels.macwidgets;

import com.explodingpixels.data.Rating;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

public class ITunesRatingTableCellRenderer extends DefaultTableCellRenderer {

	//ADDED BY MT
	private static final long serialVersionUID = 1L;
	
    private Map<Rating, RatingComponent> fRatingsToRatingComponents =
            new HashMap<Rating, RatingComponent>();

    public ITunesRatingTableCellRenderer() {
        // iterate over each value in the rating enumeration and add a
        // corresponding rating component.
        for (Rating rating : Rating.values()) {
            fRatingsToRatingComponents.put(rating, new RatingComponent(rating));
        }

    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);

        Rating rating = (Rating) value;

        // grab the cached rating component for the corresponding rating.
        RatingComponent renderer = value == null
                ? fRatingsToRatingComponents.get(Rating.NO_RATING)
                : fRatingsToRatingComponents.get(rating);
        renderer.setFocused(table.hasFocus());
        renderer.setSelected(table.isRowSelected(row));

        renderer.getComponent().setBackground(getBackground());

        return renderer.getComponent();
    }

}
