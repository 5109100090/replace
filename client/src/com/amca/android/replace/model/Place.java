package com.amca.android.replace.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Place implements Serializable {
	private Integer placeId;
	private String placeName;
	private String placeDesc;
	private String placeLat;
	private String placeLng;
	private String placeAddress;
	private Integer placeType;
	private Integer placeReviews;
	private Double placeDistance;
	private Double averagePoint;

	public Integer getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceDesc() {
		return placeDesc;
	}

	public void setPlaceDesc(String placeDesc) {
		this.placeDesc = placeDesc;
	}

	public String getPlaceLat() {
		return placeLat;
	}

	public void setPlaceLat(String placeLat) {
		this.placeLat = placeLat;
	}

	public String getPlaceLng() {
		return placeLng;
	}

	public void setPlaceLng(String placeLng) {
		this.placeLng = placeLng;
	}

	public String getPlaceAddress() {
		return placeAddress;
	}

	public void setPlaceAddress(String placeAddress) {
		this.placeAddress = placeAddress;
	}

	public Integer getPlaceType() {
		return placeType;
	}

	public void setPlaceType(Integer placeType) {
		this.placeType = placeType;
	}

	public Integer getPlaceReviews() {
		return placeReviews;
	}

	public void setPlaceReviews(Integer placeReviews) {
		this.placeReviews = placeReviews;
	}

	public Double getPlaceDistance() {
		return placeDistance;
	}

	public void setPlaceDistance(Double placeDistance) {
		this.placeDistance = placeDistance;
	}

	public Double getAveragePoint() {
		return averagePoint;
	}

	public void setAveragePoint(Double averagePoint) {
		this.averagePoint = averagePoint;
	}
}
