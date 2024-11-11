package org.meliapp.backend.exception.apc

class BookmarkNotFoundException(id: Long) : BookmarkException("Bookmark with id $id not found")