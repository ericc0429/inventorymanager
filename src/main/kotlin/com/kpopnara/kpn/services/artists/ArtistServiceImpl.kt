package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.repos.ArtistRepo
import com.kpopnara.kpn.repos.GroupRepo
import com.kpopnara.kpn.repos.PersonRepo
import java.util.Optional
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ArtistServiceImpl(val artistRepo: ArtistRepo<Artist>, val groupRepo: ArtistRepo<Group>, val personRepo: ArtistRepo<Person>) : ArtistService, GroupService, PersonService {

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

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}