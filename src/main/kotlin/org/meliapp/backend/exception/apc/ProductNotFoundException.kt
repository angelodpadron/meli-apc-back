package org.meliapp.backend.exception.apc

class ProductNotFoundException(id: String) : RuntimeException("Product with id $id not found") {
}