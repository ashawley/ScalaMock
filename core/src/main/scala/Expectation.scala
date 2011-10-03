// Copyright (c) 2011 Paul Butcher
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.borachio

/**
 * Represents a single expectation
 */
class Expectation(target: MockFunction) extends Handler {
  
  def expects(arguments: Any*) = {
    require(!argumentsMatcher.isDefined, "arguments matcher can only be set once")
    argumentsMatcher = Some({ args: Product => arguments sameElements args.productIterator.toIterable })
    argumentsString = "with arguments: "+ arguments.mkString("(", ", ", ")")
    this
  }
  
  def withArguments(arguments: Any*) = expects(arguments: _*)
  
  def withArgs(arguments: Any*) = expects(arguments: _*)

  def returns(value: Any) = {
    require(!returnValue.isDefined, "return value can only be set once")
    require(!exception.isDefined, "either return value or exception can be set, not both")
    returnValue = Some(value)
    this
  }
  
  def returning(value: Any) = returns(value)
  
  def throws(e: Throwable) = {
    require(!exception.isDefined, "exception can only be set once")
    require(!returnValue.isDefined, "either return value or exception can be set, not both")
    exception = Some(e)
    this
  }
  
  def throwing(e: Throwable) = throws(e)
  
  def repeat(range: Range) = {
    require(!expectedCalls.isDefined, "expected number of calls can only be set once")
    expectedCalls = Some(range)
    this
  }
  
  def repeat(count: Int): Expectation = repeat(count to count)
  
  def never() = repeat(0)
  def once() = repeat(1)
  def twice() = repeat(2)
  
  def anyNumberOfTimes() = repeat(0 to scala.Int.MaxValue - 1)
  def atLeastOnce() = repeat(1 to scala.Int.MaxValue - 1)
  def atLeastTwice() = repeat(2 to scala.Int.MaxValue - 1)

  def noMoreThanOnce() = repeat(0 to 1)
  def noMoreThanTwice() = repeat(0 to 2)
  
  def repeated(range: Range) = repeat(range)
  def repeated(count: Int) = repeat(count)
  def times() = this

  override def toString = 
    Seq(target.toString, argumentsString, returnString, expectedCallsString, actualCallsString).
      filter(_.length > 0).mkString(", ")
  
  private[borachio] def satisfied = expectedCalls match {
    case Some(r) => r contains actualCalls
    case None => actualCalls > 0
  }
  
  private[borachio] def unsatisfiedString = toString
  
  private[borachio] def exhausted = expectedCalls match {
    case Some(r) => r.last == actualCalls
    case None => false
  }
  
  private[borachio] def handle(mock: MockFunction, arguments: Array[Any]): Option[Any] = {
    if (mock == target && !exhausted) {
      if (!argumentsMatcher.isDefined || argumentsMatcher.get(tupled(arguments))) {
        actualCalls += 1
        exception match {
          case Some(e) => throw e
          case None => return Some(returnValue.getOrElse(null))
        }
      }
    }
    None
  }
  
  private def tupled(arguments: Array[Any]) = arguments match {
    case Array() => List()
    case Array(x1) => Tuple1(x1)
    case Array(x1, x2) => (x1, x2)
    case Array(x1, x2, x3) => (x1, x2, x3)
    case Array(x1, x2, x3, x4) => (x1, x2, x3, x4)
    case Array(x1, x2, x3, x4, x5) => (x1, x2, x3, x4, x5)
    case Array(x1, x2, x3, x4, x5, x6) => (x1, x2, x3, x4, x5, x6)
    case Array(x1, x2, x3, x4, x5, x6, x7) => (x1, x2, x3, x4, x5, x6, x7)
    case Array(x1, x2, x3, x4, x5, x6, x7, x8) => (x1, x2, x3, x4, x5, x6, x7, x8)
    case Array(x1, x2, x3, x4, x5, x6, x7, x8, x9) => (x1, x2, x3, x4, x5, x6, x7, x8, x9)
    case Array(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10) => (x1, x2, x3, x4, x5, x6, x7, x8, x9, x10)
  }
  
  private def returnString = returnValue match {
    case Some(r) => "returning: "+ r
    case None => ""
  }
  
  private def expectedCallsString = "expected calls: "+ (
    expectedCalls match {
      case Some(c) if c.start == c.last => c.start
      case Some(c) => c.start +" to "+ c.last
      case None => "1"
    }
  )
  
  private def actualCallsString = "actual calls: " + actualCalls

  private var argumentsMatcher: Option[Function1[Product, Boolean]] = None
  private var returnValue: Option[Any] = None
  private var exception: Option[Throwable] = None
  private var expectedCalls: Option[Range] = None
  
  private var argumentsString = ""

  private var actualCalls = 0
}
