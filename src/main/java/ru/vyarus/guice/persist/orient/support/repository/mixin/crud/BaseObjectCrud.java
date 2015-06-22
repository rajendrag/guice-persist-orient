package ru.vyarus.guice.persist.orient.support.repository.mixin.crud;

import com.orientechnologies.orient.core.id.ORID;
import ru.vyarus.guice.persist.orient.repository.delegate.Delegate;
import ru.vyarus.guice.persist.orient.support.repository.mixin.crud.delegate.BaseObjectCrudDelegate;

import java.util.Iterator;
import java.util.List;

/**
 * Base crud mixin for object repositories. It does not contain remove methods, because remove methods
 * implementation should be different for pure objects and objects for vertex types.
 *
 * @param <T> entity type
 * @author Vyacheslav Rusakov
 * @since 21.06.2015
 */
@Delegate(BaseObjectCrudDelegate.class)
public interface BaseObjectCrud<T> {

    /**
     * @param id entity id
     * @return found entity or null
     */
    T get(String id);

    /**
     * @param id entity id
     * @return found entity or null
     */
    T get(ORID id);

    /**
     * Note: for object entities returns object proxy. If you need to use saved object just after save,
     * use returned proxy instead of original object (original entity will not be updated with id or version).
     *
     * @param entity entity to save or update
     * @return saved entity instance
     */
    T save(T entity);

    /**
     * @param entity entity to attach
     * @return return same entity if its already proxy or proxied instance
     */
    T attach(T entity);

    /**
     * Calls attach first to simplify usage out of transaction.
     *
     * @param entity entity object (proxy)
     * @return detached row entity (unproxied deeply)
     */
    T detach(T entity);

    /**
     * Calls attach first to simplify usage out of transaction.
     *
     * @param entities entities objects
     * @return detached entities list (unproxied deeply)
     */
    List<T> detachAll(T... entities);

    /**
     * Calls attach first to simplify usage out of transaction.
     *
     * @param entities entities objects
     * @return detached entities list (unproxied deeply)
     */
    List<T> detachAll(Iterable<T> entities);

    /**
     * Calls attach first to simplify usage out of transaction.
     *
     * @param entities entities objects
     * @return detached entities list (unproxied deeply)
     */
    List<T> detachAll(Iterator<T> entities);

    /**
     * Create new empty proxy. Used if it's important to track instance:
     * if raw object used for save, then it will not be updated and you will
     * have to use returned proxy object. This allows to overcome limitation.
     *
     * @return empty object proxy
     */
    T create();

    /**
     * @return all records of type
     */
    Iterator<T> getAll();
}
