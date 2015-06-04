package com.dgeorgiev.servicer.servlet

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.dgeorgiev.servicer.core.{Entry, Registry, SynchEntry}

/**
 * Created by fmap on 03.06.15.
 */
class ServicerDispatcher extends HttpServlet {

  val notFoundEntry = SynchEntry[HttpServletRequest,String,Unit,Unit]((req: HttpServletRequest) => Right(()), _ => Left("not found") )

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) : Unit = {

    val path = req.getPathInfo
    val pathNormalized = if (path.endsWith("/")) path.substring(0,path.length-1) else path

    val entry : Entry[HttpServletRequest] = ServletRegistry.registry.get(path).getOrElse(notFoundEntry)

    entry match {
      case se : SynchEntry[HttpServletRequest] => {
        val input = se.parse(req)
        val result : Either[se.ERR,se.Y] = input.right.flatMap(se.convert)
        resp.getWriter.println(result)
      }
    }


  }

}


object ServletRegistry {
  var registry: Registry[HttpServletRequest] = new Registry[HttpServletRequest](Map())
}

