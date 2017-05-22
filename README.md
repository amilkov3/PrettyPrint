## PrettyPrint

Will convert any case class to a formatted String:

```scala
case class Foo(x: String, y: Option[Boolean], z: Double)
Foo("hello", Some(true), 1.25)

```
will be represented as: 
```
fully.qualified.class.foo.is.defined.in$Foo: (x = 'hello', y = Some(true), 1.25)
```

### Usage
```scala
import io.voklim.prettyprint._

case class Foo(x: Option[Boolean], y: Bar)
case class Bar(x: String, y: Double)

Foo(Some(true), Bar("hello", 1.25)).pprint

```

### Motivation
Really you can just call `.toString` on a case class instance, but I wanted
to leverage shapeless to include case class member names via `LabelledGeneric`
