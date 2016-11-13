from django.db import models

class LocationNote(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    title = models.CharField(max_length=100, blank=True, default='')
    description = models.TextField()
    latitude = models.FloatField()
    longitude = models.FloatField()
    upvotes = models.IntegerField()