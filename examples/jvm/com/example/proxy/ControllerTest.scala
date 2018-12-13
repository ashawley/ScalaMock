// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

package com.example.proxy

import org.scalatest.FunSuite
import org.scalamock.scalatest.proxy.MockFactory
import scala.math.{Pi, sqrt}
import com.example.{Controller, Turtle}
 
class ControllerTest extends FunSuite with MockFactory {

  test("draw line") {
    val mockTurtle = mock[Turtle]
    val controller = new Controller(mockTurtle)
 
    inSequence {
      inAnyOrder {
        mockTurtle.expects(sym"penUp")()
        mockTurtle.expects(sym"getPosition")().returning(0.0, 0.0)
        mockTurtle.expects(sym"getAngle")().returning(0.0)
      }
      mockTurtle.expects(sym"turn")(~(Pi / 4))
      mockTurtle.expects(sym"forward")(~sqrt(2.0))
      mockTurtle.expects(sym"getAngle")().returning(Pi / 4)
      mockTurtle.expects(sym"turn")(~(-Pi / 4))
      mockTurtle.expects(sym"penDown")()
      mockTurtle.expects(sym"forward")(1.0)
    }
 
    controller.drawLine((1.0, 1.0), (2.0, 1.0))
  }
}