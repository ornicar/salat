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
import com.novus.salat.test.always._
import com.novus.salat.test.model._
import com.mongodb.casbah.Imports._

class CaseObjectSupport extends SalatSpec {
  "a grater" should {

    "support case objects" in {
      "be able to serialize case objects" in {
        val mine = Wardrobe(suits = List(Zoot))
        log.info("mine: %s", mine)
        val dbo: MongoDBObject = grater[Wardrobe].asDBObject(mine)
        log.info("dbo : %s", dbo)
        val suits = dbo.expand[BasicDBList]("suits")
        suits must beSome[BasicDBList]
        suits.getOrElse(throw new Exception("argh, someone stole my wardrobe")) // TODO: where did which go?
        suits match {
          case Some(suits) => {
            val suit: MongoDBObject = suits(0).asInstanceOf[DBObject]
            val th = suit.expand[String]("_typeHint")
            th must beSome(Zoot.getClass.getName)
          }
          case None => throw new Exception("where are my suits?") // TODO: where did fail go?
        }
      }

      "be able to deserialize case objects" in {
        val mine = Wardrobe(suits = List(WhatArmstrongWore, Zoot))
        val dbo = grater[Wardrobe].asDBObject(mine)
        val mine_* = grater[Wardrobe].asObject(dbo)
        mine must_== mine_*
      }
    }

    "support nested case objects" in {
      "be able to serialize nested case objects" in {
        val tree = Tree("a", Tree("b", Tree("c"), Tree("d")), Tree("e"))
        log.info("tree: %s", tree)
        val dbo: MongoDBObject = grater[Tree].asDBObject(tree)
        log.info("dbo : %s", dbo)
        val optionTrees = dbo.expand[BasicDBList]("trees")
        optionTrees must beSome[BasicDBList]
        val trees = optionTrees.get
        val treeB: MongoDBObject = trees(0).asInstanceOf[DBObject]
        val th = treeB.expand[String]("_typeHint")
        th must beSome(tree.getClass.getName)
        treeB.expand[String]("name") must_== Some("b")
      }

      "be able to deserialize case objects" in {
        val tree = Tree("a", Tree("b", Tree("c"), Tree("d")), Tree("e"))
        val dbo = grater[Tree].asDBObject(tree)
        val tree_* = grater[Tree].asObject(dbo)
        tree must_== tree_*
      }
    }
  }
}
