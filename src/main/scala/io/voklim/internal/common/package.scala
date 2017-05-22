package io.voklim.internal

package object common {
  implicit final class RichOption[A](val repr: Option[A]) extends AnyVal {
    def cata[B](noneF: => B, someF: A => B): B = repr match {
      case None => noneF
      case Some(a) => someF(a)
    }
  }
}
