package ch.makery.vm

import java.sql._
import java.util.regex.Pattern

import ch.makery.vm.model.{Employee, EmployeeCategory, EmployeeDepartment}
import ch.makery.vm.util.ConnectionUtil
import ch.makery.vm.util.DateUtil._
import org.apache.commons.mail.Email
import org.apache.commons.validator.routines.EmailValidator

import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class NewEmployeeDialogController(
                                   private val eFirstNameField: TextField,
                                   private val eLastNameField: TextField,
                                   private val eNRICField: TextField,
                                   private val employeeNumberField: TextField,
                                   private val eDOBField: DatePicker,
                                   private val eCategoryField: ComboBox[String],
                                   private var eDepartmentField: ComboBox[String],
                                   private val eStreetAddressField: TextField,
                                   private val eCityField: TextField,
                                   private val eStateField: TextField,
                                   private val ePostalField: TextField,
                                   private val eCountryField: TextField,
                                   private val eCellPhoneField: TextField,
                                   private val eWorkPhoneField: TextField,
                                   private val eEmailField: TextField,
                                   private val eNewPassField: PasswordField,
                                   private val eConfNewPassField: PasswordField
                                 ) {
  var dialogStage: Stage = null
  private var _employee: Employee = null
  var okClicked = false

  var eDepartmentCB = new ObservableBuffer[String]()
  var eCategoryCB = new ObservableBuffer[String]()
  var eDepartment = new ObservableBuffer[EmployeeDepartment]()
  var eCategory = new ObservableBuffer[EmployeeCategory]()

  var connection: Connection = null
  var preparedStatement: PreparedStatement = null
  var rs: ResultSet = null
  connection = ConnectionUtil.connectiondb();

  def fillDepartmentCB() {
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT DNAME FROM DEPT"
      )
      rs = preparedStatement.executeQuery()
    } catch {
      case e: Exception => null
    }
    while (rs.next()) {
      eDepartmentCB += rs.getString(1)
    }

    for (name <- eDepartmentCB) {
      eDepartmentField.+=(name)
    }
  }

  def fillCategoryCB() {
    eCategoryField.valueProperty().set(null)
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT CATNAME FROM EMPLOYEE_CATEGORY"
      )
      rs = null
      rs = preparedStatement.executeQuery()
    } catch {
      case e: Exception => null
    }

    while (rs.next()) {
      eCategoryCB += rs.getString(1)
    }

    for (category <- eCategoryCB) {
      eCategoryField.+=(category)
    }
  }

  def refreshCategoryCB(): Unit = {
    eCategoryField.valueProperty().set(null)

    for (category <- eCategoryCB) {
      eCategoryField.+=(category)
    }
  }

  def refreshDeptCB(): Unit = {
    eDepartmentField.valueProperty().set(null)

    for (dept <- eDepartmentCB) {
      eCategoryField.+=(dept)
    }
  }


  def handleAddEmployeeCategory(action: ActionEvent) = {
    eCategory.clear()
    val eCategoryData = new EmployeeCategory(0,"")
    val okClicked = MainApp.showNewCategoryDialog(eCategoryData)
    if (okClicked) {
      eCategory += eCategoryData
      eCategoryField.valueProperty().set(null)
      refreshCategoryCB()
    }
  }

  def handleAddEmployeeDepartment(action: ActionEvent) = {
    eDepartment.clear()
    val eDeptData = new EmployeeDepartment(0,"")
    val okClicked = MainApp.showNewDepartmentDialog(eDeptData)
    if (okClicked) {
      eDepartment += eDeptData
      eDepartmentField.valueProperty().set(null)
      refreshDeptCB()
    }
  }

  def employee = _employee

  def employee_=(x: Employee) {
    _employee = x

    eFirstNameField.text = _employee.eFname.value
    eLastNameField.text = _employee.eLname.value
    eNRICField.text = _employee.eIcNo.value
    employeeNumberField.text = _employee.eID.value.toString
    eDOBField.value = _employee.eDOB.value
    eDOBField.setPromptText("dd/MM/yyyy")
    eCategoryField.value = _employee.eCat.value
    eDepartmentField.value = _employee.eDept.value
    eStreetAddressField.text = _employee.eStreetAddress.value
    eCityField.text = _employee.eCity.value
    eStateField.text = _employee.eState.value
    ePostalField.text = _employee.ePostal.value.toString
    eCountryField.text = _employee.eCountry.value
    eCellPhoneField.text = _employee.eContact.value
    eWorkPhoneField.text = _employee.eWorkPhone.value
    eEmailField.text = _employee.eEmail.value
  }

  def handleCancel(action: ActionEvent): Unit = {
    dialogStage.close()
  }

  def handleSaveEmployee(action: ActionEvent) = {
    if (isInputValid()) {
      _employee.eFname <== eFirstNameField.text
      _employee.eLname <== eLastNameField.text
      _employee.eIcNo <== eNRICField.text
      _employee.eID.value = employeeNumberField.getText.toInt
      _employee.eDOB.value = eDOBField.value.toString.parseLocalDate
      _employee.eCat.value = eCategoryField.getValue.toString()
      _employee.eDept.value = eDepartmentField.getValue.toString()
      _employee.eStreetAddress <== eStreetAddressField.text
      _employee.eCity <== eCityField.text
      _employee.eState <== eStateField.text
      _employee.ePostal.value = ePostalField.getText.toInt
      _employee.eCountry <== eCountryField.text
      _employee.eContact <== eCellPhoneField.text
      _employee.eWorkPhone <== eWorkPhoneField.text
      _employee.eEmail <== eEmailField.text
      _employee.ePassword <== eNewPassField.text

      okClicked = true

      var preparedStatement2: PreparedStatement = null
      val queryAdd: String = "INSERT INTO EMPLOYEE (EMPLOYEE_ID, EMPLOYEE_FNAME, EMPLOYEE_LNAME, EMPLOYEE_EMAIL, EMPLOYEE_CONTACT, EMPLOYEE_STREET_ADDRESS,EMPLOYEE_STATE, EMPLOYEE_POSTAL_CODE,EMPLOYEE_DOB,DEPTNO,EMPLOYEE_CITY,EMPLOYEE_CAT_ID,EMPLOYEE_WORKPHONE,EMPLOYEE_COUNTRY,EMPLOYEE_NRIC,PASSWORD) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      var preparedStatement3: PreparedStatement = null
      val query2: String = "SELECT CATID FROM EMPLOYEE_CATEGORY WHERE CATNAME =?"

      var preparedStatement4: PreparedStatement = null
      val query1: String = "SELECT DEPTNO FROM DEPT WHERE DNAME =?"

      var resultSet: ResultSet = null

      var eCatID: Int = 0

      var eDeptID: Int = 0

      try {
        preparedStatement3 = connection.prepareStatement(query2)

        //get category ID
        preparedStatement3.setString(1, _employee.eCat.getValue)
        resultSet = preparedStatement3.executeQuery()
        if (resultSet.next()) {
          eCatID = resultSet.getInt(1)
        }
        preparedStatement3.close()
      } catch {
        case e: SQLException =>
      }

      try {
        preparedStatement4 = connection.prepareStatement(query1)
        //get Dept ID
        preparedStatement4.setString(1, _employee.eDept.getValue)
        resultSet = preparedStatement4.executeQuery()
        if (resultSet.next()) {
          eDeptID = resultSet.getInt(1)
        }
        preparedStatement4.close()
      } catch {
        case e: SQLException =>
      }

      try {
        preparedStatement2 = connection.prepareStatement(queryAdd)

        preparedStatement2.setInt(1, _employee.eID.getValue)
        preparedStatement2.setString(2, _employee.eFname.value)
        preparedStatement2.setString(3, _employee.eLname.value)
        preparedStatement2.setString(4, _employee.eEmail.value)
        preparedStatement2.setString(5, _employee.eContact.value)
        preparedStatement2.setString(6, _employee.eStreetAddress.value)
        preparedStatement2.setString(7, _employee.eState.value)
        preparedStatement2.setInt(8, _employee.ePostal.getValue)
        preparedStatement2.setDate(9, java.sql.Date.valueOf(eDOBField.getValue))
        preparedStatement2.setInt(10, eDeptID)
        preparedStatement2.setString(11, _employee.eCity.value)
        preparedStatement2.setInt(12, eCatID)
        preparedStatement2.setString(13, _employee.eWorkPhone.value)
        preparedStatement2.setString(14, _employee.eCountry.value)
        preparedStatement2.setString(15, _employee.eIcNo.value)
        preparedStatement2.setString(16, _employee.ePassword.value)
      } catch {
        case f: SQLIntegrityConstraintViolationException=>
          val alert = new Alert(Alert.AlertType.Error) {
            initOwner(dialogStage)
            title = "Employee ID Duplicate"
            headerText = "The employee id is duplicate"
            contentText = f.getMessage
          }.showAndWait()
      } finally {
        preparedStatement2.execute()
        preparedStatement2.close()
        val alert = new Alert(Alert.AlertType.Information) {
          initOwner(dialogStage)
          title = "Add Employee"
          headerText = "Employee Added"
          contentText = "Employee successfully added"
        }.showAndWait()
      }
      dialogStage.close()
      MainApp.showEmployeeOverview()
    }
  }

  def handleEditEmployee(action: ActionEvent) = {
    if (isEditInputValid()) {
      _employee.eFname <== eFirstNameField.text
      _employee.eLname <== eLastNameField.text
      _employee.eIcNo <== eNRICField.text
      _employee.eDOB.value = eDOBField.value.toString.parseLocalDate
      _employee.eCat.value = eCategoryField.getValue.toString()
      _employee.eDept.value = eDepartmentField.getValue.toString()
      _employee.eStreetAddress <== eStreetAddressField.text
      _employee.eCity <== eCityField.text
      _employee.eState <== eStateField.text
      _employee.ePostal.value = ePostalField.getText.toInt
      _employee.eCountry <== eCountryField.text
      _employee.eContact <== eCellPhoneField.text
      _employee.eWorkPhone <== eWorkPhoneField.text
      _employee.eEmail <== eEmailField.text

      okClicked = true

      var preparedStatement5: PreparedStatement = null
      val queryupdate: String =
        "UPDATE EMPLOYEE SET EMPLOYEE_FNAME = ?, EMPLOYEE_LNAME =?, EMPLOYEE_NRIC=?,EMPLOYEE_DOB=?,DEPTNO=?,EMPLOYEE_CAT_ID=?,EMPLOYEE_STREET_ADDRESS=?,EMPLOYEE_CITY=?,EMPLOYEE_STATE=?,EMPLOYEE_POSTAL_CODE=?,EMPLOYEE_COUNTRY=?,EMPLOYEE_CONTACT=?,EMPLOYEE_WORKPHONE=?,EMPLOYEE_EMAIL=? WHERE EMPLOYEE_ID = ?"

      var preparedStatement3: PreparedStatement = null
      val query2: String = "SELECT CATID FROM EMPLOYEE_CATEGORY WHERE CATNAME =?"

      var preparedStatement4: PreparedStatement = null
      val query1: String = "SELECT DEPTNO FROM DEPT WHERE DNAME =?"

      var resultSet: ResultSet = null

      var eCatID: Int = 0

      var eDeptID: Int = 0

      try {
        preparedStatement3 = connection.prepareStatement(query2)
        //get category ID
        preparedStatement3.setString(1, _employee.eCat.getValue)
        resultSet = preparedStatement3.executeQuery()
        if (resultSet.next()) {
          eCatID = resultSet.getInt(1)
        }
        preparedStatement3.close()
      } catch {
        case e: SQLException =>
      }

      try {
        preparedStatement4 = connection.prepareStatement(query1)
        //get Dept ID
        preparedStatement4.setString(1, _employee.eDept.getValue)
        resultSet = preparedStatement4.executeQuery()
        if (resultSet.next()) {
          eDeptID = resultSet.getInt(1)
        }
        preparedStatement4.close()
      } catch {
        case e: SQLException =>
      }

      try {
        preparedStatement5 = connection.prepareStatement(queryupdate)
        preparedStatement5.setString(1, _employee.eFname.value)
        preparedStatement5.setString(2, _employee.eLname.value)
        preparedStatement5.setString(3, _employee.eIcNo.value)
        preparedStatement5.setDate(4, java.sql.Date.valueOf(eDOBField.getValue))
        preparedStatement5.setInt(5, eDeptID)
        preparedStatement5.setInt(6, eCatID)
        preparedStatement5.setString(7, _employee.eStreetAddress.value)
        preparedStatement5.setString(8, _employee.eCity.value)
        preparedStatement5.setString(9, _employee.eState.value)
        preparedStatement5.setInt(10, _employee.ePostal.value)
        preparedStatement5.setString(11, _employee.eCountry.value)
        preparedStatement5.setString(12, _employee.eContact.value)
        preparedStatement5.setString(13, _employee.eWorkPhone.value)
        preparedStatement5.setString(14, _employee.eEmail.value)
        preparedStatement5.setInt(15, employeeNumberField.getText.toInt)
      } catch {
        case e: SQLException =>

      } finally {
        preparedStatement5.execute()
        preparedStatement5.close()
        val alert = new Alert(Alert.AlertType.Information) {
          initOwner(dialogStage)
          title = "Edit Employee"
          headerText = "Employee Update"
          contentText = "Employee successfully updated"
        }.showAndWait()
      }
      dialogStage.close()
      MainApp.showEmployeeOverview()
    }
  }

  def checkNull(z: String) = {
    z == null || z.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessage = ""

    if (checkNull(eFirstNameField.text.value))
      errorMessage += "Please enter employee first name !\n"
    if (checkNull(eLastNameField.text.value))
      errorMessage += "Please enter employee last name ! \n"
    if (checkNull(employeeNumberField.text.value))
      errorMessage += "Please enter employee ID ! \n"
    else {
      if (employeeNumberField.getLength.<(8))
        errorMessage += "Employee ID must be 8 digit ! \n"
      if (employeeNumberField.getLength.>(8))
        errorMessage += "Employee ID must be 8 digit ! \n"
      try {
        Integer.parseInt(employeeNumberField.getText())
      } catch {
        case e: NumberFormatException =>
          errorMessage += "Not valid employee id (must be integer) \n"
      }
    }
    if (checkNull(eDOBField.value.toString()))
      errorMessage += "Please enter employee birth date ! \n"
    else {
      if (!eDOBField.getValue.asString.isValid)
        errorMessage += "Not valid date format (dd/MM/yyyy) \n"
    }
    if (checkNull(eCategoryField.value.toString()))
      errorMessage += "Please select employee category ! \n"
    if (checkNull(eDepartmentField.value.toString()))
      errorMessage += "Please select employee department ! \n"
    if (checkNull(eStreetAddressField.text.value))
      errorMessage += "please enter employee street address ! \n"
    if (checkNull(eCityField.text.value))
      errorMessage += "Please enter employee city ! \n"
    if (checkNull(eStateField.text.value))
      errorMessage += "Please enter employee state ! \n"
    if (checkNull((ePostalField.text.toString)))
      errorMessage += "Please enter employee postal code! \n"
    else {
      try {
        Integer.parseInt(ePostalField.getText())
      } catch {
        case e: NumberFormatException =>
          errorMessage += "Not valid postal code, must be number ! \n"
      }
    }
    if (checkNull(eCountryField.text.value))
      errorMessage += "Please enter employee country ! \n"
    if (checkNull(eCellPhoneField.text.value))
      errorMessage += "Please enter employee contact number ! \n"
    if (checkNull(eWorkPhoneField.text.value))
      errorMessage += "Please enter employee work phone ! \n"
    if (checkNull(eEmailField.text.value))
      errorMessage += "Please enter employee email ! \n"
    else{
      val emailValidator:EmailValidator = EmailValidator.getInstance()
      if(!emailValidator.isValid(eEmailField.text.value))
        errorMessage+="Email not valid ! \n"
    }
    if (checkNull(eNewPassField.text.value))
      errorMessage += "Please enter password ! \n"
    if (checkNull(eConfNewPassField.text.value))
      errorMessage += "Please enter password again ! \n"
    if (!eNewPassField.text.value.equals(eConfNewPassField.text.value))
      errorMessage += "Password not match ! \n"
    else {
      val partialRegexChecks = scala.Array(
        ".*[a-z]+.*", // lower
        ".*[A-Z]+.*", // upper
        ".*[\\d]+.*", // digits
        ".*[@#$%]+.*" // symbols
      )
      if(!eNewPassField.text.value.matches(partialRegexChecks(0)))
        errorMessage+="Password must have at leat one lower case ! \n"
      if(!eNewPassField.text.value.matches(partialRegexChecks(1)))
        errorMessage+="Password must have at leat one upper case ! \n"
      if(!eNewPassField.text.value.matches(partialRegexChecks(2)))
        errorMessage+="Password must have at leat one digit case ! \n"
      if(!eNewPassField.text.value.matches(partialRegexChecks(3)))
        errorMessage+="Password must have at leat one symbol case ! \n"
      if(eNewPassField.text.value.length<8)
        errorMessage+="Password must at least 8 digit"
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

  def isEditInputValid(): Boolean = {
    var errorMessage = ""

    if (checkNull(eFirstNameField.text.value))
      errorMessage += "Please enter employee first name !\n"
    if (checkNull(eLastNameField.text.value))
      errorMessage += "Please enter employee last name ! \n"
    if (checkNull(eDOBField.value.toString()))
      errorMessage += "Please enter employee birth date ! \n"
    else {
      if (!eDOBField.getValue.asString.isValid)
        errorMessage += "Not valid date format (dd/MM/yyyy) \n"
    }
    if (checkNull(eCategoryField.value.toString()))
      errorMessage += "Please select employee category ! \n"
    if (checkNull(eDepartmentField.value.toString()))
      errorMessage += "Please select employee department ! \n"
    if (checkNull(eStreetAddressField.text.value))
      errorMessage += "please enter employee street address ! \n"
    if (checkNull(eCityField.text.value))
      errorMessage += "Please enter employee city ! \n"
    if (checkNull(eStateField.text.value))
      errorMessage += "Please enter employee state ! \n"
    if (checkNull((ePostalField.text.toString)))
      errorMessage += "Please enter employee postal code! \n"
    else {
      try {
        Integer.parseInt(ePostalField.getText())
      } catch {
        case e: NumberFormatException =>
          errorMessage += "Not valid postal code, must be number ! \n"
      }
    }
    if (checkNull(eCountryField.text.value))
      errorMessage += "Please enter employee country ! \n"
    if (checkNull(eCellPhoneField.text.value))
      errorMessage += "Please enter employee contact number ! \n"
    if (checkNull(eWorkPhoneField.text.value))
      errorMessage += "Please enter employee work phone ! \n"
    if (checkNull(eEmailField.text.value))
      errorMessage += "Please enter employee email ! \n"

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

  fillDepartmentCB()
  fillCategoryCB()
}
