package com.dgeorgiev.servicer.servlet.jetty

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest

import com.dgeorgiev.servicer.core.Registry
import com.dgeorgiev.servicer.servlet.ServicerDispatcher
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHandler

/**
 * Created by fmap on 04.06.15.
 */
class Bootstrap(val serverPort: Int, val registry: Registry[HttpServletRequest]) {

  val id = UUID.randomUUID()

  val server = {
    val s = new Server(serverPort)
    val servletHandler = new ServletHandler()
    s.setHandler(servletHandler)
    val holder = servletHandler.addServletWithMapping(classOf[ServicerDispatcher], "/*")
    Bootstrap.registries.put(id.toString, registry)
    holder.setInitParameter("id", id.toString)

    s
  }

  def start() : Unit = {
    server.start()
  }

  def join() : Unit = {
    server.join()
  }

}

object Bootstrap {
  private val registries = new ConcurrentHashMap[String,Registry[HttpServletRequest]]()
  def getRegistry(bootstrapId: String) : Registry[HttpServletRequest] = registries.get(bootstrapId)

}
