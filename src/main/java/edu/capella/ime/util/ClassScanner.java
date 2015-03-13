package edu.capella.ime.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

public class ClassScanner {

	private final ClassPathScanningCandidateComponentProvider scanner = 
			new ClassPathScanningCandidateComponentProvider(false);

	public final Collection<Class<?>> findClasses(String... basePackages) {

		final List<Class<?>> classes = new ArrayList<>();

		for (final String basePackage : basePackages) {

			for (final BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {

				classes.add(ClassUtils.resolveClassName(
						candidate.getBeanClassName(),
						ClassUtils.getDefaultClassLoader()));
			}
		}

		return classes;
	}

	public ClassScanner withIncludeFilter(final TypeFilter filter) {
		
		scanner.addIncludeFilter(filter);
		
		return this;
	}


	public ClassScanner withAnnotationFilter(final Class<? extends Annotation> annotationClass) {
		
		return withIncludeFilter(new AnnotationTypeFilter(annotationClass));
	}
}
