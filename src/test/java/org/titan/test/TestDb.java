package org.titan.test;


import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.titan.utils.DBConnection;
import org.titan.utils.DataBase;

@Ignore
public class TestDb {

	private DataBase db;
	
	@Before
	public void setUp() throws Exception {
		
		DBConnection dbConn = new DBConnection();
		dbConn.setSgbd("oracle");
		dbConn.setUrl("jdbc:oracle:thin:@cidra.com.br:1521:cidr10i1");
		//jdbc:oracle:thin:@vmbd5:1521:vmbd5i1
		dbConn.setUser("FRAME_TST");
		dbConn.setPass("FRAME_TST");
		
		db = new DataBase(dbConn);
		
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	
	@Test
	public void testSelect() throws Exception
	{
		String sql = "select sysdate from dual";
//		sql = "select cod_login from t_sec_usuario where cod_login = 'bla'";
		ArrayList<String> returned = new ArrayList<String>();
		returned = db.runSelect(sql);
		assertNotNull(returned);
	}
	
	

}
