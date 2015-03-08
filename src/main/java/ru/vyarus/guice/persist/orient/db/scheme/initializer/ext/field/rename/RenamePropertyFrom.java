package ru.vyarus.guice.persist.orient.db.scheme.initializer.ext.field.rename;

import ru.vyarus.guice.persist.orient.db.scheme.initializer.core.spi.field.SchemeFieldInit;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Scheme model field extension to rename property. Property is renamed before orient registration, so data will
 * be preserved.
 * <p>If class not registered or old property not exist in db, no action will be performed.</p>
 * <p>If new property already registered, exception will be thrown, because rename is impossible.</p>
 * <p>Sample sql: alter property Model.oldName name newName</p>
 *
 * @author Vyacheslav Rusakov
 * @since 07.03.2015
 */
@Target(FIELD)
@Retention(RUNTIME)
@SchemeFieldInit(RenameFromFieldExtension.class)
public @interface RenamePropertyFrom {

    /**
     * @return old property name
     */
    String value();
}