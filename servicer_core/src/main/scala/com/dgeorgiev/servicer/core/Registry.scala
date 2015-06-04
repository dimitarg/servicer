package com.dgeorgiev.servicer.core


/**
 * Created by fmap on 03.06.15.
 */
class Registry[A](val map: Map[String,Entry[A]]) {

  def addMapping[B,C,Err,Az](path: String, validate: A=>Either[Err,B], service: B=> Either[Err,C]) : Registry[A] = {
    new Registry[A](map + (path-> SynchEntry(validate,service)))
  }

  def get(path: String) : Option[Entry[A]] = {
    map.get(path)
  }

}

sealed trait Entry[A]

abstract case class SynchEntry[IN]() extends Entry[IN] {
  type ERR
  type X
  type Y
  val parse: IN => Either[ERR,X]
  val convert: X=> Either[ERR,Y]
}

object SynchEntry {
  def apply[INN,ERRR,XX,YY](aParse: INN=>Either[ERRR,XX], aConvert: XX=>Either[ERRR,YY]) : SynchEntry[INN] = {
    new SynchEntry[INN] {
      override type ERR = ERRR
      override type Y = YY
      override type X = XX
      override val convert = aConvert
      override val parse = aParse
    }
  }
}
