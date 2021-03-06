package org.junit.internal.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

@Deprecated
public class JUnit4ClassRunner extends Runner implements Filterable, Sortable {
    private TestClass testClass;
    private final List<Method> testMethods = getTestMethods();

    public JUnit4ClassRunner(Class<?> klass) throws InitializationError {
        this.testClass = new TestClass(klass);
        validate();
    }

    /* access modifiers changed from: protected */
    public List<Method> getTestMethods() {
        return this.testClass.getTestMethods();
    }

    /* access modifiers changed from: protected */
    public void validate() throws InitializationError {
        MethodValidator methodValidator = new MethodValidator(this.testClass);
        methodValidator.validateMethodsForDefaultRunner();
        methodValidator.assertValid();
    }

    @Override // org.junit.runner.Runner
    public void run(final RunNotifier notifier) {
        new ClassRoadie(notifier, this.testClass, getDescription(), new Runnable() {
            /* class org.junit.internal.runners.JUnit4ClassRunner.AnonymousClass1 */

            @Override // java.lang.Runnable
            public void run() {
                JUnit4ClassRunner.this.runMethods(notifier);
            }
        }).runProtected();
    }

    /* access modifiers changed from: protected */
    public void runMethods(RunNotifier notifier) {
        for (Method method : this.testMethods) {
            invokeTestMethod(method, notifier);
        }
    }

    @Override // org.junit.runner.Runner, org.junit.runner.Describable
    public Description getDescription() {
        Description spec = Description.createSuiteDescription(getName(), classAnnotations());
        for (Method method : this.testMethods) {
            spec.addChild(methodDescription(method));
        }
        return spec;
    }

    /* access modifiers changed from: protected */
    public Annotation[] classAnnotations() {
        return this.testClass.getJavaClass().getAnnotations();
    }

    /* access modifiers changed from: protected */
    public String getName() {
        return getTestClass().getName();
    }

    /* access modifiers changed from: protected */
    public Object createTest() throws Exception {
        return getTestClass().getConstructor().newInstance(new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void invokeTestMethod(Method method, RunNotifier notifier) {
        Description description = methodDescription(method);
        try {
            new MethodRoadie(createTest(), wrapMethod(method), notifier, description).run();
        } catch (InvocationTargetException e) {
            testAborted(notifier, description, e.getCause());
        } catch (Exception e2) {
            testAborted(notifier, description, e2);
        }
    }

    private void testAborted(RunNotifier notifier, Description description, Throwable e) {
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, e));
        notifier.fireTestFinished(description);
    }

    /* access modifiers changed from: protected */
    public TestMethod wrapMethod(Method method) {
        return new TestMethod(method, this.testClass);
    }

    /* access modifiers changed from: protected */
    public String testName(Method method) {
        return method.getName();
    }

    /* access modifiers changed from: protected */
    public Description methodDescription(Method method) {
        return Description.createTestDescription(getTestClass().getJavaClass(), testName(method), testAnnotations(method));
    }

    /* access modifiers changed from: protected */
    public Annotation[] testAnnotations(Method method) {
        return method.getAnnotations();
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        Iterator<Method> iter = this.testMethods.iterator();
        while (iter.hasNext()) {
            if (!filter.shouldRun(methodDescription(iter.next()))) {
                iter.remove();
            }
        }
        if (this.testMethods.isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

    @Override // org.junit.runner.manipulation.Sortable
    public void sort(final Sorter sorter) {
        Collections.sort(this.testMethods, new Comparator<Method>() {
            /* class org.junit.internal.runners.JUnit4ClassRunner.AnonymousClass2 */

            public int compare(Method o1, Method o2) {
                return sorter.compare(JUnit4ClassRunner.this.methodDescription(o1), JUnit4ClassRunner.this.methodDescription(o2));
            }
        });
    }

    /* access modifiers changed from: protected */
    public TestClass getTestClass() {
        return this.testClass;
    }
}
