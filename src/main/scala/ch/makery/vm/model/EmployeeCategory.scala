package ch.makery.vm.model

import scalafx.beans.property.{ObjectProperty, StringProperty}

class EmployeeCategory(eCatIDs:Int, eCategoryNames: String) {
  var eCatID =  ObjectProperty[Int](eCatIDs)
  var eCategoryName = new StringProperty(eCategoryNames)
}
