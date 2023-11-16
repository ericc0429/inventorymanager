package com.kpopnara.kpn.models.artists

import com.kpopnara.kpn.models.products.Album
import com.kpopnara.kpn.models.products.Asset
import jakarta.persistence.*
import java.util.UUID
import org.springframework.web.bind.annotation.*

/* ENTITY -- Artist -- Person
This represents a solo person or a member of a group.
*/
@Entity
@DiscriminatorValue("Person")
class Person(
    // Inherited
    id: UUID?, // Unique Identifier
    name: String,
    gender: GenderType,
    debut: String,
    // Albums
    albums: Set<Album>,
    // Other Assets
    assets: Set<Asset>,

    // Artist Specific Fields
    var birthday: String,
    // Use a set in case of person being in a group and its subunits
    @ManyToMany(mappedBy = "members") var group: Set<Group>,
) : Artist(id, name, ArtistType.ARTIST, gender, debut, albums, assets) {}

fun Person.toDTO() = 
    ArtistDTO(
        id = id,
        name = name,
        type = ArtistType.ARTIST,
        gender = gender,
        debut = debut,
        albums = albums.map { it.name },
        assets = assets.map { it.name },
        
        members = emptySet(),
        birthday = birthday,
        group = group.map{ it.name },
    )

data class SearchGroup(val id: UUID?, val name: String?)