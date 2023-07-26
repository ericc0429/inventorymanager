package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.*

interface GroupService {
  fun getGroups(): Iterable<GroupDTO>

  fun addGroup(newGroup: NewGroup): GroupDTO
}
