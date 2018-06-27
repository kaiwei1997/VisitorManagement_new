package ch.makery.vm

import java.net.ConnectException
import java.sql._
import java.time.LocalDateTime
import javafx.fxml.FXML

import ch.makery.vm.util.ConnectionUtil
import com.jfoenix.controls.{JFXDatePicker, JFXTimePicker}
import com.sun.mail.util.MailConnectException
import org.apache.commons.mail.{DefaultAuthenticator, Email, SimpleEmail}

import scalafx.scene.control.{Alert, Label}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class EditAppointmentDialogController (
                                        private val aIDLbl: Label,
                                        @FXML
                                      private val aDate: JFXDatePicker,
                                        @FXML
                                      private val aTime: JFXTimePicker
                                      ) {
  var dialogStage: Stage = null
  var okClicked = false

  var connection: Connection = null
  var preparedStatement: PreparedStatement = null
  var preparedStatementSend: PreparedStatement = null
  var rs:ResultSet = null
  connection = ConnectionUtil.connectiondb()

  def getAID(aID: String): Unit = {
    aIDLbl.text = aID
  }

  def checkNull(x: String) = {
    x == null || x.length == 0
  }

  def isInputValid(): Boolean = {
    var errorMessages = ""

    if (checkNull(aDate.getValue.toString))
      errorMessages += "Please select date ! \n"

    if (checkNull(aTime.getValue.toString))
      errorMessages += "Please select time ! \n"

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

  def handleEditAppointment(): Unit ={
    if(isInputValid()){
      try{
        preparedStatement = connection.prepareStatement(
          "UPDATE APPOINTMENT SET APPOINTMENT_DATE_TIME=? WHERE AID =?"
        )
        preparedStatement.setTimestamp(1,Timestamp.valueOf(LocalDateTime.of(aDate.getValue,aTime.getValue)))
        preparedStatement.setString(2,aIDLbl.text.value)
      }catch {
        case e: SQLException =>
      }finally {
        preparedStatement.execute()

        try{
          preparedStatementSend = connection.prepareStatement(
            "SELECT APPOINTMENT.*,VISITORS.VISITOR_FIRST_NAME,VISITORS.VISITOR_LAST_NAME,VISITORS.VISITOR_EMAIL, EMPLOYEE.EMPLOYEE_FNAME,EMPLOYEE.EMPLOYEE_LNAME FROM APPOINTMENT,EMPLOYEE,VISITORS WHERE APPOINTMENT.VISITOR_ID = VISITORS.VISITOR_ID AND APPOINTMENT.EMPLOYEE_ID = EMPLOYEE.EMPLOYEE_ID AND AID=?"
          )
          preparedStatementSend.setString(1,aIDLbl.text.value)
        }catch {
          case e:SQLException =>
        }finally {
          rs = preparedStatementSend.executeQuery()
        }

        if(rs.next()){
          sendResecheduleEmail(rs.getString(1),rs.getString(13)+" " +rs.getString(12),rs.getTimestamp(8).toLocalDateTime,rs.getString(10)+" "+rs.getString(9),rs.getString(11))
        }

        val alert = new Alert(Alert.AlertType.Information) {
          initOwner(dialogStage)
          title = "Appointment Reschedule"
          headerText = "Appointment Reschedule"
          contentText = "Successfully reschedule appointment"
        }.showAndWait()

        dialogStage.close()
        MainApp.showAppointmentOverview()
      }
    }
  }

  def sendResecheduleEmail(id:String,employee:String,appointmentDT:LocalDateTime,visitor:String,vEmail:String): Unit ={
    try{
      val email: Email = new SimpleEmail()
      email.setHostName("smtp.googlemail.com")
      email.setSmtpPort(465)
      email.setAuthenticator(new DefaultAuthenticator("visitormanagemnt@gmail.com","VisitorManagement@1234"))
      email.setSSLOnConnect(true)
      email.setFrom("visitormanagement@gmail.com");
      email.setSubject("Appointment with " + employee);

      val message ="Dear " + visitor +", \n\n" +
        "Appointment have been reschedule, "+
        "appointment detail as below:- \n"+
        "Appointment confirmation code: " + id + "\n"+
        "Employee: " + employee + "\n" +
        "Appointment Date Time: " + appointmentDT + "\n\n"+
        "The appointment will hold for 15 minutes only, please be on time. \n\n" +
        "Thank you. \n\n" +
        "Cheers, \n"+
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

  def handleCancel(): Unit ={
    dialogStage.close()
  }
}
