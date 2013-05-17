from django.conf.urls import patterns, url

from similarity import views

urlpatterns = patterns('',
    url(r'^process/', views.process, name='process'),
)