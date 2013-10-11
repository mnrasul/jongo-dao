/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.rasul.jongo;

import java.net.UnknownHostException;
import org.bson.types.ObjectId;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;
import org.junit.Test;
/**
 *Tests persistence of Person which includes an embedded document
 * @author nasir
 */
@RunWith(JUnit4.class)
public class PersonTest{
    /**
     * Tests 4 CRUD operations
     * - creates
     * - reads to verify
     * - updates
     * - deletes
     * @throws UnknownHostException 
     */
    @Test
    public void testCRUD() throws UnknownHostException{
        Person p = new Person();
        p.lastName = "Aladin";
        p.firstName = "Prince";
        Address address = new Address();
        address.street = "Palace";
        address.city = "City";
        address.postal = "12345";
        address.state = "Kingdom";
        address.country = "Magicland";
        p.address = address;
        p.tags.add("flying carpet");
        p.tags.add("magic lamp");
        p.tags.add("fantasy");
        
        PersonDAO dao = new PersonDAO();
        dao.save(p);
        ObjectId id = p.id;
        Person find = dao.find(id);
        
        Assert.assertEquals(p.firstName, find.firstName);
        Assert.assertEquals(p.lastName, find.lastName);
        Assert.assertEquals(p.address.city, find.address.city);


        
//        p.firstName = "Genie";
        Person p2 = new Person();
        p2.firstName = "fafa";
        p2.lastName = "lala";
        dao.update(id,p2);
        find = dao.find(id);
        Assert.assertEquals(p2.firstName, find.firstName);
        Assert.assertEquals(p2.lastName, find.lastName);
        Assert.assertEquals(p.address.city, find.address.city);
        
        find = dao.find(id);
        Assert.assertEquals(p2.firstName, find.firstName);
        
        dao.delete(id);
        find = dao.find(id);
        Assert.assertNull(find);
        
    }
}
