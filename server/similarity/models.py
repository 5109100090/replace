from django.db import models
from authenticate.models import User

class Similarity(models.Model):
    similarityId = models.AutoField(primary_key=True)
    similarityUser1 = models.ForeignKey(User, related_name='+')
    similarityUser2 = models.ForeignKey(User, related_name='+')
    similarityValue = models.FloatField()
