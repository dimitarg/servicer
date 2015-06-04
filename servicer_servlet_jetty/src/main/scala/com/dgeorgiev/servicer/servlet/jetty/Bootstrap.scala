package com.dgeorgiev.servicer.servlet.jetty

import com.dgeorgiev.servicer.config.Configuration
import com.dgeorgiev.servicer.servlet.ServicerDispatcher
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHandler

/**
 * Created by fmap on 04.06.15.
 */
class Bootstrap() {

  val server = {
    val s = new Server(Configuration.serverPort)
    val servletHandler = new ServletHandler()
    s.setHandler(servletHandler)
    servletHandler.addServletWithMapping(classOf[ServicerDispatcher], "/*")
    s
  }

  def start() : Unit = {
    server.start()
  }

  def join() : Unit = {
    server.join()
  }

}
