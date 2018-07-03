package ch.makery.vm.model

import java.sql.Timestamp
import java.time.LocalDateTime

import scalafx.beans.property.{ObjectProperty, StringProperty}

  class Appointment(aIDS: String, aReasons: String, appointmentDateTimes: LocalDateTime, checkInDateTimeS: LocalDateTime,checkOutDateTimeS: LocalDateTime, aCategorys: String) {

  var aID = StringProperty(aIDS)
  var aVID = new ObjectProperty[Int]()
  var aEID = new ObjectProperty[Int]()
  var aReason = StringProperty(aReasons)
  var appointmentDateTime = ObjectProperty[LocalDateTime](appointmentDateTimes)
  var checkInDateTime = ObjectProperty[LocalDateTime](checkInDateTimeS)
  var checkOutDateTime = ObjectProperty[LocalDateTime](checkOutDateTimeS)
  var aCategory = StringProperty(aCategorys)
}
