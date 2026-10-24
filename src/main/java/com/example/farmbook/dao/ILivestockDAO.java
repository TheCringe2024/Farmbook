
package com.example.farmbook.dao;

import com.example.farmbook.model.Livestock;
import java.util.List;

/**
 * Defines persistence operations for livestock records.
 *
 * <p>Separating the interface from the concrete SQLite
 * implementation reduces coupling and allows the service
 * layer to be tested without requiring a real database.
 */
public interface ILivestockDAO {

    /**
     * Saves a new livestock record to persistent storage.
     *
     * @param livestock the livestock record to save
     * @return {@code true} when saved successfully;
     *         {@code false} otherwise
     */
    boolean save(Livestock livestock);

    /**
     * Retrieves all stored livestock records.
     *
     * @return a list containing the available livestock records
     */
    List<Livestock> findAll();

    /**
     * Deletes a livestock record by its database ID.
     *
     * @param id the database ID of the record to delete
     * @return {@code true} when a record is deleted;
     *         {@code false} otherwise
     */
    boolean delete(int id);

    /**
     * Updates an existing livestock record.
     *
     * @param livestock the updated record, including
     *                  its existing database ID
     * @return {@code true} when an existing record is updated;
     *         {@code false} otherwise
     */
    boolean update(Livestock livestock);
}
