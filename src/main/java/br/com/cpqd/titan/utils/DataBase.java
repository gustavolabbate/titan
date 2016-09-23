package br.com.cpqd.titan.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * 
 * @author glabbate
 *
 */
public class DataBase
{
	private String sgbd;
//	private String host;
//	private String port;
//	private String instance;
//	private String connString;
	private String user;
	private String pass;
	private String url;
	
        private static final Logger LOGGER = Logger.getLogger(DataBase.class.getName());

        /**
         * 
         * @param sgbd
         * @param host
         * @param instance
         * @param port
         * @param user
         * @param pass
         */
	public DataBase(DBConnection dbConn)
	{
		this.sgbd = dbConn.getSgbd();
//		this.host = dbConn.ge;
//		this.instance = instance;
//		this.port = port;
		this.url = dbConn.getUrl();
		this.user = dbConn.getUser();
		this.pass = dbConn.getPass();
	}

	/**
	 * Run a select query.
	 *
	 * @author gustavo godoy
	 * @param  sql  the string query
	 * @return      a Array List with the results of query.
	 * @throws SQLException 
	 */
	public final ArrayList<String> runSelect(String sql) throws SQLException
	{
		ArrayList<String> result = null;
		ArrayList<ArrayList> resultLines = null;

		Connection conn = null;
		ResultSet rset= null;
		Statement stmt = null;
		try{
			result = new ArrayList<String>();
			if(this.sgbd.toLowerCase().equals("oracle"))
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
//				this.connString = "jdbc:oracle:thin:@" + host + ":" + port +  ":" + instance;
			}
			else if(this.sgbd.toLowerCase().equals("sqlserver"))
			{
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//				this.connString = "jdbc:sqlserver://"+host+":"+port+";DatabaseName="+instance;
			}
			else if(this.sgbd.toLowerCase().equals("mysql"))
			{
				Class.forName("com.mysql.jdbc.Driver");
				//TODO Connection String for MySql
			}

			conn = DriverManager.getConnection(this.url,this.user, this.pass);
			stmt = conn.createStatement();

			rset = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rset.getMetaData();
			int numColumns = rsmd.getColumnCount();


			result = new ArrayList<String>();
			while ( rset.next() )
			{
				for (int i=1; i<numColumns+1; i++)
				{
					result.add(rset.getString(i));
				}
			}

			
			return result;
		}catch (Exception e){

			LOGGER.error(e.getMessage(), e);
			stmt.close();	
		    	conn.close();
			rset.close();
			return result;
		}
		finally
		{
		    	stmt.close();	
		    	conn.close();
			rset.close();
		}

	}


	/**
	 * Run a create, update or delete query.
	 *
	 * @author gustavo godoy
	 * @param  sql  the string query
	 * @return      1 if success, otherwise, it failed.
	 * @throws SQLException 
	 */
	public final Integer runUpdate (String sql) throws SQLException
	{
		Connection conn = null;
		Statement stmt = null;
		int resultado = -1;
		try{
			if(this.sgbd.equals("Oracle"))
			{
				Class.forName("oracle.jdbc.driver.OracleDriver" );
			}
			else if(this.sgbd.equals("SqlServer"))
			{
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			}
			else if(this.sgbd.equals("MySql"))
			{
				Class.forName("com.mysql.jdbc.Driver");
			}

			conn = DriverManager.getConnection(this.url,this.user,this.pass);
			stmt = conn.createStatement();

			resultado = stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
			return resultado;

		}
		catch (Exception e){
			resultado = -1;
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return resultado;
		}finally
		{
		    	stmt.close();
		    	conn.close();
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}