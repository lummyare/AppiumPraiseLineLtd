package com.suban.framework.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Transformer to automatically add RetryAnalyzer to all test methods
 * This enables automatic retry functionality for flaky mobile tests
 */
public class RetryTransformer implements IAnnotationTransformer {
    private static final Logger logger = LoggerFactory.getLogger(RetryTransformer.class);
    
    @Override
    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (annotation.getRetryAnalyzerClass() == null) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
            logger.debug("RetryAnalyzer added to method: {}", testMethod != null ? testMethod.getName() : "unknown");
        }
    }
}
