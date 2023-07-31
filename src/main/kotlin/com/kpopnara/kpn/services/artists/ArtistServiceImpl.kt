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

    override fun getAll(): Iterable<ArtistDTO> {
        //return artistRepo.findAll()
        return groupRepo.findAll().map {it.toArtistDTO()} + personRepo.findAll().map {it.toArtistDTO()}
    }

    override fun getGroups(): Iterable<GroupDTO> {
        return groupRepo.findAll().map { it.toDTO() }
    }

    override fun getPeople(): Iterable<PersonDTO> {
        return personRepo.findAll().map { it.toDTO() }
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
        .toDTO()

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
        .toDTO()

    override fun updateGroup(id: UUID, editGroup: EditGroup) : GroupDTO {
        val group = groupRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        group.name = if (editGroup.name != null) editGroup.name else group.name
        group.debut = if (editGroup.debut != null) editGroup.debut else group.debut
        group.gender = if (editGroup.gender != null) editGroup.gender else group.gender
        group.type = if (editGroup.type != null) editGroup.type else group.type

        return groupRepo.save(group).toDTO()
    }

    override fun updatePerson(id: UUID, editPerson: EditPerson) : PersonDTO {
        val person = personRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        person.name = if (editPerson.name != null) editPerson.name else person.name
        person.debut = if (editPerson.debut != null) editPerson.debut else person.debut
        person.gender = if (editPerson.gender != null) editPerson.gender else person.gender
        person.birthday = if (editPerson.birthday != null) editPerson.birthday else person.birthday

        return personRepo.save(person).toDTO()
    }

    override fun addPersonToGroup(id: UUID, newMember: NewMember) : GroupDTO {
        val group = groupRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        val member = if (newMember.id != null) personRepo.findById(newMember.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
            else if (newMember.name != null) personRepo.findByName(newMember.name)/* .orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) } */
            else throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        group.members = if (member != null) group.members.plus(member) else group.members
        
        return groupRepo.save(group).toDTO()

    }

    // fun addAssetToGroup(id: UUID, newAsset)

    override fun deleteArtist(id: UUID): ArtistDTO {
        val artist = artistRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        if (artist is Person && artist.group.size > 0) {
            for (group: Group in artist.group ) group.members = group.members.minus(artist)
        }
        artistRepo.deleteById(id)
        return if (artist is Group) artist.toArtistDTO() else (artist as Person).toArtistDTO()
    }

    fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}