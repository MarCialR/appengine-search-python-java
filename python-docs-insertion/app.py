import webapp2

from html_templates import templates

import handlers_internal as _internal
import handlers_search as _search

routes = [
    ('/ison', _internal.IsonPAge),

    ('/search/insert', _search.InsertArabianNights),
    ('/search/clear', _search.ClearArabianNights),
    
]
templates.save_routes(routes)
webapp = webapp2.WSGIApplication(routes, debug=True)


