jongo-dao
=========

A generic data access library for jongo

Summary
=======
Encapsulates frequently used boiler plate, so models don't have to rewrite the same
boring code again and again.

Author
======
Mohammad Nasir Rasul < nasir [at] rasul [dot] ca>

Website: http://mnrasul.github.io/jongo-dao/

Issue Tracker: On Github - https://github.com/mnrasul/jongo-dao/issues

Code: https://github.com/mnrasul/jongo-dao

Mailing List: None exists yet. You can email me. If there is sufficient traction, I'll set one up.

Wishlist
========
 - [x] Provide support for aggregation queries

Usage
=====
Have a look at the test class provided for an example usage.

>Helpful Tip : Use mvn dependency:copy-dependencies -DoutputDirectory=directory
>to copy all jars to a target directory of your choice if needed


Advantages
==========
I find it tedious to rewrite the same kind of queries for similar objects. I built this DAO for myself. 
Perhaps someone will find it useful.

Feel free to modify it to suit your needs.

It saves me the effort of writing same queries again and again. Instead, I only need to write queries
which are rather unique. Overtime, I expect, to flesh this out more as I observe my usage patterns.

Usage
=====
* Create a model
* Subclass DAO to provide specific initialization vectors
* Use DAO

 
## Create a model

```java
public class Person implements Model{
    @Id
    ObjectId id;
    
    @JsonProperty("fn")
    String firstName;
    
    @JsonProperty("ln")
    String lastName;
    
    @JsonProperty("a")
    Address address;
    
    @JsonProperty("l")
    List<String> tags = new ArrayList<String>(3);
    
    public ObjectId getId() {
        return this.id;
    }

    public String getHash() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
```



## Subclass DAO
```java
public class PersonDAO extends DAO<Person>{

    public PersonDAO(String connectionURL, String dbname, String collectionName, Class<Person> type) throws UnknownHostException {
        super(connectionURL, dbname, collectionName, Person.class);
    }
    
    public PersonDAO() throws UnknownHostException{
        super("localhost:27017", "jongo-dao-test", "person", Person.class);
    }
    
}
```

## Use DAO
```java
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
        
        p.firstName = "Genie";
        dao.update(p);
        Assert.assertNotEquals(p.firstName, find.firstName);
        
        find = dao.find(id);
        Assert.assertEquals(p.firstName, find.firstName);
        
        dao.delete(id);
        find = dao.find(id);
        Assert.assertNull(find);
        
    }
```
