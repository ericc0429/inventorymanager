package com.kpopnara.kpn.repos

import com.kpopnara.kpn.models.artists.*
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository interface ArtistRepo<T : Artist?> : JpaRepository<T, UUID?>

@Repository interface GroupRepo : ArtistRepo<Group>

@Repository interface PersonRepo : ArtistRepo<Person>
