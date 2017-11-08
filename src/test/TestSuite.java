package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

public class TestSuite {

    @RunWith(Suite.class)
    @Suite.SuiteClasses({ContinentTest.class, CountryTest.class, RiskMapTest.class,TestStartupPhase.class,FortificationPhaseTest.class,PlayerTestReinforcementArmies.class,AttackPhase.class})

    public class MapTestSuite {

    }
}
