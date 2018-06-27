package ch.makery.vm

import java.sql.{Connection, PreparedStatement, SQLException}

import ch.makery.vm.model.Visitor
import ch.makery.vm.util.ConnectionUtil
import oracle.net.aso.r
import org.apache.commons.validator.routines.EmailValidator

import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Label, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class NewVisitorDialogController(
                                  private val vFNameField: TextField,
                                  private val vLNameField: TextField,
                                  private val vNRICFied: TextField,
                                  private val vCompanyField: TextField,
                                  private val vStreetField: TextField,
                                  private val vCityField: TextField,
                                  private val vStateField: TextField,
                                  private val vPostalField: TextField,
                                  private val vCountryField: TextField,
                                  private val vContactField: TextField,
                                  private val vEmailField: TextField,
                                  private val vIDField: Label
                                ) {
  var dialogStage: Stage = null
  private var _visitor: Visitor = null
  var okClicked = false

  var connection: Connection = null
  var preparedStatement: PreparedStatement = null

  connection = ConnectionUtil.connectiondb();

  def visitor = _visitor

  def visitor_=(z: Visitor): Unit = {
    _visitor = z

    vFNameField.text = _visitor.vFirstName.value
    vLNameField.text = _visitor.vLastName.value
    vNRICFied.text = _visitor.vNRICNo.value
    vCompanyField.text = _visitor.vCompany.value
    vStreetField.text = _visitor.vStreetAddress.value
    vCityField.text = _visitor.vCity.value
    vStateField.text = _visitor.vState.value
    vPostalField.text = _visitor.vPostal.value.toString
    vCountryField.text = _visitor.vCountry.value
    vContactField.text = _visitor.vContact.value
    vEmailField.text = _visitor.vEmail.value
    vIDField.text = _visitor.vID.value.toString
  }

  def handleCancel(action: ActionEvent): Unit = {
    dialogStage.close()
  }

  def handleSaveVisitor(): Unit = {
    if (isInputValid()) {
      _visitor.vFirstName <== vFNameField.text
      _visitor.vLastName <== vLNameField.text
      _visitor.vNRICNo <== vNRICFied.text
      _visitor.vCompany <== vCompanyField.text
      _visitor.vStreetAddress <== vStreetField.text
      _visitor.vCity <== vCityField.text
      _visitor.vState <== vStateField.text
      _visitor.vPostal.value = vPostalField.getText.toInt
      _visitor.vCountry <== vCountryField.text
      _visitor.vContact <== vContactField.text
      _visitor.vEmail <== vEmailField.text

      okClicked = true

      try {
        preparedStatement = connection.prepareStatement(
          "INSERT INTO VISITORS (VISITOR_ID, VISITOR_FIRST_NAME, VISITOR_LAST_NAME, VISITOR_STREET_ADDRESS,VISITOR_CITY,VISITOR_STATE,VISITOR_POSTAL_CODE,VISITOR_PHONE_NUMBER,VISITOR_EMAIL,VISITOR_COMPANY,VISITOR_NRIC,COUNTRY) VALUES (VISITORS_VISITOR_ID_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?)"
        )
        preparedStatement.setString(1, _visitor.vFirstName.value)
        preparedStatement.setString(2, _visitor.vLastName.value)
        preparedStatement.setString(3, _visitor.vStreetAddress.value)
        preparedStatement.setString(4, _visitor.vCity.value)
        preparedStatement.setString(5, _visitor.vState.value)
        preparedStatement.setInt(6, _visitor.vPostal.getValue)
        preparedStatement.setString(7, _visitor.vContact.value)
        preparedStatement.setString(8, _visitor.vEmail.value)
        preparedStatement.setString(9, _visitor.vCompany.value)
        preparedStatement.setString(10, _visitor.vNRICNo.value)
        preparedStatement.setString(11, _visitor.vCountry.value)
      } catch {
        case e: SQLException => println(e.getMessage)
      } finally {
        preparedStatement.execute()
        preparedStatement.close()
        val alert = new Alert(Alert.AlertType.Information) {
          initOwner(dialogStage)
          title = "Add Visitor"
          headerText = "Visitor Added"
          contentText = "Visitor successfully added"
        }.showAndWait()
        dialogStage.close()
        MainApp.showVisitorOverview()
      }
    }
  }

  def handleSaveEditVisitor(): Unit = {
    if (isInputValid()) {
      _visitor.vFirstName <== vFNameField.text
      _visitor.vLastName <== vLNameField.text
      _visitor.vNRICNo <== vNRICFied.text
      _visitor.vCompany <== vCompanyField.text
      _visitor.vStreetAddress <== vStreetField.text
      _visitor.vCity <== vCityField.text
      _visitor.vState <== vStateField.text
      _visitor.vPostal.value = vPostalField.getText.toInt
      _visitor.vCountry <== vCountryField.text
      _visitor.vContact <== vContactField.text
      _visitor.vEmail <== vEmailField.text

      okClicked = true

      try {
        preparedStatement = connection.prepareStatement(
          "UPDATE VISITORS SET VISITOR_FIRST_NAME = ?, VISITOR_LAST_NAME=?, VISITOR_STREET_ADDRESS=?,VISITOR_CITY=?,VISITOR_STATE=?,VISITOR_POSTAL_CODE=?,VISITOR_PHONE_NUMBER=?,VISITOR_EMAIL=?,VISITOR_COMPANY=?,VISITOR_NRIC=?,COUNTRY=? WHERE VISITOR_ID =?"
        )
        preparedStatement.setString(1, _visitor.vFirstName.value)
        preparedStatement.setString(2, _visitor.vLastName.value)
        preparedStatement.setString(3, _visitor.vStreetAddress.value)
        preparedStatement.setString(4, _visitor.vCity.value)
        preparedStatement.setString(5, _visitor.vState.value)
        preparedStatement.setInt(6, _visitor.vPostal.getValue)
        preparedStatement.setString(7, _visitor.vContact.value)
        preparedStatement.setString(8, _visitor.vEmail.value)
        preparedStatement.setString(9, _visitor.vCompany.value)
        preparedStatement.setString(10, _visitor.vNRICNo.value)
        preparedStatement.setString(11, _visitor.vCountry.value)
        preparedStatement.setInt(12, _visitor.vID.value)
      } catch {
        case e: SQLException => println(e.getMessage)
      }
      finally {
        preparedStatement.execute()
        preparedStatement.close()
        val alert = new Alert(Alert.AlertType.Information) {
          initOwner(dialogStage)
          title = "Edit Visitor"
          headerText = "Visitor Edited"
          contentText = "Visitor successfully update"
        }.showAndWait()
        dialogStage.close()
        MainApp.showVisitorOverview()
      }
    }
  }

  def checkNull(x: String) = {
    x == null || x.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessages = ""

    if (checkNull(vFNameField.text.value))
      errorMessages += "Please enter visitor first name ! \n"
    if (checkNull(vLNameField.text.value))
      errorMessages += "Please enter visitor last name ! \n"
    if (checkNull(vNRICFied.text.value))
      errorMessages += "Please enter visitor NRIC/Passport number ! \n"
    if (checkNull(vContactField.text.value))
      errorMessages += "Please enter visitor company ! \n"
    if (checkNull(vStreetField.text.value))
      errorMessages += "Please enter visitor Street Address ! \n"
    if (checkNull(vCityField.text.value))
      errorMessages += "Please enter visitor city ! \n"
    if (checkNull(vStateField.text.value))
      errorMessages += "Please enter visitor state ! \n"
    if (checkNull(vPostalField.text.toString))
      errorMessages += "Please enter visitor postal code ! \n"
    else {
      try {
        Integer.parseInt(vPostalField.getText())
      } catch {
        case e: NumberFormatException =>
          errorMessages += "Not valid postal code, must be number ! \n"
      }
    }
    if (checkNull(vCountryField.text.value))
      errorMessages += "Please enter visitor country ! \n"
    if (checkNull(vContactField.text.value))
      errorMessages += "Please enter visitor cell phone number ! \n"
    if (checkNull(vEmailField.text.value))
      errorMessages += "Please enter visitor email ! \n"
    else{
      val emailValidator:EmailValidator = EmailValidator.getInstance()
      if(!emailValidator.isValid(vEmailField.text.value))
        errorMessages+="Email not valid ! \n"
    }
    if (errorMessages.length == 0) {
      return true
    } else {
      val alert = new Alert(Alert.AlertType.Error) {
        initOwner(dialogStage)
        title = "Invalid Input"
        headerText = "Please correct invalid fields before it can proceed"
        contentText = errorMessages
      }.showAndWait()

      return false;
    }
  }
}
