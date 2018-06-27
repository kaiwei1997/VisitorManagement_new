package ch.makery.vm

import java.sql.{Connection, PreparedStatement, SQLException}

import ch.makery.vm.model.{EmployeeDepartment}
import ch.makery.vm.util.ConnectionUtil

import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Button, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class NewEmployeeDeptDialogController(
                                       private val eDeptTxt: TextField,
                                       private val addBtn: Button
                                     ) {
  var dialogStage: Stage = null
  private var _employeeDept: EmployeeDepartment = null
  var okClicked = false
  var connection: Connection = null
  connection = ConnectionUtil.connectiondb();

  def employeeDept = _employeeDept

  def checkNull(z: String) = {
    z == null || z.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessage = ""

    if (checkNull(eDeptTxt.text.value)) {
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

  def employeeDept_=(A: EmployeeDepartment) {
    _employeeDept = A
  }

  def saveEmployeeDept(action: ActionEvent) = {
    if (isInputValid()) {
      _employeeDept.employeeDepartmentName <== eDeptTxt.text

      okClicked = true

      val query: String = "INSERT INTO DEPT (DEPTNO, DNAME) VALUES (DEPT_DEPTNO_SEQ.NEXTVAL,?)"
      var preparedStatement: PreparedStatement = null

      try {
        preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, _employeeDept.employeeDepartmentName.getValue)
      } catch {
        case e: SQLException =>
      } finally {
        preparedStatement.execute()
        preparedStatement.close()
        val alert = new Alert(Alert.AlertType.Confirmation) {
          initOwner(dialogStage)
          title = "Successful"
          headerText = "Department Added"
          contentText = "Department Added Successfully"
        }.showAndWait()
        dialogStage.close()
      }
    }
  }
}
