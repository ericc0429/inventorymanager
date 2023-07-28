package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.repos.GroupRepo
import java.util.Optional
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GroupServiceImpl(val db: GroupRepo) : GroupService {

  override fun getGroups(): Iterable<GroupDTO> {
    return db.findAll().map { it.toView() }
  }

  override fun addGroup(newGroup: NewGroup): GroupDTO =
      db.save(
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

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}

  /* fun addAttrib(id: UUID, newAttrib: NewAttrib): GroupDTO {
    println("==== id:   $id")
    val memberid = UUID.fromString(newAttrib.attribId)
    println("==== memberid: $memberid")
    val group = db.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
    println("==== group: ${group.name}")
    val member =
        artistRepo.findById(memberid).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
    println("==== name: ${member.name}")
    return db.save(group.copy(members = group.members.plus(member))).toView()
  } */

  /*   fun addAttrib(id: UUID, newAttrib: NewAttrib): GroupDTO {
    val group = db.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
    if (newAttrib.type == AttribType.MEMBER) {
      val member =
          artistRepo.findById(newAttrib.attribId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
          }
      return db.save(group.copy(members = group.members.plus(member))).toView()
    } else if (newAttrib.type == AttribType.ALBUM) {
      val album =
          albumRepo.findById(newAttrib.attribId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
          }
      return db.save(group.copy(albums = group.albums.plus(album))).toView()
    } else if (newAttrib.type == AttribType.ASSET) {
      val asset =
          assetRepo.findById(newAttrib.attribId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
          }
      return db.save(group.copy(assets = group.assets.plus(asset))).toView()
    } else return group.toView()
  } */
