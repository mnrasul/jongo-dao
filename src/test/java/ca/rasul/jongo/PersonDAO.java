/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.rasul.jongo;

import java.net.UnknownHostException;

/**
 *
 * @author nasir
 */
public class PersonDAO extends DAO<Person>{

    public PersonDAO(String connectionURL, String dbname, String collectionName, Class<Person> type) throws UnknownHostException {
        super(connectionURL, dbname, collectionName, Person.class);
    }
    
    public PersonDAO() throws UnknownHostException{
        super("localhost:27017", "jongo-dao-test", "person", Person.class);
    }
    
}
