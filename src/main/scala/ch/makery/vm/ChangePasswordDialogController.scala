package ch.makery.vm

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}

import ch.makery.vm.util.ConnectionUtil

import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Label, PasswordField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class ChangePasswordDialogController(
                                      private val eIDLbl: Label,
                                      private val currentPassField: PasswordField,
                                      private val newPassField: PasswordField,
                                      private val confNewPassField: PasswordField
                                    ) {
  var dialogStage: Stage = null
  var okClicked = false

  var preparedStatement: PreparedStatement = null
  var preparedStatement1: PreparedStatement = null
  var resultSet: ResultSet = null
  var connection: Connection = null
  connection = ConnectionUtil.connectiondb();

  def getEID(eID: Int): Unit = {
    eIDLbl.text = eID.toString
  }

  def checkNull(z: String) = {
    z == null || z.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessage = ""

    if (checkNull(currentPassField.text.value))
      errorMessage += "Please enter current password ! \n"

    if (checkNull(newPassField.text.value))
      errorMessage += "Please enter new password ! \n"

    if (checkNull(confNewPassField.text.value))
      errorMessage += "Please retype new password ! \n"

    if (!newPassField.text.value.equals(confNewPassField.text.value))
      errorMessage += "Password does not match ! \n"
    else {
      var strengthPercentage: Int = 0;
      val partialRegexChecks = scala.Array(
        ".*[a-z]+.*", // lower
        ".*[A-Z]+.*", // upper
        ".*[\\d]+.*", // digits
        ".*[@#$%]+.*" // symbols
      )
      if(!newPassField.text.value.matches(partialRegexChecks(0)))
        errorMessage+="Password must have at leat one lower case ! \n"
      if(!newPassField.text.value.matches(partialRegexChecks(1)))
        errorMessage+="Password must have at leat one upper case ! \n"
      if(!newPassField.text.value.matches(partialRegexChecks(2)))
        errorMessage+="Password must have at leat one digit case ! \n"
      if(!newPassField.text.value.matches(partialRegexChecks(3)))
        errorMessage+="Password must have at leat one symbol case ! \n"
      if(newPassField.text.value.length<8)
        errorMessage+="Password must at least 8 digit"
    }

    if (errorMessage.length == 0) {
      return true
    } else {
      val alert = new Alert(Alert.AlertType.Error) {
        title = "Invalid Input"
        headerText = "Please correct invalid fields before it can proceed"
        contentText = errorMessage
      }.showAndWait()

      return false;
    }
  }

  def changePass(): Unit = {
    if (isInputValid()) {
      try {
        preparedStatement = connection.prepareStatement(
          "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID =? AND PASSWORD=?"
        )
        preparedStatement.setInt(1, eIDLbl.text.value.toInt)
        preparedStatement.setString(2, currentPassField.text.value)
      } catch {
        case e: SQLException => null
      } finally {
        resultSet = preparedStatement.executeQuery()
      }
      if (resultSet.next()) {
        try {
          preparedStatement1 = connection.prepareStatement("UPDATE EMPLOYEE SET PASSWORD=? WHERE EMPLOYEE_ID=?")
          preparedStatement1.setString(1, newPassField.text.value)
          preparedStatement1.setInt(2, eIDLbl.text.value.toInt)
        } catch {
          case e: SQLException =>
        } finally {
          preparedStatement1.execute()
          val alert = new Alert(Alert.AlertType.Information) {
            initOwner(dialogStage)
            title = "Password Change"
            headerText = "Password Updated"
            contentText = "Password successfully updated"
          }.showAndWait()
          dialogStage.close()
        }
      } else {
        val alert = new Alert(Alert.AlertType.Error) {
          initOwner(dialogStage)
          title = "Current Password Not Match"
          headerText = "Current Password Not Match"
          contentText = "Current Password Not Match"
        }.showAndWait()
      }
    }
  }

  def handleCancel(actionEvent: ActionEvent): Unit = {
    dialogStage.close()
  }
}
