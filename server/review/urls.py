from django.conf.urls import patterns, url

from review import views

urlpatterns = patterns('',
    url(r'^$', views.index, name='index'),
    url(r'^listReviews/', views.listReviews, name='listReviews'),
)