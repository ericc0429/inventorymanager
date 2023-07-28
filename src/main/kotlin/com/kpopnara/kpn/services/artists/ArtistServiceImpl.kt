package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.repos.ArtistRepo
import com.kpopnara.kpn.repos.GroupRepo
import com.kpopnara.kpn.repos.PersonRepo
import java.util.Optional
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional
class ArtistServiceImpl(
    val artistRepo: ArtistRepo<Artist>, 
    val groupRepo: ArtistRepo<Group>, 
    val personRepo: ArtistRepo<Person>
    ) : ArtistService, GroupService, PersonService {

    override fun getAll(): Iterable<Artist> {
        return artistRepo.findAll()
    }

    override fun getGroups(): Iterable<GroupDTO> {
        return groupRepo.findAll().map { it.toView() }
    }

    override fun getPeople(): Iterable<PersonDTO> {
        return personRepo.findAll().map { it.toView() }
    }

    override fun addGroup(newGroup: NewGroup): GroupDTO =
        groupRepo.save(
            Group(
                id = null,
                name = newGroup.name,
                debut = "unknown",
                gender = GenderType.NONE,
                albums = emptySet<Album>(),
                assets = emptySet<Asset>(),
                type = GroupType.NONE,
                members = emptySet<Artist>()
            )
        )
        .toView()

    override fun addPerson(newPerson: NewPerson): PersonDTO =
        personRepo.save(
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

    override fun updateGroup(id: UUID, editGroup: EditGroup) : GroupDTO {
        val group = groupRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        group.name = if (editGroup.name != null) editGroup.name else group.name
        group.debut = if (editGroup.debut != null) editGroup.debut else group.debut
        group.gender = if (editGroup.gender != null) editGroup.gender else group.gender
        group.type = if (editGroup.type != null) editGroup.type else group.type

        return groupRepo.save(group).toView()
    }

    override fun updatePerson(id: UUID, editPerson: EditPerson) : PersonDTO {
        val person = personRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        person.name = if (editPerson.name != null) editPerson.name else person.name
        person.debut = if (editPerson.debut != null) editPerson.debut else person.debut
        person.gender = if (editPerson.gender != null) editPerson.gender else person.gender
        person.birthday = if (editPerson.birthday != null) editPerson.birthday else person.birthday

        return personRepo.save(person).toView()
    }

    override fun deleteArtist(id: UUID): Artist {
        val artist = artistRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        artistRepo.deleteById(id)
        return artist
    }

    fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}