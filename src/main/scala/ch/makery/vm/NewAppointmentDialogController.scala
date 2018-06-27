package ch.makery.vm

import java.net.ConnectException
import java.sql._
import java.time.LocalDateTime
import javafx.fxml.FXML

import ch.makery.vm.model._
import ch.makery.vm.util.ConnectionUtil

import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import com.jfoenix.controls.{JFXDatePicker, JFXTimePicker}
import com.sun.mail.util.MailConnectException
import org.apache.commons.mail.{DefaultAuthenticator, Email, SimpleEmail}

import scalafx.event.ActionEvent


@sfxml
class NewAppointmentDialogController(
                                      private val visitorTable: TableView[Visitor],
                                      private val vIDColumn: TableColumn[Visitor, Int],
                                      private val vFnameColumn: TableColumn[Visitor, String],
                                      private val vLnameColumn: TableColumn[Visitor, String],
                                      private val vCompanyColumn: TableColumn[Visitor, String],
                                      private val vEmailColumn: TableColumn[Visitor, String],

                                      private val employeeTable: TableView[Employee],
                                      private val eIDColumn: TableColumn[Employee, Int],
                                      private val eFnameColumn: TableColumn[Employee, String],
                                      private val eLnameColumn: TableColumn[Employee, String],
                                      private val eDepartmentColumn: TableColumn[Employee, String],
                                      private val eEmailColumn: TableColumn[Employee, String],

                                      private val aIDLbl: Label,
                                      @FXML
                                      private val aDateField: JFXDatePicker,
                                      @FXML
                                      private val aTimeField: JFXTimePicker,
                                      private val aReasonField: TextField,
                                      private val aCatCB: ComboBox[String],
                                      private val vIDLbl: Label,
                                      private val eIDLbl: Label
                                    ) {

  var dialogStage: Stage = null
  private var _appointment: Appointment = null
  var okClicked = false

  var aCategoryCB = new ObservableBuffer[String]()
  var aCategory = new ObservableBuffer[AppointmentPassCat]()

  val visitorData = new ObservableBuffer[Visitor]()
  val employeeData = new ObservableBuffer[Employee]()

  var connection: Connection = null
  var rs: ResultSet = null
  var preparedStatement: PreparedStatement = null
  var preparedStatementInsert: PreparedStatement = null
  var preparedStatementSend: PreparedStatement = null
  connection = ConnectionUtil.connectiondb()

  var selectedCatID: Int = 0

  def appointment = _appointment

  def appointment_=(z: Appointment): Unit = {

    _appointment = z

    aIDLbl.text = _appointment.aID.value
    aDateField.setValue(_appointment.appointmentDateTime.value.toLocalDate)
  }

  def fillCategoryCB() {
    aCatCB.valueProperty().set(null)
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT CATEGORY_NAME FROM APPOINTMENT_CATEGORY"
      )
      rs = preparedStatement.executeQuery()
    } catch {
      case e: SQLException => null
    }

    while (rs.next()) {
      aCategoryCB += rs.getString(1)
    }

    for (category <- aCategoryCB) {
      aCatCB.+=(category)
    }
  }

  def fillVisitorTable() {
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT * FROM VISITORS"
      )
      rs = preparedStatement.executeQuery()
    } catch {
      case e: SQLException => null
    }

    while (rs.next()) {
      visitorData += new Visitor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(11),
        rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8),
        rs.getString(9), rs.getString(10), rs.getString(12))
    }

    visitorTable.items = visitorData
    vIDColumn.cellValueFactory = {
      _.value.vID
    }
    vFnameColumn.cellValueFactory = {
      _.value.vFirstName
    }
    vLnameColumn.cellValueFactory = {
      _.value.vLastName
    }
    vCompanyColumn.cellValueFactory = {
      _.value.vCompany
    }
    vEmailColumn.cellValueFactory = {
      _.value.vEmail
    }
  }

  def fillEmployeeTable(): Unit = {
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
    eIDColumn.cellValueFactory = {
      _.value.eID
    }
    eFnameColumn.cellValueFactory = {
      _.value.eFname
    }
    eLnameColumn.cellValueFactory = {
      _.value.eLname
    }
    eDepartmentColumn.cellValueFactory = {
      _.value.eDept
    }
    eEmailColumn.cellValueFactory = {
      _.value.eEmail
    }

  }

  import scalafx.Includes._


  selectedVisitor(None)

  visitorTable.selectionModel().selectedItem.onChange(
    (_, _, newValue) => selectedVisitor(Option(newValue))
  )

  private def selectedVisitor(visitor: Option[Visitor]) = {
    visitor match {
      case Some(visitor) =>
        vIDLbl.text.value = visitor.vID.getValue.toString()

      case None =>
        vIDLbl.text = ""
    }
  }

  selectedEmployee(None)

  employeeTable.selectionModel().selectedItem.onChange(
    (_, _, newValue) => selectedEmployee(Option(newValue))
  )


  private def selectedEmployee(employee: Option[Employee]) = {
    employee match {
      case Some(employee) =>
        eIDLbl.text.value = employee.eID.getValue.toString()

      case None =>
        eIDLbl.text = ""
    }
  }

  def checkNull(x: String) = {
    x == null || x.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessages = ""

    if (checkNull(vIDLbl.text.value))
      errorMessages += "Please select a visitor ! \n"

    if (checkNull(eIDLbl.text.value))
      errorMessages += "Please select a employee ! \n"

    if (checkNull(aDateField.getValue.toString))
      errorMessages += "Please select the appointment date ! \n"

    if (checkNull(aTimeField.getValue.toString))
      errorMessages += "Please select the appointment time ! \n"

    if (checkNull(aReasonField.text.value))
      errorMessages += "Please state the reason of visit ! \n"

    if (checkNull(aCatCB.selectionModel().getSelectedItem.toString))
      errorMessages += "Please select the category ! \n"

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

  def handleAddNewAppointment(): Unit = {
    if (isInputValid()) {
      _appointment.aID <== aIDLbl.text
      _appointment.aVID.value = vIDLbl.text.value.toInt
      _appointment.aEID.value = eIDLbl.text.value.toInt
      _appointment.appointmentDateTime.value = LocalDateTime.of(aDateField.getValue, aTimeField.getValue)
      _appointment.aReason <== aReasonField.text
      _appointment.aCategory.value = aCatCB.selectionModel().getSelectedItem.toString
      _appointment.checkInDateTime = null
      _appointment.checkOutDateTime = null

      okClicked = true

      try {
        preparedStatement = connection.prepareStatement(
          "SELECT ACID FROM APPOINTMENT_CATEGORY WHERE CATEGORY_NAME =?"
        )
        preparedStatement.setString(1, aCatCB.selectionModel().getSelectedItem.toString)
      } catch {
        case e: SQLException => null
      } finally {
        rs = preparedStatement.executeQuery()
      }
      if (rs.next())
        selectedCatID = rs.getInt(1)

      try {
        preparedStatementInsert = connection.prepareStatement(
          "INSERT INTO APPOINTMENT(AID,VISITOR_ID,EMPLOYEE_ID,REASON,APPOINT_CATEGORY_ID,APPOINTMENT_DATE_TIME) VALUES (?,?,?,?,?,?)"
        )
        preparedStatementInsert.setString(1, _appointment.aID.value)
        preparedStatementInsert.setInt(2, _appointment.aVID.value)
        preparedStatementInsert.setInt(3, _appointment.aEID.value)
        preparedStatementInsert.setString(4, _appointment.aReason.value)
        preparedStatementInsert.setInt(5, selectedCatID)
        preparedStatementInsert.setTimestamp(6, Timestamp.valueOf(_appointment.appointmentDateTime.value))
      } catch {
        case e: SQLException => null
      } finally {
        preparedStatementInsert.execute()
      }

      try{
        preparedStatementSend = connection.prepareStatement(
          "SELECT APPOINTMENT.*,VISITORS.VISITOR_FIRST_NAME,VISITORS.VISITOR_LAST_NAME,VISITORS.VISITOR_EMAIL, EMPLOYEE.EMPLOYEE_FNAME,EMPLOYEE.EMPLOYEE_LNAME FROM APPOINTMENT,EMPLOYEE,VISITORS WHERE APPOINTMENT.VISITOR_ID = VISITORS.VISITOR_ID AND APPOINTMENT.EMPLOYEE_ID = EMPLOYEE.EMPLOYEE_ID AND AID=?"
        )
        preparedStatementSend.setString(1,_appointment.aID.value)
      }catch {
        case e:SQLException =>
      }finally {
        rs = preparedStatementSend.executeQuery()
      }

      if(rs.next()){
        sendConfirmEmail(rs.getString(1),rs.getString(13)+" " +rs.getString(12),rs.getTimestamp(8).toLocalDateTime,rs.getString(10)+" "+rs.getString(9),rs.getString(11))
      }

      val alert = new Alert(Alert.AlertType.Information) {
        initOwner(dialogStage)
        title = "New Appointment"
        headerText = "Appointment Added"
        contentText = "Successfully schedule appointment"
      }.showAndWait()

      dialogStage.close()
      MainApp.showAppointmentOverview()
    }
  }

  def sendConfirmEmail(id:String,employee:String,appointmentDT:LocalDateTime,visitor:String,vEmail:String): Unit ={
    try {
      val email: Email = new SimpleEmail()
      email.setHostName("smtp.googlemail.com")
      email.setSmtpPort(465)
      email.setAuthenticator(new DefaultAuthenticator("visitormanagemnt@gmail.com", "VisitorManagement@1234"))
      email.setSSLOnConnect(true)
      email.setFrom("visitormanagement@gmail.com");
      email.setSubject("Appointment with " + employee);

      val message = "Dear " + visitor + ", \n\n" +
        "Appointment detail as below:- \n" +
        "Appointment confirmation code: " + id + "\n" +
        "Employee: " + employee + "\n" +
        "Appointment Date Time: " + appointmentDT + "\n\n" +
        "The appointment will hold for 15 minutes only, please be on time. \n\n" +
        "Thank you. \n\n" +
        "Cheers, \n" +
        employee

      email.setMsg(message);
      email.addTo(vEmail);
      email.send();
    }catch {
      case e: ConnectException =>
        val alert = new Alert(Alert.AlertType.Error) {
          initOwner(dialogStage)
          title = "Connection"
          headerText = "No Connection"
          contentText = "Couldn't connect to host"
        }.showAndWait()
      case e: MailConnectException=>
        val alert = new Alert(Alert.AlertType.Error) {
          initOwner(dialogStage)
          title = "Connection"
          headerText = "No Connection"
          contentText = "Couldn't connect to host"
        }.showAndWait()
    }
  }

  def handleAddPassCategory(action: ActionEvent) = {
    aCategory.clear()
    val aCategoryData = new AppointmentPassCat(0,"")
    val okClicked = MainApp.showNewPassCategoryDialog(aCategoryData)
    if (okClicked) {
      aCategory += aCategoryData
      aCatCB.items().removeAll()
      refreshPassCategoryCB()
    }
  }

  def refreshPassCategoryCB(): Unit = {
    aCategoryCB.clear()
    aCatCB.items().removeAll()

    for (category <- aCategoryCB) {
      aCatCB.+=(category)
    }
  }

  def handleCancel(): Unit ={
    dialogStage.close()
  }


}
