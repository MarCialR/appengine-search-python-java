import os

class _o(object):
  pass

_t = _o()

def _save_routes(routes):
  def _linkify(route):
      r = route[0] + str(route[1].__class__)
      return '<a href="%s">%s</a>' % (route[0], r)

  def _routes_html(routes):
      return "<br>".join([_linkify(route) for route in routes])
  _t.ROUTES = _routes_html(routes)

  _t.BASE = """<html>
    <head>
      <title>__TITLE__</title>
      <style>
        body {
          padding: 20px;
          font-family: arial, sans-serif;
          font-size: 14px;
        }
        pre {
          background: #F2F2F2;
          padding: 10px;
        }
      </style>
    </head>
    <body>
    %s
    </body>
  </html>"""


templates = _t
templates.save_routes = _save_routes