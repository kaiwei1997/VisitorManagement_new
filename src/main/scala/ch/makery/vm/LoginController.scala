package ch.makery.vm

import java.sql.{Connection, PreparedStatement, ResultSet, SQLNonTransientConnectionException}
import javafx.fxml.FXMLLoader

import ch.makery.vm.MainApp.{getClass, stage}
import ch.makery.vm.util.ConnectionUtil

import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.{Node, Parent, Scene}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, TextField}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafxml.core.macros.sfxml
import javafx.{scene => jfxs}

import scalafx.scene.layout.BorderPane
import scalafx.stage.Stage
import scalafxml.core

@sfxml
class LoginController(
                       private val textEmployeeID: TextField,
                       private val textPass: TextField
                     ) {
  var connection: Connection = null
  var preparedStatement: PreparedStatement = null
  var resultSet: ResultSet = null
  connection = ConnectionUtil.connectiondb();

  def checkNull(z: String) = {
    z == null || z.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessage = ""

    if (checkNull(textEmployeeID.text.value))
      errorMessage += "Please enter employee ID ! \n"
    else {
      try {
        Integer.parseInt(textEmployeeID.getText())
      } catch {
        case e: NumberFormatException =>
          errorMessage += "Employee ID only accept number ! \n"
      }
    }
    if (checkNull(textPass.text.value))
      errorMessage += "Please enter password ! \n"

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

  def handleLogin(action: ActionEvent) = {
    var ID: String = textEmployeeID.getText;
    var pass: String = textPass.getText;

    if (isInputValid()) {
      try {
        preparedStatement = connection.prepareStatement("SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID= ? AND PASSWORD = ?")
        preparedStatement.setString(1, ID)
        preparedStatement.setString(2, pass)
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
          val alert = new Alert(AlertType.Confirmation) {
            initOwner(MainApp.stage)
            title = "Login Success"
            headerText = "Login Success"
            contentText = "WELCOME " + resultSet.getString(4) + " " + resultSet.getString(3)
          }.showAndWait()
          MainApp.showRootLayout(ID.toInt)
        } else {
          val alert = new Alert(AlertType.Error) {
            initOwner(MainApp.stage)
            title = "Login Failed"
            headerText = "Login Failed"
            contentText = "Employee ID or password Incorrect"
          }.showAndWait()
          textPass.clear()
        }
      } catch {
        case e: SQLNonTransientConnectionException => null
      }
    }
  }

}
