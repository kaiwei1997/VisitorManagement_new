package ch.makery.vm

import java.sql.{Connection, PreparedStatement, SQLException}

import ch.makery.vm.model.EmployeeCategory
import ch.makery.vm.util.ConnectionUtil

import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Button, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class NewEmployeeCategoryDialogController(
                                           private val eCategoryTxt: TextField,
                                           private val addBtn: Button
                                         ) {
  var dialogStage: Stage = null
  private var _employeeCat: EmployeeCategory = null
  var okClicked = false
  var connection: Connection = null
  connection = ConnectionUtil.connectiondb();

  def employeeCategory = _employeeCat

  def checkNull(z: String) = {
    z == null || z.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessage = ""

    if (checkNull(eCategoryTxt.text.value)) {
      errorMessage += "Please enter category"
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

  def employeeCategory_=(x: EmployeeCategory) {
    _employeeCat = x
  }

  def saveEmployeeCategory(action: ActionEvent) = {
    if (isInputValid()) {
      _employeeCat.eCategoryName <== eCategoryTxt.text

      okClicked = true

      val query: String = "INSERT INTO EMPLOYEE_CATEGORY (CATID, CATNAME) VALUES (EMPLOYEECAT_SEQ.NEXTVAL,?)"
      var preparedStatement: PreparedStatement = null

      try {
        preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, _employeeCat.eCategoryName.getValue)
      } catch {
        case e: SQLException =>
      } finally {
        preparedStatement.execute()
        preparedStatement.close()
        val alert = new Alert(Alert.AlertType.Confirmation) {
          initOwner(dialogStage)
          title = "Successful"
          headerText = "Category Added"
          contentText = "Category Added Successfully"
        }.showAndWait()
        dialogStage.close()
      }
    }
  }
}
