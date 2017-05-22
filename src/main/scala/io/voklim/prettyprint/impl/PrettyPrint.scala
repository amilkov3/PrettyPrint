package io.voklim.prettyprint.impl

import shapeless._
import shapeless.labelled.FieldType

/** Type class for encoding any type as a string */
trait PrettyPrint[A] {
  def pprint(a: A): String
}

object PrettyPrint {

  def apply[A](implicit ev: PrettyPrint[A]) = ev

  def instance[A](f: A => String) = new PrettyPrint[A] {
    override def pprint(a: A): String = f(a)
  }

  def toStringInstance[A] = instance[A](_.toString)

  implicit val prettyPrintInt: PrettyPrint[Int] = toStringInstance[Int]
  implicit val prettyPrintDouble: PrettyPrint[Double] = toStringInstance[Double]
  implicit val prettyPrintString: PrettyPrint[String] = instance[String](str => s"'${identity(str)}'")
  implicit val prettyPrintFloat: PrettyPrint[Float] = toStringInstance[Float]
  implicit val prettyPrintBoolean: PrettyPrint[Boolean] = toStringInstance[Boolean]

  implicit def prettyPrintList[A](implicit ev: PrettyPrint[A]): PrettyPrint[List[A]] = instance[List[A]](
    _.map(ev.pprint).mkString(", ")
  )

  implicit def prettyPrintOption[A](implicit ev: PrettyPrint[A]): PrettyPrint[Option[A]] = toStringInstance[Option[A]]

  implicit val prettyPrintHNil: PrettyPrint[HNil] = instance[HNil](_ => "")

  /** Implicit hlist encoder. This definition is recursively invoked to convert an entire hlist into a string
    * based on there being evidence that all of its constituent types have instances of [[PrettyPrint]] */
  implicit def hlistEncoder[K <: Symbol, H, T <: HList](
    implicit
    witness: Witness.Aux[K],
    hEncoder: Lazy[PrettyPrint[H]],
    tEncoder: PrettyPrint[T]
  ): PrettyPrint[FieldType[K, H] :: T] = {
    val fieldName = witness.value.name
    instance{
      /** Get rid of the trailing comma */
      case h :: HNil => s"$fieldName = ${hEncoder.value.pprint(h)}"
      case h :: t => s"$fieldName = ${hEncoder.value.pprint(h)}, ${tEncoder.pprint(t)}"}
  }

  /** Generic type encoder. This tells shapeless how to convert a case class to a string. The case class
    * is converted to its generic representation (an hlist) via the implicit hlist encoder we defined above */
  implicit def genericEncoder[A, H <: HList](
    implicit
    generic: LabelledGeneric.Aux[A, H],
    hEncoder: Lazy[PrettyPrint[H]]
  ): PrettyPrint[A] = {
    instance{a => s"${a.getClass().getName()}: (${hEncoder.value.pprint(generic.to(a))})"}
  }
}
