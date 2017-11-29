package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
@RunWith(Suite.class)
@SuiteClasses({ContinentTest.class, 
               CountryTest.class,
               RiskMapTest.class,
               RiskGameModalTest.class,
               PlayerModelTest.class})
/**
 * This is the test suite for testing all the cases
 */
public class TestSuite {

}