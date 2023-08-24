package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.repos.ArtistRepo
import com.kpopnara.kpn.repos.ProductRepo
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
    val personRepo: ArtistRepo<Person>,
    val albumRepo: ProductRepo<Album>,
    val assetRepo: ProductRepo<Asset>
    ) : ArtistService, GroupService, PersonService {

    override fun getArtists(): Iterable<ArtistDTO> {
        return groupRepo.findAll().map {it.toDTO()} + personRepo.findAll().map {it.toDTO()}
    }

    override fun getArtistById(id: UUID): ArtistDTO {
        val artist =
            artistRepo.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        if (artist.type == ArtistType.GROUP || artist.type == ArtistType.SUBUNIT)
            return groupRepo.getReferenceById(id).toDTO()
        else return personRepo.getReferenceById(id).toDTO()
    }

    override fun getGroups(): Iterable<ArtistDTO> {
        return groupRepo.findAll().map { it.toDTO() }
    }

    override fun getPeople(): Iterable<ArtistDTO> {
        return personRepo.findAll().map { it.toDTO() }
    }

    override fun addArtist(newArtist: NewArtist): ArtistDTO {
        if (newArtist.type == ArtistType.NONE) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
        else if ( newArtist.type == ArtistType.ARTIST) {
            return addPerson(newArtist)
        }
        else return addGroup(newArtist)
    }

    override fun addGroup(newArtist: NewArtist): ArtistDTO {
        return groupRepo.save(
            Group(
                id = null,
                name = newArtist.name,
                type = newArtist.type,
                gender = newArtist.gender,
                debut = newArtist.debut,
                albums = emptySet<Album>(),
                assets = emptySet<Asset>(),
                members = emptySet<Artist>(),
            )
        )
        .toDTO()
    }

    override fun addPerson(newArtist: NewArtist): ArtistDTO {
        return personRepo.save(
            Person(
                id = null,
                name = newArtist.name,
                gender = newArtist.gender,
                debut = newArtist.debut,
                albums = emptySet<Album>(),
                assets = emptySet<Asset>(),
                birthday = newArtist.birthday,
                group = emptySet<Group>(),
            )
        )
        .toDTO()
    }

    // override fun updateArtist(id: UUID, editArtist: EditArtist) :

    override fun updateGroup(id: UUID, editArtist: EditArtist) : ArtistDTO {
        val group = groupRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        group.name = if (editArtist.name != null) editArtist.name else group.name
        group.debut = if (editArtist.debut != null) editArtist.debut else group.debut
        group.gender = if (editArtist.gender != null) editArtist.gender else group.gender
        group.type = if (editArtist.type != null &&
                            (editArtist.type == ArtistType.SUBUNIT ||
                            editArtist.type == ArtistType.GROUP)
                        ) editArtist.type else group.type

        return groupRepo.save(group).toDTO()
    }

    override fun updatePerson(id: UUID, editArtist: EditArtist) : ArtistDTO {
        val person = personRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        person.name = if (editArtist.name != null) editArtist.name else person.name
        person.debut = if (editArtist.debut != null) editArtist.debut else person.debut
        person.gender = if (editArtist.gender != null) editArtist.gender else person.gender
        person.birthday = if (editArtist.birthday != null) editArtist.birthday else person.birthday

        return personRepo.save(person).toDTO()
    }

    override fun addPersonToGroup(id: UUID, newMember: IdNameDTO) : ArtistDTO {
        val group = groupRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Group not found.") }
        val member = if (newMember.id != null) personRepo.findById(newMember.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.") }
            else if (newMember.name != null) personRepo.findByName(newMember.name)
            else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide an id or name in request body.")

        if (member != null) {
            if (member !in group.members) {
                group.members = group.members.plus(member)
                return groupRepo.save(group).toDTO()
            }
            else throw ResponseStatusException(HttpStatus.CONFLICT, "Artist already a member of Group.")
        }
        else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.")
    }

    override fun removePersonFromGroup(id: UUID, removeMember: IdNameDTO) : ArtistDTO {
        val group = groupRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Group not found.") }
        val member = if (removeMember.id != null) personRepo.findById(removeMember.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.") }
            else if (removeMember.name != null) personRepo.findByName(removeMember.name)
            else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide an id or name in request body.")

        if (member != null) {
            if (member in group.members) {
                group.members = group.members.minus(member)
                return groupRepo.save(group).toDTO()
            }
            else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found in Group.")
        }
        else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.")
    }

    override fun joinGroup(id: UUID, joinGroup: IdNameDTO): ArtistDTO {
        val member = personRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.") }
        val group = if (joinGroup.id != null) groupRepo.findById(joinGroup.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Group not found.") }
            else if (joinGroup.name != null) groupRepo.findByName(joinGroup.name)
            else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide an id or name in request body.")

        if (group != null) {
            if (member !in group.members) {
                group.members = group.members.plus(member)
                groupRepo.save(group)
                member.group = member.group.plus(group)
                return member.toDTO()
            }
            else throw ResponseStatusException(HttpStatus.CONFLICT, "Artist already a member of Group.")
        }
        else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Group not found.")
    }

    override fun leaveGroup(id: UUID, leaveGroup: IdNameDTO): ArtistDTO {
        val member = personRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.") }
        val group = if (leaveGroup.id != null) groupRepo.findById(leaveGroup.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Group not found.") }
            else if (leaveGroup.name != null) groupRepo.findByName(leaveGroup.name)
            else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide an id or name in request body.")

        if (group != null) {
            if (member in group.members) {
                group.members = group.members.minus(member)
                groupRepo.save(group)
                member.group = member.group.minus(group)
                return member.toDTO()
            }
            else throw ResponseStatusException(HttpStatus.CONFLICT, "Artist not found in specified Group.")
        }
        else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Group not found.")
    }

    override fun addAlbumToArtist(id: UUID, product: IdNameDTO): ArtistDTO {
        val artist = artistRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.") }
        val album = if (product.id != null) albumRepo.findById(product.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Album not found.") }
                    else if (product.name != null) albumRepo.findByName(product.name)
                    else throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if (album != null) {
            if (album !in artist.albums) {
                artist.albums = artist.albums.plus(album)
                val result = artistRepo.save(artist)
                return if (result is Group) result.toDTO() else (result as Person).toDTO()
            }
            else throw ResponseStatusException(HttpStatus.CONFLICT)
        }
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
    override fun removeAlbumFromArtist(id: UUID, product: IdNameDTO): ArtistDTO {
        val artist = artistRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.") }
        val album = if (product.id != null) albumRepo.findById(product.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Album not found.") }
                    else if (product.name != null) albumRepo.findByName(product.name)
                    else throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if (album != null) {
            if (album in artist.albums) {
                artist.albums = artist.albums.minus(album)
                val result = artistRepo.save(artist)
                return if (result is Group) result.toDTO() else (result as Person).toDTO()
            }
            else throw ResponseStatusException(HttpStatus.CONFLICT)
        }
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    override fun addAssetToArtist(id: UUID, product: IdNameDTO): ArtistDTO {
        val artist = artistRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.") }
        val asset = if (product.id != null) assetRepo.findById(product.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Asset not found.") }
                    else if (product.name != null) assetRepo.findByName(product.name)
                    else throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if (asset != null) {
            if (asset !in artist.assets) {
                artist.assets = artist.assets.plus(asset)
                val result = artistRepo.save(artist)
                return if (result is Group) result.toDTO() else (result as Person).toDTO()
            }
            else throw ResponseStatusException(HttpStatus.CONFLICT)
        }
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
    override fun removeAssetFromArtist(id: UUID, product: IdNameDTO): ArtistDTO {
        val artist = artistRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Artist not found.") }
        val asset = if (product.id != null) assetRepo.findById(product.id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Specified Asset not found.") }
                    else if (product.name != null) assetRepo.findByName(product.name)
                    else throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if (asset != null) {
            if (asset in artist.assets) {
                artist.assets = artist.assets.minus(asset)
                val result = artistRepo.save(artist)
                return if (result is Group) result.toDTO() else (result as Person).toDTO()
            }
            else throw ResponseStatusException(HttpStatus.CONFLICT)
        }
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    override fun deleteArtist(id: UUID): ArtistDTO {
        val artist = artistRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        if (artist is Person && artist.group.size > 0) {
            for (group: Group in artist.group ) group.members = group.members.minus(artist)
        }
        artistRepo.deleteById(id)
        return if (artist is Group) artist.toDTO() else (artist as Person).toDTO()
    }

    fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}