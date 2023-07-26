package com.kpopnara.kpn.repos

import com.kpopnara.kpn.models.*
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository interface GroupRepo : JpaRepository<Group, UUID> {}
