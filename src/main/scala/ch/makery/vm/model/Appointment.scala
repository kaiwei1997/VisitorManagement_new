package ch.makery.vm.model

import java.sql.Timestamp
import java.time.LocalDateTime

import scalafx.beans.property.{ObjectProperty, StringProperty}

  class Appointment(aIDS: String, aVIDs: Int, aEIDs: Int, aReasons: String, appointmentDateTimes: LocalDateTime,
                  aCheckInDateTimes: LocalDateTime, aCheckOutDateTimes: LocalDateTime, aCategorys: String) {

  var aID = StringProperty(aIDS)
  var aVID = ObjectProperty[Int](aVIDs)
  var aEID = ObjectProperty[Int](aEIDs)
  var aReason = StringProperty(aReasons)
  var appointmentDateTime = ObjectProperty[LocalDateTime](appointmentDateTimes)
  var checkInDateTime = ObjectProperty[LocalDateTime](aCheckInDateTimes)
  var checkOutDateTime = ObjectProperty[LocalDateTime](aCheckOutDateTimes)
  var aCategory = StringProperty(aCategorys)
}
