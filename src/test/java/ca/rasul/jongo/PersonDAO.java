/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.rasul.jongo;

import java.net.UnknownHostException;
import org.bson.types.ObjectId;

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

    @Override
    public void update(ObjectId id, Person person) {
        collection.update(id).with("{$set: {fn: #, ln: #}}", person.firstName, person.lastName);
    }

    @Override
    public void update(String query, Person object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
