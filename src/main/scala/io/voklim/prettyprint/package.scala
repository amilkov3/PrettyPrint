package io.voklim

package object prettyprint {

  type PrettyPrint[A] = io.voklim.prettyprint.impl.PrettyPrint[A]
  val PrettyPrint = io.voklim.prettyprint.impl.PrettyPrint

  implicit final class PrettyPrintOps[A](val repr: A) extends AnyVal {
    def pprint(implicit ev: PrettyPrint[A]): String = ev.pprint(repr)
  }
}
