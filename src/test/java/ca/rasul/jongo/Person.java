/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.rasul.jongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nasir
 */
public class Person implements Model<ObjectId> {
    @Id
    ObjectId id;
    @JsonProperty("ref")
    ObjectId reference;

    @JsonProperty("fn")
    String firstName;
    
    @JsonProperty("ln")
    String lastName;
    
    @JsonProperty("a")
    Address address;
    
    @JsonProperty("l")
    List<String> tags = new ArrayList<String>(3);

    int version = 1;

    public ObjectId getId() {
        return this.id;
    }

    public String getHash() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
