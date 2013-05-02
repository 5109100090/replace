from django.db import models
from authenticate.models import User
from place.models import Place

class Review(models.Model):
    reviewId = models.AutoField(primary_key=True)
    reviewUser = models.ForeignKey(User)
    reviewPlace = models.ForeignKey(Place)
    reviewPoint = models.DecimalField(decimal_places=0, max_digits=10)