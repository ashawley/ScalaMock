package org.scalamock.test.scalatest

import org.scalamock.scalatest.MixedMockFactory
import org.scalatest.{FlatSpec, Matchers}

class MixedMockFactoryTest extends FlatSpec with MixedMockFactory with Matchers {
  "mixed mocks" should "work" in {
    trait Foo {
      def getI: Int
    }
    val m = mock[Foo]
    val p = Proxy.mock[Foo]

    p.expects(sym"getI")().returning(5).once()
    m.getI _ expects() returning 42 once()

    m.getI should be(42)
    p.getI should be(5)
  }
}
