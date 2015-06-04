package com.dgeorgiev.servicer.config

import com.typesafe.config.ConfigFactory

/**
 * Created by fmap on 03.06.15.
 */
object Configuration {

  private lazy val config = ConfigFactory.load()

  val serverPort = config.getInt("server.port")

}
