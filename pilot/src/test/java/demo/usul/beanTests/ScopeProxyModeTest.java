package demo.usul.beanTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.NEVER, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ScopeProxyModeTest {

    @Autowired
    private TestComponent testComponent;

    @Test
    void shouldPrototypeMultipleInstance() {
        for (int i = 0; i < 3; i++) {
            testComponent.doSomething();
        }
    }

    @Test
    void shouldPrototypeInSingletonHasOnlyOneInstanceBetweenMethods() {
        for (int i = 0; i < 3; i++) {
            testComponent.doSomething();
        }
        for (int i = 0; i < 2; i++) {
            testComponent.doOtherThingPrototype();
        }
    }

    @Test
    void shouldPrototypeMultipleInstanceTargetClass() {
        testComponent.doSomethingTargetClass2();
    }

    @Test
    void shouldPrototypeMultipleInstanceTargetClass2() {
        for (int i = 0; i < 3; i++) {
            testComponent.doSomethingTargetClass();
        }
    }
}

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class TestComponent {

    @Autowired
    private TestComponentPrototype testComponentPrototype;

    @Autowired
    private TestComponentPrototypeTargetClass testComponentPrototypeTargetClass;

    public TestComponent() {
        log.warn("singleton component init");
    }

    public void doSomething() {
        for (int i = 0; i < 10; i++) {

            testComponentPrototype.print();
        }
    }

    public void doOtherThingPrototype() {
        testComponentPrototype.print();
    }

    public void doSomethingTargetClass() {
        testComponentPrototypeTargetClass.print();
    }

    public void doSomethingTargetClass2() {
        for (int i = 0; i < 3; i++) {
            testComponentPrototypeTargetClass.print();
        }
    }
}

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class TestComponentPrototype {

    public TestComponentPrototype() {
        System.out.println("prototype component init");
    }

    public void print() {
        System.out.println("hi" + this.hashCode());
    }

}


@Slf4j
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
class TestComponentPrototypeTargetClass {

    public TestComponentPrototypeTargetClass() {
        log.warn("prototype component init");
    }

    public void print() {
        log.warn("hi + " + this.hashCode());
    }
}
