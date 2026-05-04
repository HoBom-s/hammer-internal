package com.hammer.internal;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.hammer.internal", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule domain_should_not_depend_on_application = noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..application..");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_adapter = noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..adapter..");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_spring = noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("org.springframework..", "jakarta..");

    @ArchTest
    static final ArchRule application_should_not_depend_on_adapter = noClasses()
            .that()
            .resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..adapter..");

    @ArchTest
    static final ArchRule adapter_in_should_not_depend_on_adapter_out = noClasses()
            .that()
            .resideInAPackage("..adapter.in..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..adapter.out..");

    @ArchTest
    static final ArchRule adapter_out_should_not_depend_on_adapter_in = noClasses()
            .that()
            .resideInAPackage("..adapter.out..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..adapter.in..");

    @ArchTest
    static final ArchRule service_should_implement_use_case_ports = classes()
            .that()
            .resideInAPackage("..application.service..")
            .should()
            .implement(resideInAPackage("..application.port.in.."));

    @ArchTest
    static final ArchRule persistence_adapter_should_implement_out_ports = classes()
            .that()
            .resideInAPackage("..adapter.out.persistence..")
            .and()
            .haveSimpleNameEndingWith("PersistenceAdapter")
            .should()
            .implement(resideInAPackage("..application.port.out.."));

    @ArchTest
    static final ArchRule no_cycles_between_modules =
            slices().matching("com.hammer.internal.(*)..").should().beFreeOfCycles();
}
