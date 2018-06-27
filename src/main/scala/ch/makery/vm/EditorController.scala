package ch.makery.vm

import java.sql._

import ch.makery.vm.model.{AppointmentPassCat, EmployeeCategory, EmployeeDepartment}
import ch.makery.vm.util.ConnectionUtil

import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class EditorController(
                        private val eCatTbl: TableView[EmployeeCategory],
                        private val eCatIDCol: TableColumn[EmployeeCategory, Int],
                        private val eCatNameCol: TableColumn[EmployeeCategory, String],

                        private val eDeptTbl: TableView[EmployeeDepartment],
                        private val eDeptIDCol: TableColumn[EmployeeDepartment, Int],
                        private val eDeptNameCol: TableColumn[EmployeeDepartment, String],

                        private val pCatTbl: TableView[AppointmentPassCat],
                        private val pCatIDCol: TableColumn[AppointmentPassCat, Int],
                        private val pCatNameCol: TableColumn[AppointmentPassCat, String],

                        private val eCatIdLbl: Label,
                        private val eCatNameField: TextField,

                        private val eDeptIDLbl: Label,
                        private val eDeptNameField: TextField,

                        private val pCatIDLbl: Label,
                        private val pCatNameField: TextField
                      ) {
  val eDepartment = new ObservableBuffer[EmployeeDepartment]()
  val eCategory = new ObservableBuffer[EmployeeCategory]()
  val passCategory = new ObservableBuffer[AppointmentPassCat]()

  var connection: Connection = null
  var rs: ResultSet = null
  var preparedStatement: PreparedStatement = null

  connection = ConnectionUtil.connectiondb()

  def fillDepartmentTable(): Unit = {
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT * FROM DEPT"
      )
    } catch {
      case e: SQLException =>
    } finally {
      rs = preparedStatement.executeQuery()
    }
    while (rs.next()) {
      eDepartment += new EmployeeDepartment(rs.getInt(1), rs.getString(2))
    }
    eDeptTbl.items = eDepartment
    eDeptIDCol.cellValueFactory = {
      _.value.eDeptId
    }
    eDeptNameCol.cellValueFactory = {
      _.value.employeeDepartmentName
    }
  }

  fillDepartmentTable()

  showDepartmentDetail(None)

  eDeptTbl.selectionModel().selectedItem.onChange(
    (_, _, newValue) => showDepartmentDetail(Option(newValue))
  )

  def showDepartmentDetail(eDept: Option[EmployeeDepartment]): Unit = {
    eDept match {
      case Some(eDeptDetail) =>
        eDeptIDLbl.text = eDeptDetail.eDeptId.value.toString
        eDeptNameField.text <== eDeptDetail.employeeDepartmentName

      case None =>
        eDeptIDLbl.text = ""
        eDeptNameField.text = ""
    }
  }

  def handleSaveDepartment(action:ActionEvent): Unit = {
    val dialog = new TextInputDialog() {
      title = "Edit"
      headerText="Edit Department Name"
      contentText = "Department Name"
    }.showAndWait()
    var result: Option[String] = dialog
    val selectedIndex = eDeptTbl.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      if (!result.get.isEmpty) {
        try {
          preparedStatement = connection.prepareStatement(
            "UPDATE DEPT SET DNAME =? WHERE DEPTNO=?"
          )
          preparedStatement.setString(1, dialog.get)
          preparedStatement.setInt(2, eDeptIDLbl.text.value.toInt)
        } catch {
          case e: SQLException =>
        } finally {
          preparedStatement.execute()
          val alert = new Alert(Alert.AlertType.Information) {
            title = "Update"
            headerText = "Update"
            contentText = "Department name updated"
          }.showAndWait()
          fillDepartmentTable()
        }
      }else{
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Failed"
          headerText = "Faild"
          contentText = "Please enter department name"
        }.showAndWait()
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Department Selected"
        contentText = "Please choose the department from table to remove."
      }.showAndWait()
    }
  }

  def handleNewDepartment(actionEvent: ActionEvent): Unit ={
    val dialog = new TextInputDialog() {
      title = "New"
      headerText="New Department"
      contentText = "Department Name"
    }.showAndWait()
    var result: Option[String] = dialog
    if(!result.get.isEmpty){
      try{
        preparedStatement = connection.prepareStatement(
          "INSERT INTO DEPT(DEPTNO,DNAME) VALUES (DEPT_DEPTNO_SEQ.NEXTVAL,?)"
        )
        preparedStatement.setString(1,dialog.get)
      }catch {
        case e: SQLException =>
      }finally {
        preparedStatement.execute()
        val alert = new Alert(Alert.AlertType.Information) {
          title = "Add"
          headerText = "Add"
          contentText = "Department successfully added"
        }.showAndWait()
        eDepartment.clear()
        eDeptTbl.items().removeAll()
        fillDepartmentTable()
      }
    }
  }

  def handleDeleteDept(actionEvent: ActionEvent): Unit ={
    val selectedIndex = eDeptTbl.selectionModel().selectedIndex.value

    if(selectedIndex >= 0){
      val alertConfirm = new Alert(Alert.AlertType.Confirmation) {
        initOwner(MainApp.stage)
        title = "Confirmation"
        headerText = "Confirm Delete"
        contentText = "Confirm delete department ?"
      }.showAndWait()
      if (alertConfirm.get.buttonData.isDefaultButton) {
        try{
          preparedStatement = connection.prepareStatement(
            "DELETE FROM DEPT WHERE DEPTNO =?"
          )
          preparedStatement.setInt(1,eDeptIDLbl.text.value.toInt)
        }catch {
          case e: SQLIntegrityConstraintViolationException =>
            val alert = new Alert(Alert.AlertType.Error) {
              initOwner(MainApp.stage)
              title = "Error"
              headerText = "Error"
              contentText = "This value is used by other cannot be delete"
            }.showAndWait()

          case e: SQLException =>

        } finally {
          preparedStatement.execute()
          val alertConfirm = new Alert(Alert.AlertType.Information) {
            initOwner(MainApp.stage)
            title = "Success"
            headerText = "Success"
            contentText = "Department have been remove"
          }.showAndWait()
          eDeptTbl.items().remove(selectedIndex)
        }
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Department Selected"
        contentText = "Please choose the department from table to remove."
      }.showAndWait()
    }
  }

  def fillCategoryTable(): Unit = {
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT * FROM EMPLOYEE_CATEGORY"
      )
    } catch {
      case e: SQLException =>
    } finally {
      rs = preparedStatement.executeQuery()
    }
    while (rs.next()) {
      eCategory += new EmployeeCategory(rs.getInt(1), rs.getString(2))
    }
    eCatTbl.items = eCategory
    eCatIDCol.cellValueFactory = {
      _.value.eCatID
    }
    eCatNameCol.cellValueFactory = {
      _.value.eCategoryName
    }
  }

  fillCategoryTable()

  showCategoryDetail(None)

  eCatTbl.selectionModel().selectedItem.onChange(
    (_, _, newValue) => showCategoryDetail(Option(newValue))
  )

  def showCategoryDetail(eCat: Option[EmployeeCategory]): Unit = {
    eCat match {
      case Some(eCatDetail) =>
        eCatIdLbl.text = eCatDetail.eCatID.value.toString
        eCatNameField.text <== eCatDetail.eCategoryName

      case None =>
        eCatIdLbl.text = ""
        eCatNameField.text = ""
    }
  }

  def handleSaveCategory(action:ActionEvent): Unit = {
    val dialog = new TextInputDialog() {
      title = "Edit"
      headerText="Edit Category Name"
      contentText = "Category Name"
    }.showAndWait()
    var result: Option[String] = dialog
    val selectedIndex = eCatTbl.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      if (!result.get.isEmpty) {
        try {
          preparedStatement = connection.prepareStatement(
            "UPDATE EMPLOYEE_CATEGORY SET CATNAME =? WHERE CATID=?"
          )
          preparedStatement.setString(1, dialog.get)
          preparedStatement.setInt(2, eCatIdLbl.text.value.toInt)
        } catch {
          case e: SQLException =>
        } finally {
          preparedStatement.execute()
          val alert = new Alert(Alert.AlertType.Information) {
            title = "Update"
            headerText = "Update"
            contentText = "Category name updated"
          }.showAndWait()
          fillCategoryTable()
        }
      }else{
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Failed"
          headerText = "Faild"
          contentText = "Please enter category name"
        }.showAndWait()
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Category Selected"
        contentText = "Please choose the category from table to remove."
      }.showAndWait()
    }
  }

  def handleNewCategory(actionEvent: ActionEvent): Unit ={
    val dialog = new TextInputDialog() {
      title = "New"
      headerText="New Category"
      contentText = "Category Name"
    }.showAndWait()
    var result: Option[String] = dialog
    if(!result.get.isEmpty){
      try{
        preparedStatement = connection.prepareStatement(
          "INSERT INTO EMPLOYEE_CATEGORY(CATID,CATNAME) VALUES (EMPLOYEE_CATEGORY_CATID_SEQ.NEXTVAL,?)"
        )
        preparedStatement.setString(1,dialog.get)
      }catch {
        case e: SQLException =>
      }finally {
        preparedStatement.execute()
        val alert = new Alert(Alert.AlertType.Information) {
          title = "Add"
          headerText = "Add"
          contentText = "Cateory successfully added"
        }.showAndWait()
        eDepartment.clear()
        eDeptTbl.items().removeAll()
        fillCategoryTable()
      }
    }
  }

  def handleDeleteCategory(actionEvent: ActionEvent): Unit ={
    val selectedIndex = eCatTbl.selectionModel().selectedIndex.value

    if(selectedIndex >= 0){
      val alertConfirm = new Alert(Alert.AlertType.Confirmation) {
        initOwner(MainApp.stage)
        title = "Confirmation"
        headerText = "Confirm Delete"
        contentText = "Confirm delete category ?"
      }.showAndWait()
      if (alertConfirm.get.buttonData.isDefaultButton) {
        try{
          preparedStatement = connection.prepareStatement(
            "DELETE FROM EMPLOYEE_CATEGORY WHERE CATID =?"
          )
          preparedStatement.setInt(1,eCatIdLbl.text.value.toInt)
        }catch {
          case e: SQLIntegrityConstraintViolationException =>
            val alert = new Alert(Alert.AlertType.Error) {
              initOwner(MainApp.stage)
              title = "Error"
              headerText = "Error"
              contentText = "This value is used by other cannot be delete"
            }.showAndWait()

          case e: SQLException =>

        } finally {
          preparedStatement.execute()
          val alertConfirm = new Alert(Alert.AlertType.Information) {
            initOwner(MainApp.stage)
            title = "Success"
            headerText = "Success"
            contentText = "Category have been remove"
          }.showAndWait()
          eCatTbl.items().remove(selectedIndex)
        }
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Category Selected"
        contentText = "Please choose the category from table to remove."
      }.showAndWait()
    }
  }

  def fillPassCategoryTable(): Unit = {
    try {
      preparedStatement = connection.prepareStatement(
        "SELECT * FROM APPOINTMENT_CATEGORY"
      )
    } catch {
      case e: SQLException =>
    } finally {
      rs = preparedStatement.executeQuery()
    }
    while (rs.next()) {
      passCategory += new AppointmentPassCat(rs.getInt(1), rs.getString(2))
    }
    pCatTbl.items = passCategory
    pCatIDCol.cellValueFactory = {
      _.value.catID
    }
    pCatNameCol.cellValueFactory = {
      _.value.catName
    }
  }

  fillPassCategoryTable()

  showPassCategoryDetail(None)

  pCatTbl.selectionModel().selectedItem.onChange(
    (_, _, newValue) => showPassCategoryDetail(Option(newValue))
  )

  def showPassCategoryDetail(pCat: Option[AppointmentPassCat]): Unit = {
    pCat match {
      case Some(pCatDetail) =>
        pCatIDLbl.text = pCatDetail.catID.value.toString
        pCatNameField.text <== pCatDetail.catName

      case None =>
        pCatIDLbl.text = ""
        pCatNameField.text = ""
    }
  }

  def handleSavePassCategory(action:ActionEvent): Unit = {
    val dialog = new TextInputDialog() {
      title = "Edit"
      headerText="Edit Pass Category Name"
      contentText = "Pass Category Name"
    }.showAndWait()
    var result: Option[String] = dialog
    val selectedIndex = eCatTbl.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      if (!result.get.isEmpty) {
        try {
          preparedStatement = connection.prepareStatement(
            "UPDATE APPOINTMENT_CATEGORY SET CATEGORY_NAME =? WHERE ACID=?"
          )
          preparedStatement.setString(1, dialog.get)
          preparedStatement.setInt(2, pCatIDLbl.text.value.toInt)
        } catch {
          case e: SQLException =>
        } finally {
          preparedStatement.execute()
          val alert = new Alert(Alert.AlertType.Information) {
            title = "Update"
            headerText = "Update"
            contentText = "Pass Category name updated"
          }.showAndWait()
          fillPassCategoryTable()
        }
      }else{
        val alert = new Alert(Alert.AlertType.Error) {
          title = "Failed"
          headerText = "Faild"
          contentText = "Please enter pass category name"
        }.showAndWait()
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Category Selected"
        contentText = "Please choose the pass category from table to remove."
      }.showAndWait()
    }
  }

  def handleNewPassCategory(actionEvent: ActionEvent): Unit ={
    val dialog = new TextInputDialog() {
      title = "New"
      headerText="New Pass Category"
      contentText = "Pass Category Name"
    }.showAndWait()
    var result: Option[String] = dialog
    if(!result.get.isEmpty){
      try{
        preparedStatement = connection.prepareStatement(
          "INSERT INTO APPOINTMENT_CATEGORY(ACID,CATEGORY_NAME) VALUES (APPOINTMENT_CATEGORY_ACID_SEQ.NEXTVAL,?)"
        )
        preparedStatement.setString(1,dialog.get)
      }catch {
        case e: SQLException =>
      }finally {
        preparedStatement.execute()
        val alert = new Alert(Alert.AlertType.Information) {
          title = "Add"
          headerText = "Add"
          contentText = "Pass Cateory successfully added"
        }.showAndWait()
        passCategory.clear()
        pCatTbl.items().removeAll()
        fillPassCategoryTable()
      }
    }
  }

  def handleDeletePassCategory(actionEvent: ActionEvent): Unit ={
    val selectedIndex = pCatTbl.selectionModel().selectedIndex.value

    if(selectedIndex >= 0){
      val alertConfirm = new Alert(Alert.AlertType.Confirmation) {
        initOwner(MainApp.stage)
        title = "Confirmation"
        headerText = "Confirm Delete"
        contentText = "Confirm delete pass category ?"
      }.showAndWait()
      if (alertConfirm.get.buttonData.isDefaultButton) {
        try{
          preparedStatement = connection.prepareStatement(
            "DELETE FROM APPOINTMENT_CATEGORY WHERE ACID =?"
          )
          preparedStatement.setInt(1,pCatIDLbl.text.value.toInt)
        }catch {
          case e: SQLIntegrityConstraintViolationException =>
            val alert = new Alert(Alert.AlertType.Error) {
              initOwner(MainApp.stage)
              title = "Error"
              headerText = "Error"
              contentText = "This value is used by other cannot be delete"
            }.showAndWait()

          case e: SQLException =>

        } finally {
          preparedStatement.execute()
          val alertConfirm = new Alert(Alert.AlertType.Information) {
            initOwner(MainApp.stage)
            title = "Success"
            headerText = "Success"
            contentText = "Pass Category have been remove"
          }.showAndWait()
          pCatTbl.items().remove(selectedIndex)
        }
      }
    }else{
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Pass Category Selected"
        contentText = "Please choose the pass category from table to remove."
      }.showAndWait()
    }
  }
}
