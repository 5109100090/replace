from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'replace.views.home', name='home'),
    # url(r'^replace/', include('replace.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
    url(r'^authenticate/', include('authenticate.urls')),
	url(r'^type/', include('type.urls')),
    url(r'^place/', include('place.urls')),
    url(r'^review/', include('review.urls')),
    url(r'^generator/', include('generator.urls')),
)
