package ch.makery.vm

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import java.time.LocalDate

import ch.makery.vm.model.{Employee, EmployeeCategory, EmployeeDepartment}
import ch.makery.vm.util.ConnectionUtil

import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafxml.core.macros.sfxml

@sfxml
class EmployeeOverviewController(
                                  private val employeeTable: TableView[Employee],
                                  private val eidColumn: TableColumn[Employee, Int],
                                  private val eFirstNameColumn: TableColumn[Employee, String],
                                  private val eLastNameColumn: TableColumn[Employee, String],
                                  private val eFirstNameLbl: Label,
                                  private val eLastNameLbl: Label,
                                  private val eNRICLbl: Label,
                                  private val eEmployeeNumberLbl: Label,
                                  private val eDOBLbl: Label,
                                  private val eCategoryLbl: Label,
                                  private val departmentLbl: Label,
                                  private val eStreetAddressLbl: Label,
                                  private val eCityLbl: Label,
                                  private val eStateLbl: Label,
                                  private val ePostalLbl: Label,
                                  private val eCountryLbl: Label,
                                  private val eCellPhoneLbl: Label,
                                  private val eWorkPhoneLbl: Label,
                                  private val eEmailLbl: Label
                                ) {
  val employeeData = new ObservableBuffer[Employee]()
  val eDepartment = new ObservableBuffer[EmployeeDepartment]()
  val eCategory = new ObservableBuffer[EmployeeCategory]()

  var connection: Connection = null
  var rs: ResultSet = null
  var preparedStatement: PreparedStatement = null

  connection = ConnectionUtil.connectiondb()
  try {
    preparedStatement = connection.prepareStatement(
      "SELECT EMPLOYEE.*, DEPT.DNAME, EMPLOYEE_CATEGORY.CATNAME FROM EMPLOYEE, DEPT, EMPLOYEE_CATEGORY WHERE EMPLOYEE.DEPTNO = DEPT.DEPTNO AND EMPLOYEE.EMPLOYEE_CAT_ID = EMPLOYEE_CATEGORY.CATID"
    )
    rs = preparedStatement.executeQuery()
  } catch {
    case e: Exception => null
  }
  while (rs.next()) {
    employeeData += new Employee(rs.getInt(1), rs.getString(2),
      rs.getString(3), rs.getString(4), rs.getString(16), rs.getString(5),
      rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(12), rs.getInt(9), rs.getString(17),
      rs.getString(18), rs.getString(14), rs.getString(15), rs.getDate(10).toLocalDate)
  }

  employeeTable.items = employeeData
  eidColumn.cellValueFactory = {
    _.value.eID
  }
  eFirstNameColumn.cellValueFactory = {
    _.value.eFname
  }
  eLastNameColumn.cellValueFactory = {
    _.value.eLname
  }

  showEmployeeDetail(None)

  import scalafx.Includes._

  employeeTable.selectionModel().selectedItem.onChange(
    (_, _, newValue) => showEmployeeDetail(Option(newValue))
  )

  private def showEmployeeDetail(employee: Option[Employee]) = {
    import ch.makery.vm.util.DateUtil._
    employee match {
      case Some(employee) =>
        eFirstNameLbl.text <== employee.eFname
        eLastNameLbl.text <== employee.eLname
        eNRICLbl.text <== employee.eIcNo
        eEmployeeNumberLbl.text = employee.eID.value.toString
        eDOBLbl.text = employee.eDOB.value.asString
        eCategoryLbl.text <== employee.eCat
        departmentLbl.text <== employee.eDept
        eStreetAddressLbl.text <== employee.eStreetAddress
        eCityLbl.text <== employee.eCity
        eStateLbl.text <== employee.eState
        ePostalLbl.text = employee.ePostal.value.toString
        eCountryLbl.text <== employee.eCountry
        eCellPhoneLbl.text <== employee.eContact
        eWorkPhoneLbl.text <== employee.eWorkPhone
        eEmailLbl.text <== employee.eEmail

      case None =>
        eFirstNameLbl.text = ""
        eLastNameLbl.text = ""
        eNRICLbl.text = ""
        eDOBLbl.text = ""
        eCategoryLbl.text = ""
        departmentLbl.text = ""
        eStreetAddressLbl.text = ""
        eCityLbl.text = ""
        eStateLbl.text = ""
        ePostalLbl.text = ""
        eCountryLbl.text = ""
        eCellPhoneLbl.text = ""
        eWorkPhoneLbl.text = ""
        eEmailLbl.text = ""
    }
  }

  def handleNewEmployee(action: ActionEvent) = {
    val employee = new Employee(0, "", "", "", "", "", "","", "", "", 0, "", "", "", "", LocalDate.now())
    val okClicked = MainApp.showNewEmployeeDialog(employee)
    if (okClicked) {
      employeeData += employee
    }
  }

  def handleEditEmployee(action: ActionEvent) = {
    val selectedEmployee = employeeTable.selectionModel().selectedItem.value
    if (selectedEmployee != null) {
      val okClicked = MainApp.showEditEmployeeDialog(selectedEmployee)

      if (okClicked)
        showEmployeeDetail(Some(selectedEmployee))
    } else {
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Employee Selected"
        contentText = "Please choose the employee from table to start edit."
      }.showAndWait()
    }
  }

  def handleDeleteEmployee(action: ActionEvent) = {
    val selectedIndex = employeeTable.selectionModel().selectedIndex.value

    var preparedStatement: PreparedStatement = null

    if (selectedIndex >= 0) {
      val alertConfirm = new Alert(Alert.AlertType.Confirmation) {
        initOwner(MainApp.stage)
        title = "Confirmation"
        headerText = "Confirm Remove"
        contentText = "Confirm remove " + eLastNameLbl.text.value + " " + eFirstNameLbl.text.value + " ?"
      }.showAndWait()
      if (alertConfirm.get.buttonData.isDefaultButton) {
        try {
          preparedStatement = connection.prepareStatement(
            "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID=?"
          )
          preparedStatement.setInt(1, eEmployeeNumberLbl.getText().toInt)
        } catch {
          case e: SQLException =>
        } finally {
          preparedStatement.execute()
          preparedStatement.close()
          val alert = new Alert(Alert.AlertType.Information) {
            initOwner(MainApp.stage)
            title = "Remove"
            headerText = "Remove Employee"
            contentText = "Employee have been remove"
          }.showAndWait()
        }
        employeeTable.items().remove(selectedIndex)
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Employee Selected"
        contentText = "Please choose the employee from table to remove."
      }.showAndWait()
    }
  }
}

