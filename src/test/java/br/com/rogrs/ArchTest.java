package br.com.rogrs;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("br.com.rogrs");

        noClasses()
            .that()
            .resideInAnyPackage("br.com.rogrs.service..")
            .or()
            .resideInAnyPackage("br.com.rogrs.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..br.com.rogrs.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
