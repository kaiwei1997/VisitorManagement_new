package ch.makery.vm

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}

import ch.makery.vm.model.{Appointment, AppointmentDetail, Visitor}
import ch.makery.vm.util.ConnectionUtil
import ch.makery.vm.util.DateTimeUtil._

import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Label, TableColumn, TableView}
import scalafx.scene.input.MouseEvent
import scalafxml.core.macros.sfxml

@sfxml
class VisitorOverviewController(
                                 private val visitorTable: TableView[Visitor],
                                 private val vFirstNameColumn: TableColumn[Visitor, String],
                                 private val vIDLbl: Label,
                                 private val vLastNameColumn: TableColumn[Visitor, String],
                                 private val vFirstNameLbl: Label,
                                 private val vLastNameLbl: Label,
                                 private val vNRICLbl: Label,
                                 private val vCompanyLbl: Label,
                                 private val vStreetAddressLbl: Label,
                                 private val vCityLbl: Label,
                                 private val vStateLbl: Label,
                                 private val vPostalLbl: Label,
                                 private val vCountryLbl: Label,
                                 private val vContactLbl: Label,
                                 private val vEmailLbl: Label,
                                 private val VisitHistoryTable: TableView[Appointment],

                                 private val appointmentHistory: TableView[AppointmentDetail],
                                 private val ah_aIDColumn: TableColumn[AppointmentDetail,String],
                                 private val ah_categoryColumn: TableColumn[AppointmentDetail, String],
                                 private val ah_employeeColumn: TableColumn[AppointmentDetail, String],
                                 private val ah_reasonColumn: TableColumn[AppointmentDetail, String],
                                 private val ah_appointDateTimeColumn: TableColumn[AppointmentDetail, String],
                                 private val ah_checkInDateTimeColumn: TableColumn[AppointmentDetail, String],
                                 private val ah_checkOutDateTimeColumn: TableColumn[AppointmentDetail, String],
                               ) {
  val visitorData = new ObservableBuffer[Visitor]()

  var connection: Connection = null
  var preparedStatement: PreparedStatement = null
  var rs: ResultSet = null

  connection = ConnectionUtil.connectiondb()

  try {
    preparedStatement = connection.prepareStatement("SELECT * FROM VISITORS")
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
  vFirstNameColumn.cellValueFactory = {
    _.value.vFirstName
  }
  vLastNameColumn.cellValueFactory = {
    _.value.vLastName
  }

  showVisitorDetail(None)

  import scalafx.Includes._

  visitorTable.selectionModel().selectedItem.onChange(
    (_, _, newValue) => showVisitorDetail(Option(newValue))
  )

  private def showVisitorDetail(visitor: Option[Visitor]): Unit = {
    visitor match {
      case Some(visitor) =>
        vIDLbl.text = visitor.vID.getValue.toString()
        vFirstNameLbl.text <== visitor.vFirstName
        vLastNameLbl.text <== visitor.vLastName
        vNRICLbl.text <== visitor.vNRICNo
        vCompanyLbl.text <== visitor.vCompany
        vStreetAddressLbl.text <== visitor.vStreetAddress
        vCityLbl.text <== visitor.vCity
        vStateLbl.text <== visitor.vState
        vPostalLbl.text = visitor.vPostal.getValue.toString()
        vCountryLbl.text <== visitor.vCountry
        vContactLbl.text <== visitor.vContact
        vEmailLbl.text <== visitor.vEmail

      case None =>
        vIDLbl.text = ""
        vFirstNameLbl.text = ""
        vLastNameLbl.text = ""
        vNRICLbl.text = ""
        vCompanyLbl.text = ""
        vStreetAddressLbl.text = ""
        vCityLbl.text = ""
        vStateLbl.text = ""
        vPostalLbl.text = ""
        vCountryLbl.text = ""
        vContactLbl.text = ""
        vEmailLbl.text = ""
    }
  }

  def handleNewVisitor(action: ActionEvent): Unit = {
    val visitor = new Visitor(0, "", "", "", "", "", "", 0, "", "", "", "")
    val okClicked = MainApp.showNewVisitorDialog(visitor)
    if (okClicked) {
      visitorData += visitor
    }
  }

  def handleEditVisitor(actionEvent: ActionEvent) = {
    val selectedVisitor = visitorTable.selectionModel().selectedItem.value

    if (selectedVisitor != null) {
      val okClicked = MainApp.showEditVisitorDialog(selectedVisitor)

      if (okClicked)
        showVisitorDetail(Some(selectedVisitor))
    } else {
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Visitor Selected"
        contentText = "Please choose the visitor from table to start edit."
      }.showAndWait()
    }
  }

  def handleDeleteVisitor(actionEvent: ActionEvent) = {
    val selectedIndex = visitorTable.selectionModel().selectedIndex.value

    if (selectedIndex >= 0) {
      val alertConfirm = new Alert(Alert.AlertType.Confirmation) {
        initOwner(MainApp.stage)
        title = "Confirmation"
        headerText = "Confirm Remove"
        contentText = "Confirm remove " + vLastNameLbl.text.value + " " + vFirstNameLbl.text.value + " ?"
      }.showAndWait()
      if (alertConfirm.get.buttonData.isDefaultButton) {
        try {
          preparedStatement = connection.prepareStatement(
            "DELETE FROM VISITORS WHERE VISITOR_ID=?"
          )
          preparedStatement.setInt(1, vIDLbl.getText.toInt)
        } catch {
          case e: SQLException =>
        } finally {
          preparedStatement.execute()
          preparedStatement.close()
          val alert = new Alert(Alert.AlertType.Information) {
            initOwner(MainApp.stage)
            title = "Remove"
            headerText = "Remove Visitor"
            contentText = "Visitor have been remove"
          }.showAndWait()
        }
        visitorTable.items().remove(selectedIndex)
      }
    } else {
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Visitor Selected"
        contentText = "Please choose the visitor from table to remove."
      }.showAndWait()
    }
  }

  def showAppointmentHistory(mouseEvent: MouseEvent): Unit ={
    val appointmentHistoryData = new ObservableBuffer[AppointmentDetail]()

    if(mouseEvent.clickCount == 1){
      try{
        preparedStatement = connection.prepareStatement(
          "SELECT VISITORS.VISITOR_FIRST_NAME,VISITORS.VISITOR_LAST_NAME,VISITORS.VISITOR_COMPANY,VISITORS.VISITOR_EMAIL, EMPLOYEE.EMPLOYEE_FNAME,EMPLOYEE.EMPLOYEE_LNAME,APPOINTMENT_CATEGORY.CATEGORY_NAME,APPOINTMENT.* FROM VISITORS, EMPLOYEE , APPOINTMENT, APPOINTMENT_CATEGORY WHERE APPOINTMENT.VISITOR_ID = VISITORS.VISITOR_ID AND APPOINTMENT.EMPLOYEE_ID = EMPLOYEE.EMPLOYEE_ID AND APPOINTMENT.APPOINT_CATEGORY_ID = APPOINTMENT_CATEGORY.ACID AND APPOINTMENT.VISITOR_ID=?"
        )
        preparedStatement.setInt(1,vIDLbl.text.value.toInt)
      }catch {
        case e: SQLException =>
      }finally {
        rs=preparedStatement.executeQuery()
      }

      while (rs.next()){
        if (rs.getTimestamp(12) == null && rs.getTimestamp(13) == null) {
          appointmentHistoryData += new AppointmentDetail(rs.getString(8), rs.getString(1), rs.getString(2),
            rs.getString(3), rs.getString(6) + " " + rs.getString(5), rs.getString(11),
            rs.getTimestamp(15).toLocalDateTime.asString, "",
            "", rs.getString(7), rs.getString(4))
        } else if (rs.getTimestamp(13) == null) {
          appointmentHistoryData += new AppointmentDetail(rs.getString(8), rs.getString(1), rs.getString(2),
            rs.getString(3), rs.getString(6) + " " + rs.getString(5), rs.getString(11),
            rs.getTimestamp(15).toLocalDateTime.asString, rs.getTimestamp(12).toLocalDateTime.asString,
            "", rs.getString(7), rs.getString(4))
        } else {
          appointmentHistoryData += new AppointmentDetail(rs.getString(8), rs.getString(1), rs.getString(2),
            rs.getString(3), rs.getString(6) + " " + rs.getString(5), rs.getString(11),
            rs.getTimestamp(15).toLocalDateTime.asString, rs.getTimestamp(12).toLocalDateTime.asString,
            rs.getTimestamp(13).toLocalDateTime.asString, rs.getString(7), rs.getString(4))
        }
      }

      appointmentHistory.items = appointmentHistoryData

      ah_aIDColumn.cellValueFactory={
        _.value.aID
      }

      ah_categoryColumn.cellValueFactory={
        _.value.aCategory
      }

      ah_employeeColumn.cellValueFactory={
        _.value.eFNName
      }

      ah_reasonColumn.cellValueFactory={
        _.value.aReason
      }

      ah_appointDateTimeColumn.cellValueFactory={
        _.value.appointmentDateTime
      }

      ah_checkInDateTimeColumn.cellValueFactory={
        _.value.checkInDateTime
      }

      ah_checkOutDateTimeColumn.cellValueFactory={
        _.value.checkOutDateTime
      }
    }
  }
}
