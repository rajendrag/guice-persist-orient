package ru.vyarus.guice.persist.orient.db.scheme.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Provider;
import com.google.inject.matcher.Matcher;
import com.orientechnologies.common.reflection.OReflectionHelper;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import ru.vyarus.guice.persist.orient.db.scheme.SchemeInitializer;
import ru.vyarus.guice.persist.orient.db.scheme.initializer.ObjectSchemeInitializer;

import java.util.List;

/**
 * <p>Base class for object mapping initializers (jpa-like approach).</p>
 * Object initialization specific:
 * <ul>
 * <li>Orient ignore package, so class may be moved between packages</li>
 * <li>When entity field removed, orient will hold all data already stored in records of that type</li>
 * <li>When entity field type changes, it WILL NOT be migrated automatically.</li>
 * <li>When class renamed orient will register it as new entity and you will have to manually migrate old table
 * (or use sql commands to rename entity in db scheme)</li>
 * </ul>
 * <p>Internally use {@link ru.vyarus.guice.persist.orient.db.scheme.initializer.ObjectSchemeInitializer},
 * which extends default orient registration. Support custom plugins.</p>
 *
 * @author Vyacheslav Rusakov
 * @since 24.07.2014
 */
public abstract class AbstractObjectInitializer implements SchemeInitializer {
    private final Provider<OObjectDatabaseTx> dbProvider;
    private final ObjectSchemeInitializer schemeInitializer;
    private final Matcher<? super Class<?>> classMatcher;
    private final String[] packages;

    protected AbstractObjectInitializer(final Provider<OObjectDatabaseTx> dbProvider,
                                        final ObjectSchemeInitializer schemeInitializer,
                                        final Matcher<? super Class<?>> classMatcher,
                                        final String... packages) {
        this.schemeInitializer = schemeInitializer;
        this.dbProvider = dbProvider;
        this.classMatcher = classMatcher;
        this.packages = packages.length == 0 ? new String[]{""} : packages;
    }

    @Override
    public void initialize() {
        final OObjectDatabaseTx db = dbProvider.get();
        // auto create schema for new classes
        db.setAutomaticSchemaGeneration(true);
        registerClasses(scan());
        // important to guarantee correct state in dynamic environments (like tests or using different databases)
        db.getMetadata().getSchema().synchronizeSchema();
        // if persistent context restarted, registration may be requested one more time
        // without cache clear it would be ignored (handy for tests)
        schemeInitializer.clearModelCache();
    }

    /**
     * Called to init schema with predefined object connection.
     */
    protected List<Class<?>> scan() {
        final List<Class<?>> modelClasses = Lists.newArrayList();
        final Predicate<Class<?>> predicate = new Predicate<Class<?>>() {
            @Override
            public boolean apply(final Class<?> input) {
                return classMatcher.matches(input);
            }
        };
        for (String pkg : packages) {
            try {
                final List<Class<?>> classes = OReflectionHelper
                        .getClassesFor(pkg, Thread.currentThread().getContextClassLoader());
                modelClasses.addAll(Lists.newArrayList(Iterables.filter(classes, predicate)));
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Failed to resolve model classes from package: " + pkg, e);
            }
        }
        Preconditions.checkState(!modelClasses.isEmpty(),
                "No model classes found in packages: %s", Joiner.on(", ").join(packages));
        return modelClasses;
    }

    protected void registerClasses(final Iterable<Class<?>> classes) {
        for (Class<?> cls : classes) {
            schemeInitializer.register(cls);
        }
    }
}
