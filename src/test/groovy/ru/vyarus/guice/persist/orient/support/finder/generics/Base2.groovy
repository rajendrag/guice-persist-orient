package ru.vyarus.guice.persist.orient.support.finder.generics

import ru.vyarus.guice.persist.orient.support.model.Model

/**
 * checks generic forward and two generics
 *
 * @author Vyacheslav Rusakov 
 * @since 16.10.2014
 */
public interface Base2<K,P> extends Lvl2Base2<K>, Lvl2Base3<Model> {

}