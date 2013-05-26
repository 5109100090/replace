from django.db import models

class User(models.Model):
    userId = models.AutoField(primary_key=True)
    userName = models.CharField(max_length=200)
    userAlias = models.CharField(max_length=200)
    userPassword = models.CharField(max_length=32)
    userFoods = models.TextField()
    userDrinks = models.TextField()
    userBooks = models.TextField()
    userMovies = models.TextField()
    userGender = models.CharField(max_length=1)
    userOccupation = models.CharField(max_length=50)
    userDOB = models.DateField()