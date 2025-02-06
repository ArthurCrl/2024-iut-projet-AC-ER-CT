package iut.nantes.project.stores.exception

class ContactNotFoundException(message: String) : RuntimeException(message)
class InvalidContactDataException(message: String) : RuntimeException(message)

class ContactExistInMagasinExecption(message: String) : RuntimeException(message)
