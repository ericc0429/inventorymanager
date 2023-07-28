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
    debut: String,
    // Albums
    albums: Set<Album>,
    // Other Assets
    assets: Set<Asset>,

    // Artist Specific Fields
    var birthday: String,
    var gender: GenderType,
    // Use a set in case of person being in a group and its subunits
    @ManyToMany var group: Set<Group>,
) : Artist(id, name, debut, albums, assets) {}

data class PersonDTO(
    val id: UUID?,
    var name: String,
    var debut: String,
    var albums: Iterable<String?>,
    var assets: Iterable<String?>,
    var birthday: String,
    var gender: GenderType,
    var group: Iterable<String?>,
)

fun Person.toView() =
    PersonDTO(
        id,
        name,
        debut,
        albums.map { it?.name },
        assets.map { it?.name },
        birthday,
        gender,
        group.map { it?.name },
    )

data class NewPerson(var name: String)
