package io.antmedia.datastore.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.antmedia.AppSettings;
import io.antmedia.cluster.DBReader;

public class DataStoreFactory implements IDataStoreFactory{

	public static final String DB_TYPE_MEMORYDB = "memorydb";
	public static final String DB_TYPE_MAPDB = "mapdb";
	public static final String DB_TYPE_MONGODB = "mongodb";

	@Autowired
	private AppSettings appSettings;

	private static Logger logger = LoggerFactory.getLogger(DataStoreFactory.class);

	
	private DataStore dataStore;
	private String appName;
	private String dbName;
	
	/**
	 * One of the DB_TYPE_*
	 */
	private String dbType;
	private String dbHost;
	private String dbUser;
	private String dbPassword;
	
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public DataStore getDataStore() {
		if (dataStore == null) {
			if(dbType.contentEquals(DB_TYPE_MONGODB))
			{
				dataStore = new MongoStore(dbHost, dbUser, dbPassword, dbName);
			}
			else if(dbType .contentEquals(DB_TYPE_MAPDB))
			{
				dataStore = new MapDBStore(dbName+".db");
			}
			else if(dbType .contentEquals(DB_TYPE_MEMORYDB))
			{
				dataStore = new InMemoryDataStore(dbName);
			}
			else {
				logger.error("Undefined Datastore:{} app:{} db name:{}", dbType, appName, dbName);
			}
			
			logger.info("Used Datastore:{} app:{} db name:{}", getDbType(), getAppName(), getDbName());
			
			if(dataStore != null) {
				dataStore.setWriteStatsToDatastore(getAppSettings().isWriteStatsToDatastore());
				DBReader.instance.addDataStore(appName, dataStore);
				dataStore.clearStreamsOnThisServer();
			}
		}
		return dataStore;
	}
	
	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	public String getAppName()
	{
		return appName;
	}
	
	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public AppSettings getAppSettings() {
		return appSettings;
	}

	public void setAppSettings(AppSettings appSettings) {
		this.appSettings = appSettings;
	}

}
	
