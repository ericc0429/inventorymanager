package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.*
import com.kpopnara.kpn.services.GroupServiceImpl
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/groups")
class GroupController(val service: GroupServiceImpl) {
  @GetMapping fun getGroups(): Iterable<GroupDTO> = service.getGroups()

  @PostMapping fun addGroup(@RequestBody newGroup: NewGroup) = service.addGroup(newGroup)

  /* @PostMapping("{id}/add")
  fun addAttrib(@PathVariable id: UUID, @RequestBody newAttrib: NewAttrib): GroupDTO =
      service.addAttrib(id, newAttrib) */
}
