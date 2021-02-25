/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
var ApproveRuleInstanceCtrl = ['$scope', function ($scope) {
  if ($scope.property.value !== undefined && $scope.property.value !== null
    && $scope.property.value.assignment !== undefined
    && $scope.property.value.assignment !== null) {
    $scope.assignment = $scope.property.value.assignment;
  } else {
    $scope.assignment = null;
  }

  //定义查询变量
  //点击查询按钮
  $scope.query = function () {
    return $scope.assignment;
  }

  $scope.save = function () {
    $scope.property.value = {};
    $scope.property.value.assignment = $scope.assignment;
    $scope.property.value.assignee = handleAssignmentInput($scope);
    $scope.setAssignment($scope.property);
    $scope.updatePropertyInModel($scope.property);
  };

  var handleAssignmentInput = function ($scope) {
    var assignee = [];
    if ($scope.assignment) {
      for (var i = 0; i < $scope.assignment.length; i++) {
        assignee[assignee.length] = $scope.assignment[i].assigneeCode;
      }
    }
    return assignee.join(',');
  };
}];