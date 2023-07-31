package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import java.util.UUID

interface GroupService {
  fun getGroups(): Iterable<GroupDTO>

  fun addGroup(newGroup: NewGroup): GroupDTO

  fun updateGroup(id: UUID, editGroup: EditGroup): GroupDTO

  fun addPersonToGroup(id: UUID, newMember: NewMember): GroupDTO
}
