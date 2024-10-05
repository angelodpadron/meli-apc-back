package org.meliapp.backend.exception.apc

class UserAlreadyRegisteredException(email: String) : RuntimeException("User already registered: $email")
