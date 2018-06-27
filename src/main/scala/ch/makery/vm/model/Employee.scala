package ch.makery.vm.model

import java.time.LocalDate

import scalafx.beans.property.{ObjectProperty, StringProperty}

class Employee(eid: Int, epassword: String, efname: String, elname: String, eIcNOs: String
               , eemail: String, econtact: String, estreet: String, estate: String, ecity: String
               , epostal: Int, edept: String, ecat: String, eWP: String, ecountry: String, eDOBs: LocalDate) {

  var eID = ObjectProperty[Int](eid)
  var ePassword = new StringProperty(epassword)
  var eFname = new StringProperty(efname)
  var eLname = new StringProperty(elname)
  var eIcNo = new StringProperty(eIcNOs)
  var eEmail = new StringProperty(eemail)
  var eContact = new StringProperty(econtact)
  var eStreetAddress = new StringProperty(estreet)
  var eState = new StringProperty(estate)
  var eCity = new StringProperty(ecity)
  var ePostal = ObjectProperty[Int](epostal)
  var eDOB = ObjectProperty[LocalDate](eDOBs)
  var eDept = new StringProperty(edept)
  var eCat = new StringProperty(ecat)
  var eWorkPhone = new StringProperty(eWP)
  var eCountry = new StringProperty(ecountry)
}

