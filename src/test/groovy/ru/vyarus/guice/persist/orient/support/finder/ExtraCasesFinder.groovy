package ru.vyarus.guice.persist.orient.support.finder

import com.google.inject.persist.Transactional
import com.google.inject.persist.finder.Finder
import com.orientechnologies.orient.core.record.impl.ODocument
import com.tinkerpop.blueprints.Vertex
import ru.vyarus.guice.persist.orient.db.DbType
import ru.vyarus.guice.persist.orient.finder.Use
import ru.vyarus.guice.persist.orient.support.model.Model

/**
 * @author Vyacheslav Rusakov 
 * @since 05.08.2014
 */
@Transactional
interface ExtraCasesFinder {

    // iterable result
    @Finder(query = "select from Model")
    Iterable<Model> selectAll();

    // iterator result
    @Finder(query = "select from Model")
    Iterator<Model> selectAllIterator();

    // convert graph iterable to iterator
    @Finder(query = "select from Model")
    Iterator<Vertex> selectAllVertex();

    // graph connection return iterable, should pass as is
    @Finder(query = "select from Model")
    Iterable<Vertex> selectAllVertexIterable();

    // converted to HashSet
    @Finder(query = "select from Model", returnAs = HashSet.class)
    Iterable<Model> selectAllAsSet();

    // converted to HashSet
    @Finder(query = "select from Model", returnAs = HashSet.class)
    Iterable<Vertex> selectAllAsSetGraph();

    // just check vararg
    @Finder(query = "select from Model where name in ?")
    List<Model> findWithVararg(String... names);

    // use object connection for select
    @Finder(query = "select name from Model")
    @Use(DbType.OBJECT)
    List<ODocument> documentOverride();

    // use jdk8 optional
    @Finder(query = "select from Model")
    Optional<Model> findJdkOptional();

    // use guava optional
    @Finder(query = "select from Model")
    com.google.common.base.Optional<Model> findGuavaOptional();
}
