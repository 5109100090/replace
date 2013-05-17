from django.db import models
from authenticate.models import User
from place.models import Place

class ReviewManager(models.Manager):
    def listReviews(self, user, place):
        query = "SELECT *, (reviewPoint*similarityValue) AS newSimilarityValue \
            FROM review_review r \
            RIGHT JOIN similarity_similarity s \
            ON (r.reviewUser_id = s.similarityUser1_id AND s.similarityUser2_id = %s) OR \
                (r.reviewUser_id = s.similarityUser2_id AND s.similarityUser1_id = %s) \
            WHERE reviewPlace_id = %s \
            ORDER BY newSimilarityValue DESC"
        
        return Review.objects.raw(query, [user.userId, user.userId, place.placeId])

class Review(models.Model):
    reviewId = models.AutoField(primary_key=True)
    reviewUser = models.ForeignKey(User)
    reviewPlace = models.ForeignKey(Place)
    reviewPoint = models.DecimalField(decimal_places=0, max_digits=10)
    objects = ReviewManager()