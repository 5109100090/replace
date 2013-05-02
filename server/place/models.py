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
                ) AS placeDistance \
            FROM place_place p \
            WHERE placeType_id = %s \
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
    objects = PlaceManager()