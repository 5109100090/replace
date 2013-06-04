from django.conf.urls import patterns, url

from place import views

urlpatterns = patterns('',
    url(r'^$', views.index, name='index'),
    url(r'^process/', views.process, name='process'),
)