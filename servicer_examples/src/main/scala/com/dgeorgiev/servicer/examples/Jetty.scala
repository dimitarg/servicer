package com.dgeorgiev.servicer.examples

import javax.servlet.http.HttpServletRequest

import com.dgeorgiev.servicer.core.Registry
import com.dgeorgiev.servicer.servlet.jetty.Bootstrap

/**
 * Created by fmap on 04.06.15.
 */
object Jetty extends App {

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

  val bootstrap = new Bootstrap(9090, Registry().addMapping("/foo", myService.parse, myService.calc))
  bootstrap.start()
  bootstrap.join()


}
