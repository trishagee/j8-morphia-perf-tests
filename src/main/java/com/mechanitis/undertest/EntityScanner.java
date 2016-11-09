package com.mechanitis.undertest;

import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.utils.Assert;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.utils.Assert;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EntityScanner {
    private final ConfigurationBuilder conf;

    public EntityScanner(final Predicate<String> predicate) {
        conf = new ConfigurationBuilder();
        conf.setScanners(new TypeElementsScanner(), new TypeAnnotationsScanner());

        final Set<URL> s = new HashSet<URL>();
        s.addAll(ClasspathHelper.forClassLoader());
        s.addAll(ClasspathHelper.forJavaClassPath());
        final Iterator<URL> iterator = s.iterator();
        while (iterator.hasNext()) {
            final URL url = iterator.next();
            if (url.getPath().endsWith("jnilib")) {
                iterator.remove();
            }
        }
        conf.setUrls(new ArrayList<URL>(s));

        conf.filterInputsBy(predicate);
        conf.addScanners(new SubTypesScanner());
    }

    public void mapAllClassesAnnotatedWithEntityOriginal(Morphia m) {
        final Reflections r = new Reflections(conf);

        final Set<Class<?>> entities = r.getTypesAnnotatedWith(Entity.class);
        for (final Class<?> c : entities) {
            m.map(c);
        }
    }

    public void mapAllClassesAnnotatedWithEntityRefactored(Morphia m) {
        new Reflections(conf).getTypesAnnotatedWith(Entity.class).forEach(m::map);
    }

    public Set<URL> identifyURLsOriginal() {
        final Set<URL> s = new HashSet<URL>();
        s.addAll(ClasspathHelper.forClassLoader());
        s.addAll(ClasspathHelper.forJavaClassPath());
        final Iterator<URL> iterator = s.iterator();
        while (iterator.hasNext()) {
            final URL url = iterator.next();
            if (url.getPath().endsWith("jnilib")) {
                iterator.remove();
            }
        }
        return s;
    }

    public Set<URL> identifyURLsRefactored() {
        final Set<URL> s = new HashSet<URL>();
        s.addAll(ClasspathHelper.forClassLoader());
        s.addAll(ClasspathHelper.forJavaClassPath());
        s.removeIf(url -> url.getPath()
                             .endsWith("jnilib"));
        return s;
    }

}
