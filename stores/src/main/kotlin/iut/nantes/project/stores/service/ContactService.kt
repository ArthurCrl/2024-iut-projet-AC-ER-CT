package iut.nantes.project.stores.service

import iut.nantes.project.stores.repository.ContactEntity
import iut.nantes.project.stores.repository.ContactRepository
import org.springframework.stereotype.Service

@Service
class ContactService(private val contactRepository: ContactRepository) {

    fun createContact(contact: ContactEntity): ContactEntity {
        return contactRepository.save(contact)
    }

    fun getAllContacts(city: String?): List<ContactEntity> {
        return if (city.isNullOrEmpty()) {
            contactRepository.findAll()
        } else {
            contactRepository.findByAddressCity(city)
        }
    }

    fun getContactById(id: Long): ContactEntity {
        return contactRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("Contact non trouvé pour l'id $id")
        }
    }

    fun updateContact(id: Long, updatedContact: ContactEntity): ContactEntity {
        val existingContact = getContactById(id)

        // Validation : ne pas permettre la modification simultanée de l'email et du téléphone
        if (existingContact.email != updatedContact.email && existingContact.phone != updatedContact.phone) {
            throw IllegalArgumentException("Vous ne pouvez pas changer à la fois l'email et le téléphone.")
        }

        val contactToSave = existingContact.copy(
            email = updatedContact.email,
            phone = updatedContact.phone,
            address = updatedContact.address
        )
        return contactRepository.save(contactToSave)
    }

    fun deleteContact(id: Long, hasLinkedStore: Boolean) {
        if (hasLinkedStore) {
            throw IllegalStateException("Le contact est lié à un magasin, suppression impossible.")
        }

        if (!contactRepository.existsById(id)) {
            throw IllegalArgumentException("Contact non trouvé pour l'id $id")
        }
        contactRepository.deleteById(id)
    }
}
