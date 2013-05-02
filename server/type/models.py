from django.db import models

class Type(models.Model):
    typeId = models.AutoField(primary_key=True)
    typeName = models.CharField(max_length=200)