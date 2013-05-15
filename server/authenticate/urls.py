from django.conf.urls import patterns, url

from authenticate import views

urlpatterns = patterns('',
    url(r'^$', views.index, name='index'),
	url(r'^login/', views.login, name='login'),
    url(r'^users/', views.users, name='users'),
    url(r'^detail/', views.detail, name='detail'),
    url(r'^update/', views.update, name='update'),
    url(r'^register/', views.register, name='register'),
)