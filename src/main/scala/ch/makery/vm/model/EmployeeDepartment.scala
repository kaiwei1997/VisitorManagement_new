package ch.makery.vm.model

import scalafx.beans.property.{ObjectProperty, StringProperty}

class EmployeeDepartment(eDeptIDs:Int,eDepartmentNames: String) {

  var eDeptId = ObjectProperty[Int](eDeptIDs)
  var employeeDepartmentName = StringProperty(eDepartmentNames)
}
