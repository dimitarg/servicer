package com.dgeorgiev.servicer.jetty

import javax.servlet.http.HttpServletRequest

import com.dgeorgiev.servicer.jetty.config.Configuration
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHandler

object Main extends App {

  object myService {
    val parse: HttpServletRequest=>Either[String,Int] = req => {
      try{
        val paramVal = req.getParameter("num")
        Right(Integer.parseInt(paramVal))
      }catch{
        case ex : NumberFormatException => Left(ex.getMessage)
      }
    }

    val calc: Int=>Either[String,Int] = i => {
      if(i%2==0) Right(i/2) else Left(s"$i is odd")
    }
  }

  ServletRegistry.registry = ServletRegistry.registry.addMapping("/foo", myService.parse, myService.calc)


  val server = new Server(Configuration.serverPort)
  val servetHandler = new ServletHandler()
  server.setHandler(servetHandler)
  servetHandler.addServletWithMapping(classOf[ServicerDispatcher], "/*")

  server.start()
  server.join()

}