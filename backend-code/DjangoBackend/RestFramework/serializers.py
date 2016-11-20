from django.contrib.auth.models import User, Group

from RestFramework.models import LocationNote

from rest_framework import serializers


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('url', 'username', 'email', 'groups')


class GroupSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Group
        fields = ('url', 'name')

class LocationNoteSerializer(serializers.HyperlinkedModelSerializer):

    class Meta:
        model = LocationNote
        fields = ('created', 'title', 'description', 'latitude', 'longitude', 'upvotes', 'user')
