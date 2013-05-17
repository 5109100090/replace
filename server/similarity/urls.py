from django.conf.urls import patterns, url

from similarity import views

urlpatterns = patterns('',
    url(r'^processAll/', views.processAll, name='processAll'),
    url(r'^processUser/', views.processUser, name='processUser'),
)