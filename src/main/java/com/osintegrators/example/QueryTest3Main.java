package com.osintegrators.example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.ReadableIndex;

public class QueryTest3Main {

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
		
        // p, length(p), last(p) order by length(p) asc
        // String findFriendsQuery = "start n=node(*) MATCH p = (owner)-[:FRIEND*1..2]-(friend) where ( (n = owner and friend = {userNode} ) OR (n = friend and owner = {userNode})) return distinct p, length(p) order by length(p)";

        String findFriendsQuery = "start n=node(*), person=node({userNode}) MATCH p = (person)-[:FRIEND*1..2]-(friend) return distinct p order by length(p)";

        
		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();		
		
		Map<String, Object> uuids = new HashMap<String, Object>();
        uuids.put( "uuid", 
        		 // "235c5608-b240-438e-975e-6124e9e047f0"    // Jack
        		 // "79202f6e-eb8d-466a-a523-87757e2ad510"           // Rose
        		"d70ee717-29ac-4326-9704-1bae1dfade62"    // Sally 
        );
		
		Node userNode = autoNodeIndex.get("uuid", uuids.get("uuid")).getSingle();
		
		Map<String, Object> params = new HashMap<String, Object>();
        params.put( "userNode", userNode );
		
        ExecutionResult result = engine.execute( findFriendsQuery, params );
        Iterator<Path> friendNodes = result.columnAs("p");
		
        while( friendNodes.hasNext() )
        {
        	
        	
        	Path path = friendNodes.next();
        	
        	System.out.println( "processing result in candidateFriendNodes: " + path.startNode().getProperty("name") + " -- " + path.endNode().getProperty("name") + " (length: " + path.length() + " )");
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
