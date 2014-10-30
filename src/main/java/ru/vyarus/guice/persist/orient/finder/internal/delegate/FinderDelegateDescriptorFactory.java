package ru.vyarus.guice.persist.orient.finder.internal.delegate;

import com.google.common.base.Strings;
import com.google.inject.Injector;
import ru.vyarus.guice.persist.orient.finder.delegate.FinderDelegate;
import ru.vyarus.guice.persist.orient.finder.internal.FinderDefinitionException;
import ru.vyarus.guice.persist.orient.finder.internal.delegate.method.MethodDescriptorAnalyzer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

/**
 * Analyze finder delegate method: search for target method in delegate and build descriptor object.
 *
 * @author Vyacheslav Rusakov
 * @since 21.10.2014
 */
@Singleton
public class FinderDelegateDescriptorFactory {

    private final Injector injector;

    @Inject
    public FinderDelegateDescriptorFactory(final Injector injector) {
        this.injector = injector;
    }

    @SuppressWarnings("unchecked")
    public FinderDelegateDescriptor buildDescriptor(final Method method, final Map<String, Type> generics,
                                                    final Class<?> finderType) {
        final FinderDelegateDescriptor descriptor = new FinderDelegateDescriptor();
        final FinderDelegate annotation = DelegateUtils.findAnnotation(method);
        FinderDefinitionException.check(annotation != null,
                "Can't find delegate method for %s#%s%s: @FinderDelegate annotation is not defined",
                method.getDeclaringClass().getName(), method.getName(), Arrays.toString(method.getParameterTypes()));
        descriptor.method = MethodDescriptorAnalyzer.analyzeMethod(method, annotation.value(),
                Strings.emptyToNull(annotation.method()), generics, finderType);
        descriptor.instanceProvider = injector.getProvider(annotation.value());
        return descriptor;
    }
}
