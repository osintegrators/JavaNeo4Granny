package com.osintegrators.example;

import java.util.List;

public interface AddressService {

	void createAddress(Address add);

	void deleteAddress(Address add);

	List<Address> getAllAddresses();

	Address getAddressById(String id);

	void updateAddress(Address address);

	Address addFriendshipAssociation( String personUuid, String newFriendUuid);
	
	List<Address> findFriends(String uuid);

	List<Address> findCandidateFriends( String name );

	List<String> findSuggestions(String uuid);
}
