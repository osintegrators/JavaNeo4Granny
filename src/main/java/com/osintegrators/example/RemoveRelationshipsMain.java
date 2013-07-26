package com.osintegrators.example;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.ReadableIndex;

public class RemoveRelationshipsMain {

	
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
		
        
        String findFriendsQuery = "start n=node({startNode}) MATCH n->[:FRIEND]->friend return friend";
        
		ReadableIndex<Node> autoNodeIndex = graphDb.index().getNodeAutoIndexer().getAutoIndex();		
		Node sallyNode = autoNodeIndex.get("name", "Sally Sparrow").getSingle();

		Transaction tx = graphDb.beginTx();
		try
		{
			Iterable<Relationship> rels = sallyNode.getRelationships();
			
			for( Relationship rel : rels )
			{
				rel.delete();
			}
			
			tx.success();
		}
		finally
		{
			tx.finish();
		}
        
		System.out.println( "done" );

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
