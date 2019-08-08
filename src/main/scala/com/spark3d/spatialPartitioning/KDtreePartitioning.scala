/*
 * Copyright 2018 AstroLab Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.astrolabsoftware.spark3d.spatialPartitioning

import com.astrolabsoftware.spark3d.spatialPartitioning
 

import com.astrolabsoftware.spark3d.geometryObjects.BoxEnvelope
import com.astrolabsoftware.spark3d.geometryObjects._
import com.astrolabsoftware.spark3d.geometryObjects.Shape3D.Shape3D
import com.astrolabsoftware.spark3d.geometryObjects.Point3D


import scala.collection.mutable.ListBuffer

class KDtreePartitioning (private val kdtree: KDtree, grids:List[BoxEnvelope])
  extends Serializable {
  /**
    * @return the octree used for partitioning
    */
  def getPartitionTree(): KDtree = {
      kdtree
    
  }

  /**
    * @return Leaf nodes of the partitioning tree
    */
  def getGrids(): List[BoxEnvelope] = {
     // grids.isInstanceOf[List[BoxEnvelope]]
     grids 
  }
}

object KDtreePartitioning {

  def apply(data: List[Point3D], tree: KDtree, levelPart:Int): KDtreePartitioning = {

    //Initialize the boundary box
    var min_X:Double=data(0).x
    var max_X:Double=data(0).x
    var min_Y:Double=data(0).y
    var max_Y:Double=data(0).y
    var min_Z:Double=data(0).z
    var max_Z:Double=data(0).z
    
    for(i<-data){

     if(i.x<min_X)
       min_X=i.x

     if(i.x>max_X)
       max_X=i.x

     if(i.y<min_Y)
        min_Y=i.y

     if(i.y>max_Y)
        max_Y=i.y

      if(i.z<min_Z)
        min_Z=i.z

     if(i.z>max_Z)
        max_Z=i.z
    }

    val KDtreeBoundary:BoxEnvelope=BoxEnvelope.apply(min_X-1,max_X+1,min_Y-1,max_Y+1,min_Z-1,max_Z+1)
    
    tree.insertList(data,0,KDtreeBoundary)
    //tree.printKDtree(tree)

    val grids=tree.BFS(tree,levelPart).toList
    new KDtreePartitioning(tree,grids)

    
    
  }
}