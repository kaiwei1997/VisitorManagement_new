package ch.makery.vm.model


import scalafx.beans.property.{ObjectProperty, StringProperty}
import ch.makery.vm.util.DateTimeUtil._

class AppointmentDetail(aIDS: String, aVFirstNames: String, aVLastNames: String, aVCompanys: String,
                        aEFNName: String, aReasons: String, appointmentDateTimes: String,
                        aCheckInDateTimes: String, aCheckOutDateTimes: String,
                        aCategorys: String, aVEmails: String) extends Appointment(aIDS,aReasons,appointmentDateTimes.parseLocalDateTime, aCheckInDateTimes.parseLocalDateTime, aCheckOutDateTimes.parseLocalDateTime ,aCategorys){

  var aVFirstName = StringProperty(aVFirstNames)
  var aVLastName = StringProperty(aVLastNames)
  var aVCompany = StringProperty(aVCompanys)
  var eFNName = StringProperty(aEFNName)
  var vEmail = StringProperty(aVEmails)
}
