package com.dgeorgiev.servicer.servlet

import javax.servlet.ServletConfig
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.dgeorgiev.servicer.core.{Entry, Registry, SynchEntry}
import com.dgeorgiev.servicer.servlet.jetty.Bootstrap

/**
 * Created by fmap on 03.06.15.
 */
class ServicerDispatcher extends HttpServlet {

  var registry : Registry[HttpServletRequest] = null

  val notFoundEntry = SynchEntry[HttpServletRequest,String,Unit,Unit]((req: HttpServletRequest) => Right(()), _ => Left("not found") )

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) : Unit = {

    val path = req.getPathInfo
    val pathNormalized = if (path.endsWith("/")) path.substring(0,path.length-1) else path

    val entry : Entry[HttpServletRequest] = registry.get(path).getOrElse(notFoundEntry)

    entry match {
      case se : SynchEntry[HttpServletRequest] => {
        val input = se.parse(req)
        val result : Either[se.ERR,se.Y] = input.right.flatMap(se.convert)
        resp.getWriter.println(result)
      }
    }
  }


  override def init(config: ServletConfig) : Unit = {
    super.init(config)
    registry = Bootstrap.getRegistry(config.getInitParameter("id"))
  }

}

