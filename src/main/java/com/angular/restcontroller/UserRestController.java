package com.angular.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.angular.buisnesslogic.UserLogic;
import com.angular.model.ApiMessage;
import com.angular.model.User;
import com.angular.model.UserApi;





@RestController
public class UserRestController {

    @Autowired
    UserLogic userLogic ;  //Service which will do all data retrieval/manipulation work
 
    
    //-------------------Retrieve All Users--------------------------------------------------------
     
    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<UserApi> listAllUsers() {
        List<User> users = userLogic.getAll();
        if(users.isEmpty()){
        	 UserApi userApi= new UserApi();
             userApi.setData(users);
             userApi.setStatus(HttpStatus.NO_CONTENT.toString());
             userApi.setMessage("something went right");
             userApi.setCode(HttpStatus.NO_CONTENT.toString());
            return new ResponseEntity<UserApi>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        UserApi userApi= new UserApi();
        userApi.setData(users);
        userApi.setStatus("success");
        userApi.setMessage("something went right");
        userApi.setCode(HttpStatus.OK.toString());
        return new ResponseEntity<UserApi>(userApi, HttpStatus.OK);
    }
 
 
    
    //-------------------Retrieve Single User--------------------------------------------------------
     
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        System.out.println("Fetching User with id " + id);
        User user = userLogic.getById(id);
        if (user == null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
 
     
     
    //-------------------Create a User--------------------------------------------------------
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<ApiMessage> createUser(@RequestBody User user,    UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + user.getUsername());
 
        if (userLogic.checkIfUserExist(user)) {
        	ApiMessage errorMessage= new ApiMessage();
        	errorMessage.setMessage("User already exist!");
        	errorMessage.setStatus(HttpStatus.CONFLICT.toString());
            System.out.println("A User with name " + user.getUsername() + " already exist");
            return new ResponseEntity<ApiMessage>(errorMessage,HttpStatus.CONFLICT);
        }
        userLogic.create(user);
       
        ApiMessage message  =new ApiMessage();
        message.setMessage("created successfully");
        message.setStatus(HttpStatus.OK.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<ApiMessage>(message,headers,HttpStatus.OK);
    }
 
    
     
    //------------------- Update a User --------------------------------------------------------
    
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ApiMessage> updateUser(@PathVariable long id, @RequestBody User user){
        User currentUser =null;
        ApiMessage errorMessage= new ApiMessage();
		try {
			System.out.println("Updating User " + id);
			currentUser = userLogic.getById(id);
			if (currentUser==null){
	        	errorMessage.setMessage("User doesnot  exist!");
	        	errorMessage.setStatus(HttpStatus.CONFLICT.toString());
			    System.out.println("User with id " + id + " not found");
			    return new ResponseEntity<ApiMessage>(HttpStatus.NOT_FOUND);
			}
			user.setId(id);
			userLogic.update(user);
        	errorMessage.setMessage("Update successful");
        	errorMessage.setStatus(HttpStatus.OK.toString());
		    System.out.println("User with id " + id + " not found");
			System.out.println("Update is successful============");
		} catch (Exception e) {
			e.printStackTrace();
		}
        return new ResponseEntity<ApiMessage>(errorMessage, HttpStatus.OK);
    }
 
    
    
    //------------------- Delete a User --------------------------------------------------------
     
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiMessage> deleteUser(@PathVariable long id) {
        System.out.println("Fetching & Deleting User with id " + id);
 
        User user =  userLogic.getById(id);
        System.out.println("user id is=============="+user.getId());
        if (user == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<ApiMessage>(HttpStatus.NOT_FOUND);
        }
        userLogic.delete(user);
        ApiMessage message  =new ApiMessage();
        message.setMessage("success");
        message.setStatus(HttpStatus.OK.toString());
        return new ResponseEntity<ApiMessage>(message,HttpStatus.OK);
    }
 
     
    
    //------------------- Delete All Users...cant delete all usere na............... --------------------------------------------------------
     
    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers() {
        System.out.println("Deleting All Users");
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
 
}
