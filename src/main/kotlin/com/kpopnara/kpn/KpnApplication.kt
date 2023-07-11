package com.kpopnara.kpn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class KpnApplication

fun main(args: Array<String>) {
  runApplication<KpnApplication>(*args)
}
