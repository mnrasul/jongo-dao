/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.rasul.jongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;
import org.jongo.Aggregate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.ResultHandler;

/**
 * Defines a generic Data Access Object (DAO). It provides frequently used
 * methods such as list, find, delete, update, save.
 *
 * It saves boiler plate.
 *
 * @author nasir
 */
public abstract class DAO<T extends Model> {

    protected MongoClient client = null;
    protected DB db = null;
    protected Jongo jongo = null;
    protected MongoCollection collection = null;
    private Class<T> type;
    private final String collectionName;

    /**
     * Create a DAO for given collection, using the URL and DB
     *
     * @param connectionURL
     * @param dbname
     * @param collectionName
     * @param type Type needs to be provided
     * @throws UnknownHostException Usually thrown when invalid connection URL
     * is provided, or database is not running
     */
    public DAO(String connectionURL, String dbname, String collectionName, Class<T> type) throws UnknownHostException {
        client = new MongoClient(connectionURL);
        db = client.getDB(dbname);
        jongo = new Jongo(db);
        this.collectionName = collectionName;
        this.collection = jongo.getCollection(this.collectionName);
        this.collection.withWriteConcern(WriteConcern.JOURNALED);
        this.type = type;
    }

    
    /**
     * Specifies what is the default limit
     *
     * @return
     */
    public int getLimit() {
        return 10;
    }

    /**
     * Converts an iterator to a List
     *
     * @param <T>
     * @param iter
     * @return
     */
    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext()) {
            copy.add(iter.next());
        }
        return copy;
    }

    /**
     * Convenience method which returns a count of all documents in collection
     * @return 
     */
    public long count(){
        return collection.count();
    }
    
    /**
     * Returns the count of documents matching the qurey
     * @param query
     * @param parameters
     * @return 
     */
    public long count(String query, Object ... parameters){
        return collection.count(query, parameters);
    }

    
    /**
     * Returns a list, specifying no query parameters, applying provided limit
     * and skip. Convenience method
     * same as calling list(0,0)
     * @return
     */
    public List<T> list() {
        return list(0,0);
    }
    /**
     * Returns list of documents.
     * 
     * @param limit If 0 is passed, there is no upper limit
     * @param skip if 0 is passed records from first and onwards are included
     * @return 
     */
    public List<T> list(int limit, int skip) {
        return copyIterator(collection.find().limit(limit).skip(skip).as(type).iterator());
    }

    /**
     * Experimental : Returns JSON Array
     *
     * @param limit
     * @param skip
     * @param query
     * @param fields
     * @return json contains list
     */
    public String listJSON(int limit, int skip, DBObject query, DBObject fields) {
        DBCollection col = db.getCollection(this.collectionName);
        return col.find(query, fields).limit(limit).skip(skip).toArray().toString();
    }

//    public List<T> list(){
//        return copyIterator(collection.find().as(type).iterator());
//    }    
    /**
     * Returns an object if one exists matching the provided ObjectId
     *
     * @param id
     * @return
     */
    public T find(ObjectId id) {
        return collection.findOne(id).as(type);
    }

    /**
     * Returns an object if one exists matching the provided ObjectId
     *
     * @param id
     * @return
     */
    public T find(String id, ResultHandler<T> handler) {
        return collection.findOne(new ObjectId(id)).map(handler);
    }
    
    
    /**
     * Returns an object if one exists matching the provided ObjectId in String
     * form. Before making the database call, it is converted into ObjectId
     *
     * @param id
     * @return
     */
    public T find(String id) {
        return collection.findOne(new ObjectId(id)).as(type);
    }
    
    public T find(String id, String fields){
        return collection.findOne(new ObjectId(id)).projection(fields).as(type);
    }

    /**
     * Saves the entity
     *
     * @param entity
     */
    public void save(T entity) {
        collection.save(entity);
    }

    /**
     * @param entity
     * @return
     */
    public Object update(T entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("ID is empty. Cannot update. Call save instead to create a new instance!");
        }
        save(entity);
        return entity.getId();
    }

    /**
     * Deletes a document based on ID. ID is converted into ObjectId.
     * Convenience method
     *
     * @param id
     */
    public void delete(String id) {
        collection.remove(new ObjectId(id));
    }

    /**
     * Deletes given document
     *
     * @param id
     */
    public void delete(ObjectId id) {
        collection.remove(id);
    }
    
    public List<T> aggregate(String ... pipeline){
//        return collection.aggregate(pipeline).as(type);
        int i= 0;
        Aggregate agr = null;
        for (String p : pipeline){
            if (i == 0){
                agr = collection.aggregate(p);
            }else{
                agr = agr.and(p);
            }
        }
        
        return agr.as(type);
    }
}
