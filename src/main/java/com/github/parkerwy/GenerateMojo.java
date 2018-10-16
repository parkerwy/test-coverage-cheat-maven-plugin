package com.github.parkerwy;

import com.github.javafaker.Faker;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.io.IOException;


/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.NONE)
public class GenerateMojo
        extends AbstractBaseMojo {

    @Parameter(required = true, name = "numberOfClass", defaultValue = "10")
    private int numberOfClass;

    @Parameter(required = true, name = "numberOfStatement", defaultValue = "10")
    private int numberOfStatement;

    public void execute()
            throws MojoExecutionException {

        this.getLog().info("Generating " + numberOfClass + " classes with " + this.numberOfStatement + " statements in each.");
        this.getLog().info("Using source directory [" + this.getSourceDirectory() + "]");
        this.getLog().info("Using test source directory [" + this.getTestSourceDirectory() + "]");
        Faker faker = new Faker();
        for (int i = 0; i < this.numberOfClass; i++) {
            String className = faker.superhero().name();
            className = StringUtils.replace(className, " ", "");
            className = StringUtils.replace(className, "-", "");

            String packageName = this.getBasePackage();

            generateMainClass(packageName, className);
            generateTestClass(packageName, className);
        }
    }

    private void generateMainClass(String packageName, String className) throws MojoExecutionException {
        try {
            MethodSpec.Builder mainBuilder = MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addParameter(String[].class, "args");

            mainBuilder.addStatement("int i = 0");
            for (int i = 0; i < numberOfStatement; i++) {
                mainBuilder.addStatement("i++");
            }

            MethodSpec main = mainBuilder.build();

            TypeSpec mainType = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(main)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, mainType)
                    .build();

            javaFile.writeTo(this.getSourceDirectory());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate main class", e);
        }
    }

    private void generateTestClass(String packageName, String className) throws MojoExecutionException {
        try {
            MethodSpec shouldRunMain = MethodSpec.methodBuilder("shouldRunMain")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(AnnotationSpec.builder(Test.class).build())
                    .returns(void.class)
                    .addException(Exception.class)
                    .addStatement(className + ".main(null)")
                    .build();

            TypeSpec testType = TypeSpec.classBuilder(className + "Test")
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(shouldRunMain)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, testType)
                    .build();

            javaFile.writeTo(this.getTestSourceDirectory());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate test class", e);
        }
    }
}
