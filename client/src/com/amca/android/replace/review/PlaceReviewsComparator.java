package com.amca.android.replace.review;

import java.util.Comparator;

import com.amca.android.replace.model.Review;

public class PlaceReviewsComparator implements Comparator<Review> {
	private int key = 0;

	public PlaceReviewsComparator(int key) {
		this.key = key;
	}

	public int compare(Review first, Review second) {
		Double n1 = 0.0, n2 = 0.0;
		if(key == 10){
			n1 = (double) first.getAveragePoint();
			n2 = (double) second.getAveragePoint();
		}else if(key == 20){
			n1 = first.getSimilarity();
			n2 = second.getSimilarity();
		}else if(key == 30){
			n1 = first.getNewSimilarity();
			n2 = second.getNewSimilarity();
		}

		if(n1 < n2) return 1;
		else if(n1 > n2) return -1;
		else return 0;
	}
}
