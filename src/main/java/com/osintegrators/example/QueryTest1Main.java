package com.osintegrators.example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.ReadableIndex;

public class QueryTest1Main {

	static GraphDatabaseService graphDb;
	
	public static void main(String[] args) throws Exception
	{
        System.out.println( "Starting Graph Database..." );
        graphDb = new GraphDatabaseFactory().
                  newEmbeddedDatabaseBuilder( "/home/prhodes/workspace/granny/neo4j-db" ).
                  setConfig( GraphDatabaseSettings.node_keys_indexable, "name, uuid" ).
                  setConfig( GraphDatabaseSettings.relationship_keys_indexable, "name" ).
                  setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
                  setConfig( GraphDatabaseSettings.relationship_auto_indexing, "true" ).
                  newGraphDatabase();
        
        registerShutdownHook( graphDb );  
		
		
        /*
    		<option value="d70ee717-29ac-4326-9704-1bae1dfade62" name="Sally Sparrow"> … </option>
    		<option value="235c5608-b240-438e-975e-6124e9e047f0" name="Jack Harkness"> … </option>
    		<option value="79202f6e-eb8d-466a-a523-87757e2ad510" name="Rose Tyler"> … </option>
         */
        
        
		
		ExecutionEngine engine = new ExecutionEngine( graphDb );
		
		// find Address nodes that aren't me, and aren't already my friend
		// String findCandidateFriendsQuery = "start n=node(*) MATCH n:Address-[r?:FRIEND]->friend where n.name <> {name} and ( r is null or friend.name <> {name}) return n";
        
		// String findCandidateFriendsQuery = "start n=node(*) match n, (owner)-[r?:FRIEND]-(friend) where ( NOT (id(n) IN [0])) AND n.uuid! <> {uuid} and ( NOT ( id( owner ) IN [0] ) )  and ( NOT ( id( friend ) IN [0] ) )   " 
		// 								+ " and (  ( (owner.uuid <> {uuid} and friend = n) and (friend.uuid <> {uuid} and owner = n) ) or (r is null ) )  return distinct n";
		
		// String findCandidateFriendsQuery = "start n=node(*) match n, (owner)-[r?:FRIEND]-(friend) where ( NOT (id(n) IN [0])) AND n.uuid! <> {uuid} and ( NOT ( id( owner ) IN [0] ) )  and ( NOT ( id( friend ) IN [0] ) )   " 
		//						+ " and (r is null AND n = owner and friend.uuid = {uuid} ) and (r is null AND n = friend and owner.uuid = {uuid} ) or (r is null and (n <> owner and n <> friend)) return n";
		
		// ( (r is null) or ( ( n = friend and owner.uuid <> {uuid} ) or (n = owner and friend.uuid <> {uuid} ) ) )
		
		
		
		String findCandidateFriendsQuery = "start n=node(*), person=node({personNode})  match (n)-[r?:FRIEND]->(person), (person)-[f?:FRIEND]->(n) where NOT (id(n) IN [0]) and (r is null and f is null ) and n <> person return n";

		
		
		Map<String, Object> uuids = new HashMap<String, Object>();
        uuids.put( "uuid", 
        		 //  "235c5608-b240-438e-975e-6124e9e047f0"    // Jack
        		 // "79202f6e-eb8d-466a-a523-87757e2ad510"            // Rose
        		 // "d70ee717-29ac-4326-9704-1bae1dfade62"    // Sally
        		"9d0cd66a-29e7-4fac-850b-de94b2d0a171" // Bracewell
        );
		
		
		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		Node personNode = autoNodeIndex.get("uuid", uuids.get("uuid")).getSingle();
		
		
		Map<String, Object> params = new HashMap<String, Object>();
        params.put( "personNode", personNode );
		
        ExecutionResult result = engine.execute( findCandidateFriendsQuery, params );
        Iterator<Node> candidateFriendNodes = result.columnAs("n");
		
       /*  Iterator<Node> tempNodes = result.columnAs("friend");
        while( tempNodes.hasNext() )
        {
        	Node tempNode = tempNodes.next();
        	if( tempNode == null )
        	{
        		System.out.println( "WTF - how can this be null???");
        		break;
        	}
        	System.out.println( "(temp) name: " + tempNode.getProperty("name"));
        }
        */
        
        while( candidateFriendNodes.hasNext() )
        {
        	
        	
        	Node candidateFriendNode = candidateFriendNodes.next();
        	
        	try
        	{
        		System.out.println( "processing result in candidateFriendNodes: " + candidateFriendNode.getProperty("name"));
        	}
        	catch( Exception e )
        	{}
        }
		

	}

	
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
              System.out.println( "Shutting down Graph Database...");
                graphDb.shutdown();
            }
        } );
    }  

}
