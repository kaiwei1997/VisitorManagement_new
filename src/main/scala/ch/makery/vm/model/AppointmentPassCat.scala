package ch.makery.vm.model

import scalafx.beans.property.{ObjectProperty, StringProperty}

class AppointmentPassCat(catIDs:Int,catNAmes: String) {
  var catID = ObjectProperty[Int](catIDs)
  var catName = new StringProperty(catNAmes)
}
