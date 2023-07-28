package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*

interface PersonService {
  fun getPeople(): Iterable<PersonDTO>

  fun addPerson(newPerson: NewPerson): PersonDTO
}
