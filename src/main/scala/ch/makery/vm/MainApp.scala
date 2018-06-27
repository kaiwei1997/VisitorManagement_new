package ch.makery.vm

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}

import ch.makery.vm.model._

import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}


object MainApp extends JFXApp {
  def showLogin()= {
    val rootResource = getClass.getResource("view/LoginDialog.fxml")
    val loader = new FXMLLoader(rootResource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]

    stage = new PrimaryStage {
      title = "Visitor Management System Login"
      icons += new Image("file:Image/Logo.png")
      scene = new Scene {
        root = roots
      }
    }
  }

  //variable declare for root layout
  val rootResource1 = getClass.getResource("view/RootLayout.fxml")
  val loader1 = new FXMLLoader(rootResource1, NoDependencyResolver)
  loader1.load()
  val roots1 = loader1.getRoot[jfxs.layout.BorderPane]

  def showRootLayout(eID: Int) = {

    stage = new PrimaryStage {
      title = "Visitor Management"
      icons += new Image("file:Image/Logo.png")
      scene = new Scene {
        root = roots1
      }
      val control = loader1.getController[RootController#Controller]
      control.getEID(eID)
    }
  }


  def showEmployeeOverview() = {
    val resource = getClass.getResource("view/EmployeeOverview.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots1.setCenter(roots)
  }

  def showNewEmployeeDialog(employee: Employee) = {
    val resource = getClass.getResourceAsStream("view/NewEmployeeDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NewEmployeeDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        title = "New Employee"
      }
    }
    control.dialogStage = dialog
    control.employee = employee
    dialog.showAndWait()
    control.okClicked
  }

  def showEditEmployeeDialog(employee: Employee) = {
    val resource = getClass.getResourceAsStream("view/EditEmployeeDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NewEmployeeDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        title = "Edit Employee"
      }
    }
    control.dialogStage = dialog
    control.employee = employee
    dialog.showAndWait()
    control.okClicked
  }

  def showNewCategoryDialog(eCategoryData: EmployeeCategory) = {
    val resource = getClass.getResourceAsStream("view/NewEmployeeCategoryDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NewEmployeeCategoryDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = root2
        title = "New Employee Category"
      }
    }
    control.dialogStage = dialog
    control.employeeCategory = eCategoryData
    dialog.showAndWait()
    control.okClicked
  }

  def showNewDepartmentDialog(eDeptData: EmployeeDepartment) = {
    val resource = getClass.getResourceAsStream("view/NewEmployeeDepartmentDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NewEmployeeDeptDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = root2
        title = "New Employee Department"
      }
    }
    control.dialogStage = dialog
    control.employeeDept = eDeptData
    dialog.showAndWait()
    control.okClicked
  }

  def showVisitorOverview() = {
    val resource = getClass.getResource("view/VisitorOverview.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots1.setCenter(roots)
  }

  def showNewVisitorDialog(visitor: Visitor) = {
    val resource = getClass.getResourceAsStream("view/NewVisitorDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NewVisitorDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        title = "New Visitor"
      }
    }
    control.dialogStage = dialog
    control.visitor = visitor
    dialog.showAndWait()
    control.okClicked
  }

  def showEditVisitorDialog(visitor: Visitor) = {
    val resource = getClass.getResourceAsStream("view/EditVisitorDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NewVisitorDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        title = "New Visitor"
      }
    }
    control.dialogStage = dialog
    control.visitor = visitor
    dialog.showAndWait()
    control.okClicked
  }

  def showAppointmentOverview() = {
    val resource = getClass.getResource("view/AppointmentOverview.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots1.setCenter(roots)
  }

  def showNewAppointmentDialog(appointment: Appointment) = {
    val resource = getClass.getResourceAsStream("view/NewAppointmentDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NewAppointmentDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        title = "New Appointment"
      }
    }
    control.dialogStage = dialog
    control.appointment = appointment
    control.fillCategoryCB()
    control.fillVisitorTable()
    control.fillEmployeeTable()
    dialog.showAndWait()
    control.okClicked
  }

  def showEditAppointmentDialog(aID: String) = {
    val resource = getClass.getResourceAsStream("view/EditAppointmentDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[EditAppointmentDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        title = "Reschedule"
      }
    }
    control.getAID(aID)
    control.dialogStage = dialog
    dialog.showAndWait()
    control.okClicked
  }

  def showNewPassCategoryDialog(passCategoryData: AppointmentPassCat) = {
    val resource = getClass.getResourceAsStream("view/NewPassCategoryDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NewPassCategoryDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = root2
        title = "New Appointment Pass Category"
      }
    }
    control.dialogStage = dialog
    control.appointmentPass = passCategoryData
    dialog.showAndWait()
    control.okClicked
  }

  def showChangePasswordDialog(eID: Int) = {
    val resource = getClass.getResourceAsStream("view/ChangePasswordDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[ChangePasswordDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        title = "Change Password"
      }
    }
    control.getEID(eID)
    control.dialogStage = dialog
    dialog.showAndWait()
    control.okClicked
  }

  def showEditor()={
      val resource = getClass.getResource("view/Editor.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots1.setCenter(roots)
    }
  showLogin()
  showAppointmentOverview()
}
