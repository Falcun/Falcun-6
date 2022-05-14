package net.mattbenson.network.common;

public class Reward {
	private String name;
	private RewardRating rating;
	
	public Reward(String name, RewardRating rating) {
		this.name = name;
		this.rating = rating;
	}
	
	public String getName() {
		return name;
	}
	
	public RewardRating getRating() {
		return rating;
	}
}
