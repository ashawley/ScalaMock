// Copyright (c) 2011-2012 Paul Butcher
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

package org.scalamock

import org.scalatest.FreeSpec

class MockFunctionTest extends FreeSpec with MockFactoryBase {
  
  "A mock function should" - {
    "return null unless told otherwise" in {
      val m = mockFunction[String]
      expect(null) { m() }
    }
    
    "have a sensible default name" in {
      val m = mockFunction[String]
      expect("unnamed MockFunction0"){ m.toString }
    }
    
    "have the name we gave it" in {
      val m1 = mockFunction[String](Symbol("a mock function"))
      expect("a mock function"){ m1.toString }

      val m2 = mockFunction[String]("another mock function")
      expect("another mock function"){ m2.toString }
    }
    
    "resolve ambiguity when taking a symbol argument" in {
      val m1 = mockFunction[Symbol, String]
      expect("unnamed MockFunction1"){ m1.toString }

      val m2 = mockFunction[Symbol, String](mockFunctionName("a named mock"))
      expect("a named mock"){ m2.toString }
    }
  }
}