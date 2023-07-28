package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import java.util.UUID

interface PersonService {
  fun getPeople(): Iterable<PersonDTO>

  fun addPerson(newPerson: NewPerson): PersonDTO

  fun updatePerson(id: UUID, editPerson: EditPerson): PersonDTO
}
