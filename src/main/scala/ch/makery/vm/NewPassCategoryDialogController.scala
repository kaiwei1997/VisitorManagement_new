package ch.makery.vm

import java.sql.{Connection, PreparedStatement, SQLException}

import ch.makery.vm.model.AppointmentPassCat
import ch.makery.vm.util.ConnectionUtil

import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class NewPassCategoryDialogController(
                                     private val catNameField: TextField
                                     ) {
  var dialogStage: Stage = null
  private var _passCat: AppointmentPassCat = null
  var okClicked = false

  var connection: Connection = null
  var preparedStatement: PreparedStatement = null
  connection = ConnectionUtil.connectiondb();

  def appointmentPass = _passCat

  def appointmentPass_=(x: AppointmentPassCat) {
    _passCat = x
  }

  def checkNull(z: String) = {
    z == null || z.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessage = ""

    if (checkNull(catNameField.text.value)) {
      errorMessage += "Please enter pass category"
    }
    if (errorMessage.length == 0) {
      return true
    } else {
      val alert = new Alert(Alert.AlertType.Error) {
        initOwner(dialogStage)
        title = "Invalid Input"
        headerText = "Please correct invalid fields before it can proceed"
        contentText = errorMessage
      }.showAndWait()

      return false;
    }
  }

  def saveCategory(action:ActionEvent): Unit ={
    if(isInputValid()){
      _passCat.catName <== catNameField.text

      okClicked = true

      try{
        preparedStatement = connection.prepareStatement(
          "INSERT INTO APPOINTMENT_CATEGORY (ACID,CATEGORY_NAME) VALUES (APPOINTMENT_CATEGORY_ACID_SEQ.NEXTVAL,?)"
        )
        preparedStatement.setString(1,catNameField.text.value)
      }catch {
        case e: SQLException =>
      }finally {
        preparedStatement.execute()
        val alert = new Alert(Alert.AlertType.Confirmation) {
          initOwner(dialogStage)
          title = "Successful"
          headerText = "Pass Category Added"
          contentText = "Pass Category Added Successfully"
        }.showAndWait()
        dialogStage.close()
      }
    }
  }

  def handleCancel(): Unit ={
    dialogStage.close()
  }
}
