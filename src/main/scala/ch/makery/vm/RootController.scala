package ch.makery.vm

import java.io.FileOutputStream
import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}

import ch.makery.vm.util.ConnectionUtil
import ch.makery.vm.util.DateTimeUtil._
import ch.makery.vm.util.DateUtil._
import org.apache.poi.xssf.usermodel.{XSSFRow, XSSFSheet, XSSFWorkbook}

import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Label}
import scalafx.scene.layout.BorderPane
import scalafxml.core.macros.sfxml

@sfxml
class RootController(private val eIDLabel: Label) {

  def getEID(EmployeeID: Int): Unit = {
    eIDLabel.text = EmployeeID.toString
  }

  def ShowEmployeeOverview(actionEvent: ActionEvent): Unit = {
    MainApp.showEmployeeOverview()
  }

  def CloseApp(actionEvent: ActionEvent): Unit = {
    Platform.exit()
    System.exit(0)
  }

  def ShowVisitorOverview(actionEvent: ActionEvent): Unit = {
    MainApp.showVisitorOverview()
  }

  def ShowAppointmentOverview(actionEvent: ActionEvent) = {
    MainApp.showAppointmentOverview()
  }

  def ShowChangePasswordDialog(actionEvent: ActionEvent): Unit = {
    MainApp.showChangePasswordDialog(eIDLabel.text.value.toInt)
  }

  //Export List

  var connection: Connection = null
  var rs:ResultSet = null
  var preparedStatement: PreparedStatement = null

  connection = ConnectionUtil.connectiondb()

  def exportVisitor(): Unit ={
    var wb:XSSFWorkbook = new XSSFWorkbook()
    var sheet:XSSFSheet = wb.createSheet("Visitor Detail")
    var header:XSSFRow = sheet.createRow(0)
    header.createCell(0).setCellValue("ID")
    header.createCell(1).setCellValue("First Name")
    header.createCell(2).setCellValue("Last Name")
    header.createCell(3).setCellValue("NRIC/Passport No")
    header.createCell(4).setCellValue("Street Address")
    header.createCell(5).setCellValue("City")
    header.createCell(6).setCellValue("State")
    header.createCell(7).setCellValue("Postal")
    header.createCell(8).setCellValue("Contact No")
    header.createCell(9).setCellValue("Email")
    header.createCell(10).setCellValue("Company")
    header.createCell(11).setCellValue("Country")

    var index:Int =1
    try {
      preparedStatement = connection.prepareStatement("SELECT * FROM VISITORS")
    }catch {
      case e:SQLException =>
    }finally {
      rs = preparedStatement.executeQuery()
    }

    while(rs.next()){
      val row:XSSFRow = sheet.createRow(index)
      row.createCell(0).setCellValue(rs.getInt(1).toString)
      row.createCell(1).setCellValue(rs.getString(2))
      row.createCell(2).setCellValue(rs.getString(3))
      row.createCell(3).setCellValue(rs.getString(11))
      row.createCell(4).setCellValue(rs.getString(4))
      row.createCell(5).setCellValue(rs.getString(5))
      row.createCell(6).setCellValue(rs.getString(6))
      row.createCell(7).setCellValue(rs.getString(7))
      row.createCell(8).setCellValue(rs.getString(8))
      row.createCell(9).setCellValue(rs.getString(9))
      row.createCell(10).setCellValue(rs.getString(10))
      row.createCell(11).setCellValue(rs.getString(12))
      index += 1
    }

    var fileOut:FileOutputStream = new FileOutputStream("Visitor Detail.xlsx")
    wb.write(fileOut)
    fileOut.close()
    val alert = new Alert(Alert.AlertType.Information) {
      title = "Export"
      headerText = "Export Visitor"
      contentText = "Export successfully"
    }.showAndWait()
  }

  def exportAppointment(): Unit ={
    var wb:XSSFWorkbook = new XSSFWorkbook()
    var sheet:XSSFSheet = wb.createSheet("Appointment Detail")
    var header:XSSFRow = sheet.createRow(0)
    header.createCell(0).setCellValue("ID")
    header.createCell(1).setCellValue("Visitor First Name")
    header.createCell(2).setCellValue("Visitor Last Name")
    header.createCell(3).setCellValue("Visitor Company")
    header.createCell(4).setCellValue("Employee Full Name")
    header.createCell(5).setCellValue("Reason")
    header.createCell(6).setCellValue("Appointment Date time")
    header.createCell(7).setCellValue("Check In Date Time")
    header.createCell(8).setCellValue("Check Out Date Time")
    header.createCell(9).setCellValue("Visitor Pass Category")
    header.createCell(10).setCellValue("Visitor Email")

    var index:Int =1
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT VISITORS.VISITOR_FIRST_NAME,VISITORS.VISITOR_LAST_NAME,VISITORS.VISITOR_COMPANY,VISITORS.VISITOR_EMAIL, EMPLOYEE.EMPLOYEE_FNAME,EMPLOYEE.EMPLOYEE_LNAME,APPOINTMENT_CATEGORY.CATEGORY_NAME,APPOINTMENT.* FROM VISITORS, EMPLOYEE , APPOINTMENT, APPOINTMENT_CATEGORY WHERE APPOINTMENT.VISITOR_ID = VISITORS.VISITOR_ID AND APPOINTMENT.EMPLOYEE_ID = EMPLOYEE.EMPLOYEE_ID AND APPOINTMENT.APPOINT_CATEGORY_ID = APPOINTMENT_CATEGORY.ACID"
      )
    }catch {
      case e:SQLException =>
    }finally {
      rs = preparedStatement.executeQuery()
    }

    while(rs.next()){
      if (rs.getTimestamp(12) == null && rs.getTimestamp(13) == null) {
        val row: XSSFRow = sheet.createRow(index)
        row.createCell(0).setCellValue(rs.getString(8))
        row.createCell(1).setCellValue(rs.getString(1))
        row.createCell(2).setCellValue(rs.getString(2))
        row.createCell(3).setCellValue(rs.getString(3))
        row.createCell(4).setCellValue(rs.getString(6) + " " + rs.getString(5))
        row.createCell(5).setCellValue(rs.getString(11))
        row.createCell(6).setCellValue(rs.getTimestamp(15).toLocalDateTime.asString)
        row.createCell(7).setCellValue("")
        row.createCell(8).setCellValue("")
        row.createCell(9).setCellValue(rs.getString(7))
        row.createCell(10).setCellValue(rs.getString(4))
      }else if(rs.getTimestamp(13) == null){
        val row: XSSFRow = sheet.createRow(index)
        row.createCell(0).setCellValue(rs.getString(8))
        row.createCell(1).setCellValue(rs.getString(1))
        row.createCell(2).setCellValue(rs.getString(2))
        row.createCell(3).setCellValue(rs.getString(3))
        row.createCell(4).setCellValue(rs.getString(6) + " " + rs.getString(5))
        row.createCell(5).setCellValue(rs.getString(11))
        row.createCell(6).setCellValue(rs.getTimestamp(15).toLocalDateTime.asString)
        row.createCell(7).setCellValue(rs.getTimestamp(12).toLocalDateTime.asString)
        row.createCell(8).setCellValue("")
        row.createCell(9).setCellValue(rs.getString(7))
        row.createCell(10).setCellValue(rs.getString(4))
      }else{
        val row: XSSFRow = sheet.createRow(index)
        row.createCell(0).setCellValue(rs.getString(8))
        row.createCell(1).setCellValue(rs.getString(1))
        row.createCell(2).setCellValue(rs.getString(2))
        row.createCell(3).setCellValue(rs.getString(3))
        row.createCell(4).setCellValue(rs.getString(6) + " " + rs.getString(5))
        row.createCell(5).setCellValue(rs.getString(11))
        row.createCell(6).setCellValue(rs.getTimestamp(15).toLocalDateTime.asString)
        row.createCell(7).setCellValue(rs.getTimestamp(12).toLocalDateTime.asString)
        row.createCell(8).setCellValue(rs.getTimestamp(13).toLocalDateTime.asString)
        row.createCell(9).setCellValue(rs.getString(7))
        row.createCell(10).setCellValue(rs.getString(4))
      }
      index += 1
    }

    var fileOut:FileOutputStream = new FileOutputStream("Appointment Detail.xlsx")
    wb.write(fileOut)
    fileOut.close()
    val alert = new Alert(Alert.AlertType.Information) {
      title = "Export"
      headerText = "Export Appointment"
      contentText = "Export successfully"
    }.showAndWait()
  }

  def exportEmployee(): Unit ={
    var wb:XSSFWorkbook = new XSSFWorkbook()
    var sheet:XSSFSheet = wb.createSheet("Employee Detail")
    var header:XSSFRow = sheet.createRow(0)
    header.createCell(0).setCellValue("ID")
    header.createCell(1).setCellValue("First Name")
    header.createCell(2).setCellValue("Last Name")
    header.createCell(3).setCellValue("Email")
    header.createCell(4).setCellValue("Cell Phone")
    header.createCell(5).setCellValue("Street Address")
    header.createCell(6).setCellValue("City")
    header.createCell(7).setCellValue("State")
    header.createCell(8).setCellValue("Postal")
    header.createCell(9).setCellValue("DOB")
    header.createCell(10).setCellValue("Department")
    header.createCell(11).setCellValue("Catrgory")
    header.createCell(12).setCellValue("Work Phone")
    header.createCell(13).setCellValue("Country")
    header.createCell(14).setCellValue("NRIC/Passport No")


    var index:Int =1
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT EMPLOYEE.*, DEPT.DNAME, EMPLOYEE_CATEGORY.CATNAME FROM EMPLOYEE, DEPT, EMPLOYEE_CATEGORY WHERE EMPLOYEE.DEPTNO = DEPT.DEPTNO AND EMPLOYEE.EMPLOYEE_CAT_ID = EMPLOYEE_CATEGORY.CATID"
      )
    }catch {
      case e:SQLException =>
    }finally {
      rs = preparedStatement.executeQuery()
    }

    while(rs.next()){
      val row:XSSFRow = sheet.createRow(index)
      row.createCell(0).setCellValue(rs.getInt(1).toString)
      row.createCell(1).setCellValue(rs.getString(3))
      row.createCell(2).setCellValue(rs.getString(4))
      row.createCell(3).setCellValue(rs.getString(5))
      row.createCell(4).setCellValue(rs.getString(6))
      row.createCell(5).setCellValue(rs.getString(7))
      row.createCell(6).setCellValue(rs.getString(12))
      row.createCell(7).setCellValue(rs.getString(8))
      row.createCell(8).setCellValue(rs.getString(9))
      row.createCell(9).setCellValue(rs.getDate(10).toLocalDate.asString)
      row.createCell(10).setCellValue(rs.getString(17))
      row.createCell(11).setCellValue(rs.getString(18))
      row.createCell(12).setCellValue(rs.getString(14))
      row.createCell(13).setCellValue(rs.getString(15))
      row.createCell(13).setCellValue(rs.getString(16))
      index += 1
    }

    var fileOut:FileOutputStream = new FileOutputStream("Employee Detail.xlsx")
    wb.write(fileOut)
    fileOut.close()
    val alert = new Alert(Alert.AlertType.Information) {
      title = "Export"
      headerText = "Export Employee"
      contentText = "Export successfully"
    }.showAndWait()
  }

  def showEditor(actionEvent: ActionEvent): Unit ={
    MainApp.showEditor()
  }
}
