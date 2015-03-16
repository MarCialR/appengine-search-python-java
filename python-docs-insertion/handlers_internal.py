import urllib
import webapp2
from html_templates import templates


class IsonPAge(webapp2.RequestHandler):

    def get(self):
        self.response.write("OKKK!!")
