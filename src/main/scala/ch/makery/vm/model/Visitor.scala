package ch.makery.vm.model

import scalafx.delegate.SFXDelegate
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.scene.image.Image

class Visitor(vIDS: Int, vfnameS: String, vlnameS: String, vNRICS: String, vstreetS: String, vcityS: String,
              vstateS: String, vpostal: Int, vcontactS: String, vemailS: String,
              vcompanyS: String, vCountryS: String) {
  var vID = ObjectProperty[Int](vIDS)
  var vFirstName = new StringProperty(vfnameS)
  var vLastName = new StringProperty(vlnameS)
  var vNRICNo = new StringProperty(vNRICS)
  var vStreetAddress = new StringProperty(vstreetS)
  var vCity = new StringProperty(vcityS)
  var vState = new StringProperty(vstateS)
  var vPostal = ObjectProperty[Int](vpostal)
  var vContact = new StringProperty(vcontactS)
  var vEmail = new StringProperty(vemailS)
  var vCompany = new StringProperty(vcompanyS)
  var vCountry = new StringProperty(vCountryS)
}
