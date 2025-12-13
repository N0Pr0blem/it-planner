package com.example.planner.domain.exception

/**
 * Base class for domain layer exceptions
 */
open class DomainException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

/**
 * Exception thrown when validation fails
 */
class ValidationException(message: String? = null) : DomainException(message)

/**
 * Exception thrown when entity is not found
 */
class EntityNotFoundException(entityName: String, id: Long) : DomainException("$entityName with id $id not found")

/**
 * Exception thrown when operation is not allowed
 */
class OperationNotAllowedException(message: String? = null) : DomainException(message)

/**
 * Exception thrown when network operation fails
 */
class NetworkException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)

/**
 * Exception thrown when authentication fails
 */
class AuthenticationException(message: String? = null) : DomainException(message)

/**
 * Exception thrown when authorization fails
 */
class AuthorizationException(message: String? = null) : DomainException(message)
