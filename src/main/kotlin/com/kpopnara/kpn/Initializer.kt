package com.kpopnara.kpn

import com.kpopnara.kpn.models.Group;
import com.kpopnara.kpn.models.GroupRepo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.stream.Stream;

@Component
class Initializer : CommandLineRunner {

  lateinit var repo: GroupRepo

  Initializer(repo: GroupRepo)

  override fun run(vararg strings: String) {
    Stream.of("BTS", "TWICE", "BlackPink").forEach({name -> repo.save(Group(name))})
  }
}