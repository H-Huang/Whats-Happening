from django.contrib.auth.models import User, Group
from RestFramework.models import LocationNote, Comment

from rest_framework import viewsets

from RestFramework.serializers import UserSerializer, GroupSerializer, LocationNoteSerializer, CommentSerializer

from django.http import Http404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer


class GroupViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    queryset = Group.objects.all()
    serializer_class = GroupSerializer

class LocationNoteList(APIView):
    """
    List all LocationNotes, or create a new LocationNote.
    """
    def get(self, request, format=None):
        location_notes = LocationNote.objects.all()
        serializer = LocationNoteSerializer(location_notes, context={'request': request}, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = LocationNoteSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class LocationNoteDetail(APIView):
    """
    Retrieve, update or delete a LocationNote instance.
    """
    def get_object(self, pk):
        try:
            return LocationNote.objects.get(pk=pk)
        except LocationNote.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        location_note = self.get_object(pk)
        serializer = LocationNoteSerializer(location_note, context={'request': request})
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        location_note = self.get_object(pk)
        serializer = LocationNoteSerializer(location_note, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        location_note = self.get_object(pk)
        location_note.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class CommentList(APIView):
    """
    List all LocationNotes, or create a new LocationNote.
    """
    def get(self, request, format=None):
        comments = Comment.objects.all()
        serializer = CommentSerializer(comments, context={'request': request}, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = CommentSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
