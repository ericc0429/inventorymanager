package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.repos.PersonRepo
import java.util.Optional
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PersonServiceImpl(val db: PersonRepo) : PersonService {
  override fun getPeople(): Iterable<PersonDTO> = db.findAll().map { it.toView() }

  override fun addPerson(newPerson: NewPerson): PersonDTO =
      db.save(
              Person(
                  id = null,
                  name = newPerson.name,
                  debut = "unknown",
                  gender = GenderType.NONE,
                  albums = emptySet(),
                  assets = emptySet(),
                  birthday = "unknown",
                  group = emptySet(),
              )
          )
          .toView()

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
