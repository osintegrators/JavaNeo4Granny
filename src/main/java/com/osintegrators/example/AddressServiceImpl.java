package com.osintegrators.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.tooling.GlobalGraphOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	GraphDatabaseService graphDb;
	
	static Label labelAddress = DynamicLabel.label( "Address" );
	
	
    private static enum RelTypes implements RelationshipType
    {
        FRIEND
    }
	
	public void createAddress(Address address) {
		
		/* this is because the one we receive doesn't include a uuid */
		Address newAddr = new Address();
		newAddr.copyFrom( address );
		
		
		System.out.println( "creating Address with uuid: " + newAddr.getUuid() );
		
		Transaction tx = graphDb.beginTx();
		try
		{
			// Updating operations go here
			Node addressNode = graphDb.createNode(labelAddress);
			
			addressNode.setProperty( "uuid", newAddr.getUuid());
			addressNode.setProperty( "address", newAddr.getAddress());
			addressNode.setProperty( "name", newAddr.getName());
			addressNode.setProperty( "email", newAddr.getEmail());
			addressNode.setProperty( "phone", newAddr.getPhone());
			
			String dob = newAddr.getDob();
			if( dob != null && !dob.isEmpty() )
			{
				addressNode.setProperty( "dob", dob );
			}
			
			String lastPresent = newAddr.getLast_present();
			
			if( lastPresent != null && !lastPresent.isEmpty() )
			{
				addressNode.setProperty( "last_present", lastPresent );
			}
			
		    tx.success();
		}
		finally
		{
			tx.finish();
		}		
	}

	public List<Address> getAllAddresses() {
		
		GlobalGraphOperations ops = GlobalGraphOperations.at(graphDb);
		
		List<Address> allAddresses = new ArrayList<Address>();
		
		ResourceIterable<Node> nodes = ops.getAllNodesWithLabel(labelAddress);
		
		for( Node node : nodes )
		{
			Address addr = buildAddressFromNode(node);
			allAddresses.add(addr);
		}
		
		return allAddresses;
	}


	public void deleteAddress(Address address) {
		
		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();		
		Node addressNode = autoNodeIndex.get("uuid", address.getUuid()).getSingle();
		
		if( addressNode == null )
		{
			System.err.println( "could not look up address to delete it!");
			return;
		}
		
		Transaction tx = graphDb.beginTx();
		try
		{
			addressNode.delete();
			tx.success();
		}
		finally
		{
			tx.finish();
		}
	}

	public Address getAddressById(String uuid) {
		
		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();		
		Node addressNode = autoNodeIndex.get("uuid", uuid).getSingle();

		Address address = buildAddressFromNode( addressNode );
		
		return address;
	}

	
	public void updateAddress(Address address) {
		
		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();		
		Node addressNode = autoNodeIndex.get("uuid", address.getUuid()).getSingle();
		
		Transaction tx = graphDb.beginTx();
		try
		{
			
			addressNode.setProperty( "address", address.getAddress());
			addressNode.setProperty( "name", address.getName());
			addressNode.setProperty( "email", address.getEmail());
			addressNode.setProperty( "phone", address.getPhone());
			
			String dob = address.getDob();
			if( dob != null && !dob.isEmpty() )
			{
				addressNode.setProperty( "dob", dob );
			}
			else
			{
				addressNode.removeProperty("dob");
			}
			
			String lastPresent = address.getLast_present();
			
			if( lastPresent != null && !lastPresent.isEmpty() )
			{
				addressNode.setProperty( "last_present", lastPresent );
			}
			else 
			{
				addressNode.removeProperty("last_present" );
			}
			
			tx.success();
		}
		finally
		{
			tx.finish();
		}
	}

	public Address addFriendshipAssociation(String personUuid, String newFriendUuid) {

		System.out.println("addFriendAssociation");

		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();		
		Node personNode = autoNodeIndex.get("uuid", personUuid).getSingle();
		Node newFriendNode = autoNodeIndex.get("uuid", newFriendUuid).getSingle();
		
		Transaction tx = graphDb.beginTx();
		try
		{
			personNode.createRelationshipTo( newFriendNode, RelTypes.FRIEND );
			tx.success();
		}
		finally
		{
			tx.finish();
		}

		Address person = buildAddressFromNode(personNode);
		return person;
	}

	
	public List<Address> findCandidateFriends(String uuid) {
		
		System.out.println( "in findCandidateFriends method, for uuid: " + uuid );
		
		List<Address> candidateFriends = new ArrayList<Address>();
				
		ExecutionEngine engine = new ExecutionEngine( graphDb );

		String findCandidateFriendsQuery = "start n=node(*), person=node({personNode})  match (n)-[r?:FRIEND]->(person), (person)-[f?:FRIEND]->(n) where NOT (id(n) IN [0]) and (r is null and f is null ) and n <> person return n";

		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		Node personNode = autoNodeIndex.get("uuid", uuid).getSingle();
		
		
		Map<String, Object> params = new HashMap<String, Object>();
        params.put( "personNode", personNode );
		
        ExecutionResult result = engine.execute( findCandidateFriendsQuery, params );
        Iterator<Node> candidateFriendNodes = result.columnAs("n");
		
        while( candidateFriendNodes.hasNext() )
        {
        	System.out.println( "processing result in candidateFriendNodes" );
        	
        	Node candidateFriendNode = candidateFriendNodes.next();
        	Address candidateFriend = buildAddressFromNode(candidateFriendNode);
        	candidateFriends.add( candidateFriend );
        }
		
		return candidateFriends;
	}

	public List<Address> findFriends(String uuid) {

		List<Address> friends = new ArrayList<Address>();

		ExecutionEngine engine = new ExecutionEngine( graphDb );
		
		String findFriendsQuery = "start n=node(*) MATCH (owner)-[:FRIEND]-(friend) where ( (n = owner and friend = {userNode} ) OR (n = friend and owner = {userNode})) return distinct n";

		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();		
		Node userNode = autoNodeIndex.get("uuid", uuid).getSingle();
		
		Map<String, Object> params = new HashMap<String, Object>();
        params.put( "userNode", userNode );
		
        ExecutionResult result = engine.execute( findFriendsQuery, params );
        Iterator<Node> friendNodes = result.columnAs("n");
		
        while( friendNodes.hasNext() )
        {
        	Node friendNode = friendNodes.next();
        	Address friend = buildAddressFromNode(friendNode);
        	
        	System.out.println( "found friend: " + friend.toString());
        	
        	friends.add( friend );
        }
		
		return friends;
	}
	
		
	public List<String> findSuggestions( String uuid ) {
		
		List<String> suggestions = new ArrayList<String>();
	
		ExecutionEngine engine = new ExecutionEngine( graphDb );
		

		// select friends and friends of friends, order by depth of the relationship
        String findFriendsQuery = "start n=node(*), person=node({userNode}) MATCH p = (person)-[:FRIEND*1..2]-(friend) return distinct p order by length(p)";

		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();		
		Node userNode = autoNodeIndex.get("uuid", uuid).getSingle();
		
		Map<String, Object> params = new HashMap<String, Object>();
        params.put( "userNode", userNode );
		
        ExecutionResult result = engine.execute( findFriendsQuery, params );
        Iterator<Path> friendPaths = result.columnAs("p");
		
        while( friendPaths.hasNext() )
        {
        	Path friendPath = friendPaths.next();
        	// we want the END node of the path, which is the nth order friend in question
        	Node friendNode = friendPath.endNode();
        	
        	String lastPresent = (String) friendNode.getProperty("last_present");
        	if( lastPresent != null && !lastPresent.isEmpty() )
        	{
        		suggestions.add( lastPresent + "(" + friendPath.length() + ")");
        	}
        }
		
		return suggestions;
	}
	
	private Address buildAddressFromNode( Node addressNode )
	{
		Address address = new Address( (String)addressNode.getProperty("uuid"));

		address.setAddress((String)addressNode.getProperty("address"));
		address.setDob((String)addressNode.getProperty("dob"));
		address.setEmail((String)addressNode.getProperty("email"));
		address.setName((String)addressNode.getProperty("name"));
		address.setPhone( (String)addressNode.getProperty("phone"));
		address.setLast_present((String)addressNode.getProperty("last_present"));

		return address;
	}

}
