package com.amca.android.replace.model;

public class Review {
	private Integer reviewId;
	private User reviewUser;
	private Place reviewPlace;
	private Integer reviewPointPrice;
	private Integer reviewPointService;
	private Integer reviewPointLocation;
	private Integer reviewPointCondition;
	private Integer reviewPointComfort;
	private Double averagePoint;
	private String reviewText;
	private Double similarity;
	private Double newSimilarity;

	public Integer getReviewId() {
		return reviewId;
	}

	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}

	public User getReviewUser() {
		return reviewUser;
	}

	public void setReviewUser(User reviewUser) {
		this.reviewUser = reviewUser;
	}

	public Place getReviewPlace() {
		return reviewPlace;
	}

	public void setReviewPlace(Place reviewPlace) {
		this.reviewPlace = reviewPlace;
	}

	public Integer getReviewPointPrice() {
		return reviewPointPrice;
	}

	public void setReviewPointPrice(Integer reviewPointPrice) {
		this.reviewPointPrice = reviewPointPrice;
	}

	public Integer getReviewPointService() {
		return reviewPointService;
	}

	public void setReviewPointService(Integer reviewPointService) {
		this.reviewPointService = reviewPointService;
	}

	public Integer getReviewPointLocation() {
		return reviewPointLocation;
	}

	public void setReviewPointLocation(Integer reviewPointLocation) {
		this.reviewPointLocation = reviewPointLocation;
	}

	public Integer getReviewPointCondition() {
		return reviewPointCondition;
	}

	public void setReviewPointCondition(Integer reviewPointCondition) {
		this.reviewPointCondition = reviewPointCondition;
	}

	public Integer getReviewPointComfort() {
		return reviewPointComfort;
	}

	public void setReviewPointComfort(Integer reviewPointComfort) {
		this.reviewPointComfort = reviewPointComfort;
	}

	public Double getAveragePoint() {
		return averagePoint;
	}

	public void setAveragePoint(Double averagePoint) {
		this.averagePoint = averagePoint;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public Double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

	public Double getNewSimilarity() {
		return newSimilarity;
	}

	public void setNewSimilarity(Double newSimilarity) {
		this.newSimilarity = newSimilarity;
	}

}
