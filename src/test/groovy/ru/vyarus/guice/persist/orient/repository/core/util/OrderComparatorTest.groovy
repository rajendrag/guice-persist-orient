package ru.vyarus.guice.persist.orient.repository.core.util

import ru.vyarus.guice.persist.orient.repository.core.util.support.First
import ru.vyarus.guice.persist.orient.repository.core.util.support.Second
import ru.vyarus.guice.persist.orient.repository.core.util.support.Third
import spock.lang.Specification


/**
 * @author Vyacheslav Rusakov 
 * @since 23.02.2015
 */
class OrderComparatorTest extends Specification {

    def "Check order sorting"() {

        when: "sorting unsorted list"
        def list = [new Third(), new First(), new Second()]
        list.sort(new OrderComparator())
        OrderComparator
        then: "sorted"
        list[0].class == First
        list[1].class == Second

    }
}