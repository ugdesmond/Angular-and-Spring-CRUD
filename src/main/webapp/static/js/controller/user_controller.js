'use strict';

angular.module('myApp').controller('UserController', ['$scope', '$http','UserService', function($scope, $http,UserService) {
	 var REST_SERVICE_URI = 'http://localhost:8080/Spring4MVCAngularJSExample/user/';
 
//    self.submit = submit;
//    self.edit = edit;
//    self.remove = remove;
//    self.reset = reset;


    fetchAllUsers();
    var allUsers=[];

    function fetchAllUsers(){
    	 $http({
    	        method : "GET",
    	        url : REST_SERVICE_URI
    	    }).then(function mySuccess(response){
    	    	  $scope.users = response.data.data;
                  allUsers=response.data.data;
    	    }, function myError(response) {
    	    	console.error('Error while fetching Users');
    	    });
        
    }
   
    function createUser(){
    	var username=$scope.username;
    	var address=$scope.address;
    	var email=$scope.email;
    	 $http({
    	        method : "POST",
    	        url : REST_SERVICE_URI,
    	        data:{'username':username,"address":address,"email":email}
    	        
    	    }).then(function mySuccess(response){
    	    reset();
    	     alert(JSON.stringify(response.data.message));
    	        fetchAllUsers();
    	    }, function myError(response) {
    	      alert( JSON.stringify(response.data.message));
    	    });
    }

  function updateUser(){
   	var id=$scope.id;
    $http({
        method : "PUT",
        url : REST_SERVICE_URI+id,
        data:{'username':$scope.username,"address":$scope.address,"email":$scope.email}
        
    }).then(function mySuccess(response) {
    	
    reset();
     alert(JSON.stringify(response.data.message));
      fetchAllUsers();
    }, function myError(response){
      alert( JSON.stringify(response.data.message));
    });  
  }
  
  
    $scope.deleteUser =function(id){
        $http({
            method : "DELETE",
            url : REST_SERVICE_URI+id
            
        }).then(function mySuccess(response) {
        reset();
      if ("success"== response.data.message){
    	  alert("deleted successfully!");
    	 
    	
      };
        }, function myError(response){
          alert("unable to delete!");
        }); 
        fetchAllUsers();
    }
    
    

    $scope.submit= function(){ 
        if($scope.id==null){
            console.log('Saving New User');
            createUser();
        }else{
            updateUser(); 
        }
     
    }

    
    
    $scope.edit=function(id){
        console.log('id to be edited', id);
        for(var i = 0; i < allUsers.length; i++){
            var userObject=allUsers[i].id;
        	if(userObject === id) {
        		var user = angular.copy(allUsers[i]);
        		$scope.username=user.username;
        		$scope.id=user.id;
        		$scope.email=user.email;
        		$scope.address=user.address;
                break;
            }
        }
    }

   

    function reset(){
    	$scope.username='';
		$scope.id=null;
		$scope.email='';
		$scope.address='';
        $scope.myForm.$setPristine(); //reset Form
    }

}]);
