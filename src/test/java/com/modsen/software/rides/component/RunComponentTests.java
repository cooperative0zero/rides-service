package com.modsen.software.rides.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:feature",
        glue = "com.modsen.software.rides.component.step"
)
public class RunComponentTests {
}
