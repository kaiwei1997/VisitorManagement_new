package ch.makery.vm

import java.net.ConnectException
import java.sql._
import java.time.{Duration, LocalDateTime}

import ch.makery.vm.model.{Appointment, AppointmentDetail}
import ch.makery.vm.util.ConnectionUtil
import ch.makery.vm.util.DateTimeUtil._
import com.sun.mail.util.MailConnectException
import org.apache.commons.mail.{DefaultAuthenticator, Email, SimpleEmail}

import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafxml.core.macros.sfxml

@sfxml
class AppointmentOverviewController(
                                     private val appointmentTable: TableView[AppointmentDetail],
                                     private val aIdColumn: TableColumn[AppointmentDetail, String],
                                     private val fNameColumn: TableColumn[AppointmentDetail, String],
                                     private val lNameColumn: TableColumn[AppointmentDetail, String],
                                     private val companyColumn: TableColumn[AppointmentDetail, String],
                                     private val categoryColumn: TableColumn[AppointmentDetail, String],
                                     private val employeeColumn: TableColumn[AppointmentDetail, String],
                                     private val employeeEmailColumn: TableColumn[AppointmentDetail,String],
                                     private val reasonColumn: TableColumn[AppointmentDetail, String],
                                     private val appointDateTimeColumn: TableColumn[AppointmentDetail, LocalDateTime],
                                     private val checkInDateTimeColumn: TableColumn[AppointmentDetail, LocalDateTime],
                                     private val checkOutDateTimeColumn: TableColumn[AppointmentDetail, LocalDateTime],
                                     private val emailColumn: TableColumn[AppointmentDetail, String],

                                     private val aIdLbl: Label,
                                     private val fNameLbl: Label,
                                     private val lNameLbl: Label,
                                     private val companyLbl: Label,
                                     private val categoryLbl: Label,
                                     private val employeeLbl: Label,
                                     private val reasonLbl: Label,
                                     private val appointDateTimeLbl: Label,
                                     private val checkInDateTimeLbl: Label,
                                     private val checkOutDateTimeLbl: Label,
                                     private val emailLbl: Label,

                                     private val search_Field: TextField
                                   ) {
  val appointmentData = new ObservableBuffer[AppointmentDetail]()
  val newAppointment = new ObservableBuffer[Appointment]()

  var connection: Connection = null
  var rs: ResultSet = null
  var preparedStatement: PreparedStatement = null
  var preparedStatementSend: PreparedStatement = null


  connection = ConnectionUtil.connectiondb()
  try {
    preparedStatement = connection.prepareStatement(
      "SELECT VISITORS.VISITOR_FIRST_NAME,VISITORS.VISITOR_LAST_NAME,VISITORS.VISITOR_COMPANY,VISITORS.VISITOR_EMAIL, EMPLOYEE.EMPLOYEE_FNAME,EMPLOYEE.EMPLOYEE_LNAME,APPOINTMENT_CATEGORY.CATEGORY_NAME,APPOINTMENT.*, EMPLOYEE.EMPLOYEE_EMAIL FROM VISITORS, EMPLOYEE , APPOINTMENT, APPOINTMENT_CATEGORY WHERE APPOINTMENT.VISITOR_ID = VISITORS.VISITOR_ID AND APPOINTMENT.EMPLOYEE_ID = EMPLOYEE.EMPLOYEE_ID AND APPOINTMENT.APPOINT_CATEGORY_ID = APPOINTMENT_CATEGORY.ACID"
    )
    rs = preparedStatement.executeQuery()
  } catch {
    case e: SQLException => null
  }

  while (rs.next()) {
    if (rs.getTimestamp(12) == null && rs.getTimestamp(13) == null) {
      appointmentData += new AppointmentDetail(rs.getString(8), rs.getString(1), rs.getString(2),
        rs.getString(3), rs.getString(6) + " " + rs.getString(5), rs.getString(11),
        rs.getTimestamp(15).toLocalDateTime.asString, "",
        "", rs.getString(7), rs.getString(4))
    } else if (rs.getTimestamp(13) == null) {
      appointmentData += new AppointmentDetail(rs.getString(8), rs.getString(1), rs.getString(2),
        rs.getString(3), rs.getString(6) + " " + rs.getString(5), rs.getString(11),
        rs.getTimestamp(15).toLocalDateTime.asString, rs.getTimestamp(12).toLocalDateTime.asString,
        "", rs.getString(7), rs.getString(4))
    } else {
      appointmentData += new AppointmentDetail(rs.getString(8), rs.getString(1), rs.getString(2),
        rs.getString(3), rs.getString(6) + " " + rs.getString(5), rs.getString(11),
        rs.getTimestamp(15).toLocalDateTime.asString, rs.getTimestamp(12).toLocalDateTime.asString,
        rs.getTimestamp(13).toLocalDateTime.asString, rs.getString(7), rs.getString(4))
    }
  }
  appointmentTable.items = appointmentData
  aIdColumn.cellValueFactory = {
    _.value.aID
  }
  fNameColumn.cellValueFactory = {
    _.value.aVFirstName
  }
  lNameColumn.cellValueFactory = {
    _.value.aVLastName
  }
  companyColumn.cellValueFactory = {
    _.value.aVCompany
  }
  categoryColumn.cellValueFactory = {
    _.value.aCategory
  }
  employeeColumn.cellValueFactory = {
    _.value.eFNName
  }
  reasonColumn.cellValueFactory = {
    _.value.aReason
  }
  appointDateTimeColumn.cellValueFactory = {
    _.value.appointmentDateTime
  }
  checkInDateTimeColumn.cellValueFactory = {
    _.value.checkInDateTime
  }
  checkOutDateTimeColumn.cellValueFactory = {
    _.value.checkOutDateTime
  }
  emailColumn.cellValueFactory = {
    _.value.vEmail
  }

  def randomStringFromCharList(length: Int, chars: Seq[Char]): String = {
    val sb = new StringBuilder
    for (i <- 1 to length) {
      val randomNum = scala.util.Random.nextInt(chars.length)
      sb.append(chars(randomNum))
    }
    sb.toString
  }

  def handleNewAppointment(action: ActionEvent): Unit = {
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    val rID = randomStringFromCharList(8, chars)
    val appointment = new Appointment(rID, null, LocalDateTime.now(), null,null,null)
    val okClicked = MainApp.showNewAppointmentDialog(appointment)
    if (okClicked) {
      newAppointment += appointment
    }
  }

  showAppointmentDetail(None)

  import scalafx.Includes._

  appointmentTable.selectionModel().selectedItem.onChange(
    (_, _, newValue) => showAppointmentDetail(Option(newValue))
  )

  private def showAppointmentDetail(appointment: Option[AppointmentDetail]) = {
    appointment match {
      case Some(appointmentDetail) =>
        aIdLbl.text <== appointmentDetail.aID
        fNameLbl.text <== appointmentDetail.aVFirstName
        lNameLbl.text <== appointmentDetail.aVLastName
        companyLbl.text <== appointmentDetail.aVCompany
        emailLbl.text <== appointmentDetail.vEmail
        categoryLbl.text <== appointmentDetail.aCategory
        employeeLbl.text <== appointmentDetail.eFNName
        reasonLbl.text <== appointmentDetail.aReason
        appointDateTimeLbl.text = appointmentDetail.appointmentDateTime.value.asString
        checkInDateTimeLbl.text = appointmentDetail.checkInDateTime.value.asString
        checkOutDateTimeLbl.text = appointmentDetail.checkOutDateTime.value.asString

      case None =>
        aIdLbl.text = ""
        fNameLbl.text = ""
        lNameLbl.text = ""
        companyLbl.text = ""
        emailLbl.text = ""
        categoryLbl.text = ""
        employeeLbl.text = ""
        reasonLbl.text = ""
        appointDateTimeLbl.text = ""
        checkInDateTimeLbl.text = ""
        checkOutDateTimeLbl.text = ""
    }
  }

  def handleCheckIn(): Unit = {
    val t1: LocalDateTime = appointDateTimeLbl.text.value.parseLocalDateTime
    val duration = Duration.between(t1, LocalDateTime.now()).toMinutes
    val selectedIndex = appointmentTable.selectionModel().selectedIndex.value

    if(selectedIndex >=0) {
      if (checkInDateTimeLbl.text.value.isEmpty) {
        if (duration > 15) {
          val alert = new Alert(Alert.AlertType.Error) {
            title = "Failed"
            headerText = "No Check In Allow"

            contentText = "The time now is over 15 minutes from the appointment time"
          }.showAndWait()
        } else if (duration < -15) {
          val alert = new Alert(Alert.AlertType.Error) {
            title = "Failed"
            headerText = "No Check In Allow"
            contentText = "The time now is more than 15 minutes before from \n the actual appointment time"
          }.showAndWait()
        } else {
          try {
            preparedStatement = connection.prepareStatement(
              "UPDATE APPOINTMENT SET CHECKIN_DATE_TIME=? WHERE AID=?"
            )
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()))
            preparedStatement.setString(2, aIdLbl.text.value)
          } catch {
            case e: SQLException =>
          } finally {
            preparedStatement.execute()
          }

          try{
            preparedStatementSend = connection.prepareStatement(
              "SELECT EMPLOYEE.EMPLOYEE_EMAIL FROM EMPLOYEE,APPOINTMENT WHERE APPOINTMENT.EMPLOYEE_ID = EMPLOYEE.EMPLOYEE_ID AND APPOINTMENT.AID=? "
            )
            preparedStatementSend.setString(1,aIdLbl.text.value)
          }catch {
            case e: SQLException =>
          }finally {
            rs = preparedStatementSend.executeQuery()
          }
          if(rs.next()){
            sendCheckInEmail(fNameLbl.text.value + lNameLbl.text.value , rs.getString(1))
          }

          val alert = new Alert(Alert.AlertType.Information) {
            title = "Success"
            headerText = "Check In "
            contentText = "Check In Successfull"
          }.showAndWait()

          preparedStatement.close()
          MainApp.showAppointmentOverview()
        }
      } else if (!checkInDateTimeLbl.text.value.isEmpty) {
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Failed"
          headerText = "Already check in"
          contentText = "Already check in"
        }.showAndWait()
      } else if (!checkOutDateTimeLbl.text.value.isEmpty) {
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Failed"
          headerText = "Already check out"
          contentText = "Already check out"
        }.showAndWait()
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Appointment Selected"
        contentText = "Please choose the appointment to check in"
      }.showAndWait()
    }
  }

  def sendCheckInEmail(vName:String,eEmail:String): Unit ={
    try{
      val email: Email = new SimpleEmail()
      email.setHostName("smtp.googlemail.com")
      email.setSmtpPort(465)
      email.setAuthenticator(new DefaultAuthenticator("visitormanagemnt@gmail.com","VisitorManagement@1234"))
      email.setSSLOnConnect(true)
      email.setFrom("visitormanagement@gmail.com");
      email.setSubject("Visitor Checked In " + vName);

      val message = "Visitor " + vName + " have check in kindly proceed to lobby to meet your visitor"

      email.setMsg(message);
      email.addTo(eEmail);
      email.send();
    }catch {
      case e: ConnectException =>
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Connection"
          headerText = "No Connection"
          contentText = "Couldn't connect to host"
        }.showAndWait()
      case e: MailConnectException=>
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Connection"
          headerText = "No Connection"
          contentText = "Couldn't connect to host"
        }.showAndWait()
    }
  }

  def handleCheckOut(): Unit = {
    val selectedIndex = appointmentTable.selectionModel().selectedIndex.value
    if(selectedIndex >=0) {
      if (checkInDateTimeLbl.text.value.isEmpty) {
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Failed"
          headerText = "Failed"
          contentText = "Not Check In Yet"
        }.showAndWait()
      } else if (!checkOutDateTimeLbl.text.value.isEmpty) {
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Failed"
          headerText = "Already check out"
          contentText = "Already check out"
        }.showAndWait()
      } else {
        try {
          preparedStatement = connection.prepareStatement(
            "UPDATE APPOINTMENT SET CHECK_OUT_DATE_TIME=? WHERE AID=?"
          )
          preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()))
          preparedStatement.setString(2, aIdLbl.text.value)
        } catch {
          case e: SQLException =>
        } finally {
          preparedStatement.execute()
          val alert = new Alert(Alert.AlertType.Information) {
            title = "Success"
            headerText = "Check Out "
            contentText = "Check Out Successfull"
          }.showAndWait()
          preparedStatement.close()
          MainApp.showAppointmentOverview()
        }
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Appointment Selected"
        contentText = "Please choose the appointment to check out"
      }.showAndWait()
    }
  }

  def handleEditAppointment(): Unit ={
    val selectedIndex = appointmentTable.selectionModel().selectedIndex.value
    if(selectedIndex >=0){
      MainApp.showEditAppointmentDialog(aIdLbl.text.value)
    }else {
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Appointment Selected"
        contentText = "Please choose the appointment from table to reschedule."
      }.showAndWait()
    }
  }

  def handleCancelAppointment(): Unit ={
    val selectedIndex = appointmentTable.selectionModel().selectedIndex.value

    if(selectedIndex >=0) {
      val alertConfirm = new Alert(Alert.AlertType.Confirmation) {
        initOwner(MainApp.stage)
        title = "Confirmation"
        headerText = "Confirm Cancel"
        contentText = "Confirm cancel appointment ?"
      }.showAndWait()
      if (alertConfirm.get.buttonData.isDefaultButton) {
        if (!checkInDateTimeLbl.text.value.isEmpty) {
          val alertConfirm = new Alert(Alert.AlertType.Error) {
            initOwner(MainApp.stage)
            title = "Error"
            headerText = "Error cancel appointment"
            contentText = "This appointment already check in"
          }.showAndWait()
        } else if (!checkOutDateTimeLbl.text.value.isEmpty) {
          val alertConfirm = new Alert(Alert.AlertType.Error) {
            initOwner(MainApp.stage)
            title = "Error"
            headerText = "Error cancel appointment"
            contentText = "This appointment already check out"
          }.showAndWait()
        } else {
          try {
            preparedStatement = connection.prepareStatement(
              "DELETE FROM APPOINTMENT WHERE AID =?"
            )
            preparedStatement.setString(1, aIdLbl.text.value)
          } catch {
            case e: SQLException =>
          } finally {
            preparedStatement.execute()
          }

          try{
            preparedStatementSend = connection.prepareStatement(
              "SELECT APPOINTMENT.*,VISITORS.VISITOR_FIRST_NAME,VISITORS.VISITOR_LAST_NAME,VISITORS.VISITOR_EMAIL, EMPLOYEE.EMPLOYEE_FNAME,EMPLOYEE.EMPLOYEE_LNAME FROM APPOINTMENT,EMPLOYEE,VISITORS WHERE APPOINTMENT.VISITOR_ID = VISITORS.VISITOR_ID AND APPOINTMENT.EMPLOYEE_ID = EMPLOYEE.EMPLOYEE_ID AND AID=?"
            )
            preparedStatementSend.setString(1,aIdLbl.text.value)
          }catch {
            case e:SQLException =>
          }finally {
            rs = preparedStatementSend.executeQuery()
          }

          if(rs.next()){
            sendCancelEmail(rs.getString(1),rs.getString(13)+" " +rs.getString(12),rs.getTimestamp(8).toLocalDateTime,rs.getString(10)+" "+rs.getString(9),rs.getString(11))
          }

          val alert = new Alert(Alert.AlertType.Information) {
            initOwner(MainApp.stage)
            title = "Cancel"
            headerText = "Cancel Appointment"
            contentText = "Appointment have been cancel"
          }.showAndWait()

          appointmentTable.items().remove(selectedIndex)
        }
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Appointment Selected"
        contentText = "Please choose the appointment from table to cancel appointment."
      }.showAndWait()
    }
  }

  def sendCancelEmail(id:String,employee:String,appointmentDT:LocalDateTime,visitor:String,vEmail:String): Unit ={
    try{
      val email: Email = new SimpleEmail()
      email.setHostName("smtp.googlemail.com")
      email.setSmtpPort(465)
      email.setAuthenticator(new DefaultAuthenticator("visitormanagemnt@gmail.com","VisitorManagement@1234"))
      email.setSSLOnConnect(true)
      email.setFrom("visitormanagement@gmail.com");
      email.setSubject("Appointment with " + employee);

      val message ="Dear " + visitor +", \n\n" +
        "Appointment below have been cancel,\n "+
        "Appointment confirmation code: " + id + "\n"+
        "Employee: " + employee + "\n" +
        "Appointment Date Time: " + appointmentDT + "\n\n"+
        "Thank you. \n\n" +
        "Cheers, \n"+
        employee

      email.setMsg(message);
      email.addTo(vEmail);
      email.send();
    }catch {
      case e: ConnectException =>
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Connection"
          headerText = "No Connection"
          contentText = "Couldn't connect to host"
        }.showAndWait()
      case e: MailConnectException=>
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Connection"
          headerText = "No Connection"
          contentText = "Couldn't connect to host"
        }.showAndWait()
    }
  }
}
