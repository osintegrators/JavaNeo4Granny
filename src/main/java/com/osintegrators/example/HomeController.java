package com.osintegrators.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	@Autowired
	AddressService addressService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.debug("in home method");

		return "home";
	}
	

	@RequestMapping(value = "/get/{uuid}", method = RequestMethod.GET)
	public @ResponseBody Address get(@PathVariable String uuid) {
	        logger.debug("in get method");
	        try
	        {
	        	Address add = addressService.getAddressById(uuid);
	        	return add;
	        }
	        catch( Exception e )
	        {
	        	e.printStackTrace();
	        	throw e;
	        }
	}

	@RequestMapping( value = "/addFriends/{personUuid}/{newFriendUuid}", method=RequestMethod.POST)
	public @ResponseBody Address addFriends( @PathVariable String personUuid, @PathVariable String newFriendUuid )
	{
		// add the new friendship association
		System.out.println( "addFriends: " + personUuid + " / " + newFriendUuid );
		try
		{
			Address add = addressService.addFriendshipAssociation( personUuid, newFriendUuid );
			return add;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			throw e;
		}
	}

	@RequestMapping(value = "/findFriends/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Address> findFriends(@PathVariable String id) {
	        
			System.out.println("in findFriends method.   id = " + id );
	        
	        List<Address> friends = new ArrayList<Address>();
	        List<Address> tempList = addressService.findFriends(id);
	        
	        if( tempList != null )
	        {
	        	System.out.println( "found " + tempList.size() + " friends for id: " + id );
	        	friends.addAll( tempList );
	        }
	        
	        return friends;
	}
	
	@RequestMapping(value = "/findCandidateFriends/{uuid}", method = RequestMethod.GET)
	public @ResponseBody List<Address> findCandidateFriends(@PathVariable String uuid) {
	        
			System.out.println("in findCandidateFriends method.  Got uuid as \"" + uuid + "\"");
	        
	        List<Address> candidateFriends = new ArrayList<Address>();
	        List<Address> tempList = null;
	        try
	        {
	        	tempList = addressService.findCandidateFriends(uuid);
	        }
	        catch( Exception e )
	        {
	        	e.printStackTrace();
	        	throw e;
	        }
	        
	        if( tempList != null ) {
	        	System.out.println( "adding " + tempList.size() + " candidate friends!" );
	        	candidateFriends.addAll( tempList );
	        }
	        else {
	        	System.out.println( "no candidate friends located!" );
	        }
	        
	        return candidateFriends;
	}	

	
	@RequestMapping(value = "/findSuggestions/{uuid}", method = RequestMethod.GET)
	public @ResponseBody List<String> findSuggestions(@PathVariable String uuid ) {
		
		System.out.println( "findSuggestions: " + uuid );
		List<String> suggestions = new ArrayList<String>();
		
		List<String> tempList = addressService.findSuggestions( uuid );
		
		if( tempList != null )
		{
			suggestions.addAll( tempList );
		}
		
		
		System.out.println( "returning " + suggestions.size() + " suggestions" );
		return suggestions;
		
	}
	
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody Address address) {
	        
			logger.debug("in create method");
	        System.out.println( "received address: " + address.toString());
	        try
	        {
	        	addressService.createAddress(address);
	        }
	        catch( Exception e )
	        {
	        	e.printStackTrace();
	        	throw e;
	        }
	}
	

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody Address address) {
		
	        System.out.println("in update method");
	        System.out.println( "received address: " + address.toString());
	        
	        try
	        {
	        	addressService.updateAddress(address);
	        }
	        catch( Exception e )
	        {
	        	e.printStackTrace();
	        	throw e;
	        }
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestBody Address address) {
	        
			System.out.println("in delete method");
	        System.out.println( "got address as: " + address.toString() );
	        
	        try
	        {
	        	addressService.deleteAddress(address);
	        }
	        catch( Exception e )
	        {
	        	e.printStackTrace();
	        	throw e;
	        }
	}
	
	@RequestMapping(value = "/addresses", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Address> list() {
	        logger.debug("in create method");
	        try
	        {
	        	return addressService.getAllAddresses();
	        }
	        catch( Exception e )
	        {
	        	e.printStackTrace();
	        	throw e;
	        }
	}

}
