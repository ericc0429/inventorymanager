package com.example.kpn

import com.example.kpn.models.SquareService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication class KpnApplication

fun main(args: Array<String>) {
  runApplication<KpnApplication>(*args)
  var squareService = SquareService();
  squareService.authorize();
}
