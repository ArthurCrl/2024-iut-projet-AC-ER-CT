package iut.nantes.project.stores.config

import iut.nantes.project.stores.exception.ContactExistInMagasinExecption
import iut.nantes.project.stores.exception.ContactIdInexistantException
import iut.nantes.project.stores.exception.ContactNotFoundException
import iut.nantes.project.stores.exception.InvalidContactDataException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }
    @ExceptionHandler(InvalidContactDataException::class)
    fun handleInvalidContactDataException(ex: InvalidContactDataException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(ContactNotFoundException::class)
    fun handleContactNotFoundException(ex: ContactNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(ContactExistInMagasinExecption::class)
    fun handleContactExistInMagasinExecption(ex: ContactExistInMagasinExecption): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données sont invalides")
    }

    @ExceptionHandler(ContactIdInexistantException::class)
    fun handleContactIdInexistantException(ex: ContactIdInexistantException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }
}

