package com.osintegrators.example;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.springframework.beans.factory.FactoryBean;

public class GraphDatabaseFactoryBean implements FactoryBean<GraphDatabaseService>
{
	
	private String dbPath = "/opt/granny/neo4j-db";
	private String node_keys_indexable = "name,uuid";
	private String relationship_keys_indexable = "name,uuid";
	private boolean node_auto_indexing = true;
	private boolean relationship_auto_indexing = true;
	
	@Override
	public GraphDatabaseService getObject() throws Exception 
	{
        GraphDatabaseService graphDb = new GraphDatabaseFactory().
                newEmbeddedDatabaseBuilder( this.dbPath ).
                setConfig( GraphDatabaseSettings.node_keys_indexable, this.node_keys_indexable ).
                setConfig( GraphDatabaseSettings.relationship_keys_indexable, this.relationship_keys_indexable).
                setConfig( GraphDatabaseSettings.node_auto_indexing, this.node_auto_indexing ? "true" : "false" ).
                setConfig( GraphDatabaseSettings.relationship_auto_indexing, this.relationship_auto_indexing ? "true" : "false" ).
                newGraphDatabase();
        
        return graphDb;
	}
	
	@Override
	public Class<?> getObjectType() 
	{
		return GraphDatabaseService.class;
	}
	
	@Override
	public boolean isSingleton() 
	{
		return true;
	}

	public String getDbPath() 
	{
		return dbPath;
	}

	public void setDbPath(String dbPath) 
	{
		this.dbPath = dbPath;
	}

	public String getNode_keys_indexable() 
	{
		return node_keys_indexable;
	}

	public void setNode_keys_indexable(String node_keys_indexable) 
	{
		this.node_keys_indexable = node_keys_indexable;
	}

	public String getRelationship_keys_indexable() 
	{
		return relationship_keys_indexable;
	}

	public void setRelationship_keys_indexable(String relationship_keys_indexable) 
	{
		this.relationship_keys_indexable = relationship_keys_indexable;
	}

	public boolean isNode_auto_indexing() 
	{
		return node_auto_indexing;
	}

	public void setNode_auto_indexing(boolean node_auto_indexing) 
	{
		this.node_auto_indexing = node_auto_indexing;
	}

	public boolean isRelationship_auto_indexing() 
	{
		return relationship_auto_indexing;
	}

	public void setRelationship_auto_indexing(boolean relationship_auto_indexing) 
	{
		this.relationship_auto_indexing = relationship_auto_indexing;
	}
}
