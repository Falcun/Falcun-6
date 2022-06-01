package net.mattbenson.network.common;

import java.awt.Color;

public enum RewardRating {
	COMMON(0, new Color(253, 255, 254)),
	UNCOMMON(1, new Color(77, 105, 205)),
	RARE(2, new Color(137, 71, 255)),
	CLASSIFIED(3, new Color(212, 43, 230)),
	COVERT(4, new Color(235, 75, 75)),
	EXCEEDINGLY_RARE(5, new Color(202, 171, 5));
	
	int rating;
	Color color;
	RewardRating(int rating, Color color) {
		if(rating > 5) {
			rating = 5;
		} else if(rating < 0) {
			rating = 0;
		}
		
		this.rating = rating;
		this.color = color;
	}
	
	public int getRating() {
		return rating;
	}
	
	public Color getColor() {
		return color;
	}
}
