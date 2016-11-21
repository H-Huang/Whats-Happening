from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from RestFramework import views

urlpatterns = [
    url(r'^locationnotes/$', views.LocationNoteList.as_view()),
    url(r'^locationnotes/(?P<pk>[0-9]+)/$', views.LocationNoteDetail.as_view()),
    url(r'^comments/$', views.CommentList.as_view())
]

urlpatterns = format_suffix_patterns(urlpatterns)
