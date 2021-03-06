/** Copyright (c) 2010, 2011 Novus Partners, Inc. <http://novus.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  For questions and comments about this product, please see the project page at:
 *
 *  http://github.com/novus/salat
 *
 */
package com.novus.salat.test

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.test.model._
import com.mongodb.casbah.Imports._

class FloatSpec extends SalatSpec {
  "A grater" should {
    "support floats" in {
      val q = Quentin(mire = 3.14f)
      val dbo: MongoDBObject = grater[Quentin].asDBObject(q)
      //      println(MapPrettyPrinter(dbo))
      dbo must havePair("_typeHint" -> "com.novus.salat.test.model.Quentin")
      dbo must havePair("mire" -> 3.14f)

      val coll = MongoConnection()(SalatSpecDb)("salat_float_test_1")
      val wr = coll.insert(dbo)
      //      println("WR: %s".format(wr))

      val q_* = grater[Quentin].asObject(coll.findOne().get)
      q_* must_== q
    }
  }
}