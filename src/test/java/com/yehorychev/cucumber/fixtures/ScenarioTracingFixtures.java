package com.yehorychev.cucumber.fixtures;

import com.microsoft.playwright.Tracing;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.nio.file.Paths;

public class ScenarioTracingFixtures {

    @Before
    public void setupTracing() {
        Hooks.getBrowserContext().tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @After
    public void recordTraces(Scenario scenario) {
        String traceName = scenario.getName().replace(" ", "-").toLowerCase();
        Hooks.getBrowserContext().tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/trace/trace-" + traceName + ".zip"))
        );
    }
}