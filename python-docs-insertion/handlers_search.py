import webapp2
from google.appengine.api import search
from html_templates import templates

from misc.arabian_nights import full_text
import logging
from datetime import datetime



SHARED_INDEX = 'shared_index'

def clean_shared_index():
  doc_index = search.Index(name=SHARED_INDEX)

  while True:
      # Get a list of documents populating only the doc_id field and extract the ids.
      document_ids = [document.doc_id
                      for document in doc_index.get_range(ids_only=True)]
      if not document_ids:
          break
      # Delete the documents for the given ids from the Index.
      doc_index.delete(document_ids)

def insert_arabian_nights():

  for i in xrange(200):
    init = 3800*i
    end = init + 3800
    logging.info('inserting arabian_nights %d:'  %init)
    document = search.Document(
        # Setting the doc_id is optional. If omitted, the search service
        # will create an identifier.
        fields=[
            search.TextField(name='content', value=full_text[init:end]),
            search.TextField(name='email', value='sellbytel-marcialr@premium-cloud-support.com'),
            search.TextField(name='domain', value='gmail.com'),
            search.DateField(name='published', value=datetime.now()),
            search.NumberField(name='rating', value=66),
        ])
    try:
        index = search.Index(name=SHARED_INDEX)
        index.put(document)
        logging.info('.... %d: OK!!'  %init)
    except search.Error:
        logging.exception('insert_arabian_nights failed')



class InsertArabianNights(webapp2.RequestHandler):

    def get(self):
        insert_arabian_nights()

        html = templates.BASE % ("200 docs x 3800chars   INSERTED!!")
        return webapp2.Response(body=html, status=200)


class ClearArabianNights(webapp2.RequestHandler):

    def get(self):
        clean_shared_index()

        html = templates.BASE % ("DONE Clearing!!")
        return webapp2.Response(body=html, status=200)
