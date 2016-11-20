from django.db import models
from django.contrib.auth.models import User

class LocationNote(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    title = models.CharField(max_length=100, blank=True, default='')
    description = models.TextField()
    latitude = models.FloatField()
    longitude = models.FloatField()
    upvotes = models.IntegerField()
    user = models.ForeignKey(User, default=None, null=True, blank=True)

    def __str__(self):
        return '%s' % (self.title)
