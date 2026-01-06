package za.blkmarket;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(packages = "za.blkmarket")
public class ArchitectureTest {

    @ArchTest
    static final ArchRule noServiceLayer =
        noClasses().that().resideInAPackage("za.blkmarket..")
            .should().haveSimpleNameEndingWith("Service");

    @ArchTest
    static final ArchRule noRepositoryLayer =
        noClasses().that().resideInAPackage("za.blkmarket..")
            .should().haveSimpleNameEndingWith("Repository");
}
