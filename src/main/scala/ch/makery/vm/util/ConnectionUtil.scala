package ch.makery.vm.util

import java.sql.{Connection, DriverManager, SQLException}

import ch.makery.vm.MainApp.stage

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object ConnectionUtil {

  def connectiondb(): Connection = {
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver")
      return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "VM", "Admin@123")
    } catch {
      case e: SQLException => null
    }
  }
}
