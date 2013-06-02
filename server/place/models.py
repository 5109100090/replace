from django.db import models
from type.models import Type

class PlaceManager(models.Manager):
    def listInRange(self, placeLat, placeLng, placeType, range):
        query = "SELECT *, \
                ( \
                    6371000 * \
                    acos( \
                        cos( radians(%s) ) * \
                        cos( radians(placeLat) ) * \
                        cos( radians(placeLng) - radians(%s) ) + \
                        sin( radians(%s) ) * \
                        sin( radians(placeLat) ) \
                    ) \
                ) AS placeDistance, \
            COUNT(r.reviewId) as placeReviews, \
            (SUM(r.reviewPointPrice)/COUNT(r.reviewId)+SUM(r.reviewPointService)/COUNT(r.reviewId)+SUM(r.reviewPointLocation)/COUNT(r.reviewId)+SUM(r.reviewPointCondition)/COUNT(r.reviewId)+SUM(r.reviewPointComfort)/COUNT(r.reviewId))/5 AS averagePoint \
            FROM place_place p \
            LEFT JOIN review_review r \
            ON r.reviewPlace_id = p.placeId\
            WHERE placeType_id = %s \
            GROUP BY p.placeId \
            HAVING placeDistance < %s \
            ORDER BY placeDistance"
        
        return Place.objects.raw(query, [placeLat, placeLng, placeLat, placeType.typeId, range])

class Place(models.Model):
    placeId = models.AutoField(primary_key=True)
    placeName = models.CharField(max_length=200)
    placeDesc = models.TextField()
    placeLat = models.TextField()
    placeLng = models.TextField()
    placeType = models.ForeignKey(Type)
    placeAddress = models.TextField()
    objects = PlaceManager()