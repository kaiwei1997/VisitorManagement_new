package ch.makery.vm.model

import java.time.LocalDateTime

import scalafx.beans.property.{ObjectProperty, StringProperty}

class AppointmentDetail(aIDS: String, aVFirstNames: String, aVLastNames: String, aVCompanys: String,
                        aEFNName: String, aReasons: String, appointmentDateTimes: String,
                        aCheckInDateTimes: String, aCheckOutDateTimes: String,
                        aCategorys: String, aVEmails: String) {

  var aID = StringProperty(aIDS)
  var aVFirstName = StringProperty(aVFirstNames)
  var aVLastName = StringProperty(aVLastNames)
  var aVCompany = StringProperty(aVCompanys)
  var eFNName = StringProperty(aEFNName)
  var aReason = StringProperty(aReasons)
  var appointmentDateTime = StringProperty(appointmentDateTimes)
  var checkInDateTime = StringProperty(aCheckInDateTimes)
  var checkOutDateTime = StringProperty(aCheckOutDateTimes)
  var aCategory = StringProperty(aCategorys)
  var vEmail = StringProperty(aVEmails)
}
