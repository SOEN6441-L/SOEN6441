# Project Coding Standard

The intention of this guide is to provide a set of conventions that encourage good code.

In general, much of our style and conventions mirror the
[Code Conventions for the Java Programming Language](http://www.oracle.com/technetwork/java/codeconvtoc-136057.html)
, [Google's Java Style Guide](https://google.github.io/styleguide/javaguide.html), and slides from Dr. Joey Paquet .

## Coding style

### Formatting

#### Use line breaks wisely
There are generally two reasons to insert a line break:

1. Our statement exceeds the column limit.

2. We want to logically separate different thought and segment of code.<br />

#### Indent style
We use the "one true brace style".
Indent size is 2 columns.

    // For example.
    if (this.player.ifForceExchange()){
            //change card
    }else{
            //do not change
    }

Continuation indent is 4 columns.  Nested continuations may add 4 columns or 2 at each level.

    
    //   - Each component of the message is separate and self-contained.
    //   - Adding or removing a component of the message requires minimal reformatting.
    public void changeCard(){
        ExchangeInteraction ei = new ExchangeInteraction();
        ei.GetAndSetCards(this.player.cards);
        ei.SetButtonLabel();
        this.player.exchangeTime = ei.count;
        this.player.cards = ei.cards;
        this.player.CalculateArmies();
    }

We do not break up a statement unnecessarily.
    
    final String value = otherValue;

Method declaration continuations.

    // Preferred for easy scanning and extra column space.
    //Using camel case

    public int getChangeCardTimes() {
        return changeCardTimes;
    }

##### Chained method calls
  
    //   - Method calls are isolated to a line.
    //   - The proper location for a new method call is unambiguous.
    int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
	int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);

#### 100 column limit
You should follow the convention set by the body of code you are working with.
We tend to use 100 columns for a balance between fewer continuation lines but still easily
fitting two editor tabs side-by-side on a reasonably-high resolution display.

#### CamelCase for types, camelCase for variables, UPPER_SNAKE for constants

#### No trailing whitespace
Trailing whitespace characters, while logically benign, add nothing to the program.

    private String matrixDisplayMode = "prefered"; //prefered, same

### Field, class, and method declarations

##### Modifier order

We follow the [Java Language Specification](http://docs.oracle.com/javase/specs/) for modifier
ordering (sections
[8.1.1](http://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.1.1),
[8.3.1](http://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.3.1) and
[8.4.3](http://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.4.3)).

    private final volatile String value;

### Variable naming

#### Extremely short variable names should be reserved for instances like loop indices.
 
    class Player {
      private int armies;
      private String name;
    }

#### Include units in variable names
    
    //   - Unit is built in to the type.
    //   - The field is easily adaptable between units, readability is high.
    public Map<Integer,ArrayList<Country>> countries;
    public Map<Integer,ArrayList<Integer>> adjacencyList;

#### Don't embed metadata in variable names
A variable name should describe the variable's purpose.  Adding extra information like scope and
type is generally a sign of a bad variable name.

Avoid embedding the field type in the field name.
    
    // Bad.
    Map<Integer, Player> idToUserMap;
    String valueString;

    // Good.
    Map<Integer, Player> usersById;
    String value;

Also avoid embedding scope information in a variable.  Hierarchy-based naming suggests that a class
is too complex and should be broken apart.

    
    // Bad.
    String _value;
    String mValue;

    // Good.
    String value;

### Space pad operators and equals.

    int foo = a + b + 1;

### Be explicit about operator precedence
Don't make your reader open the
[spec](http://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html) to confirm,
if you expect a specific operation ordering, make it obvious with parenthesis.

    
    // Bad.
    return a << 8 * n + 1 | 0xFF;

    // Good.
    return (a << (8 * n) + 1) | 0xFF;

It's even good to be *really* obvious.

    
    if ((values != null) && (10 > values.size())) {
      ...
    }
    
### Absence of "commented out" codes

Delete codes between **"/*"**  and **"*/"** to make codes clear.

	/*
	public Country findCountry(String countryName) {
		for (ArrayList<Country> loopList : countries.values()) {
			for (Country loopCountry:loopList){
				if (loopCountry.countryName.equals(countryName)){
					return loopCountry;
				}
			}
		}
		return null;
	}
	*/

### Javadoc

The more visible a piece of code is (and by extension - the farther away consumers might be),
the more documentation is needed.

    
    // Bad.
    /**
     * This is a class that implements a cache.  It does caching for you.
     */
    class Cache {
      ...
    }

    // Good.
    /**
     * A volatile storage for objects based on a key, which may be invalidated and discarded.
     */
    class Cache {
      ...
    }

#### Documenting a class
Documentation for a class may range from a single sentence
to paragraphs with code examples. Documentation should serve to disambiguate any conceptual
blanks in the API, and make it easier to quickly and *correctly* use your API.
A thorough class doc usually has a one sentence summary and, if necessary,
a more detailed explanation.

    /**
      * This class is the implementation of reinforcement phase in the Risk.
      * <p> The ReinforcementPhase class will allow players add armies to their<br>
      * to their country, based on the countries under control.</p>
      * 
      * @version alpha 0.1
      * @see javax.swing.JFrame
      *
      */
     public class ReinforcementPhase extends JFrame{
        ...
     }

#### Documenting a method
A method doc should tell what the method *does*.  Depending on the argument types, it may
also be important to document input format.

    
    /**
	 * Function to find the Country according to the Country's name
	 * @param countryName Country's name
	 * @return Country that found
	 */
	public Country findCountry(String countryName) {
		for (ArrayList<Country> loopList : countries.values()) {
			for (Country loopCountry:loopList){
				if (loopCountry.countryName.equals(countryName)){
					return loopCountry;
				}
			}
		}
		return null;
	}

When a methid is too complicated to understand, it is better to add descriptions about how the method processes.

	/**
	 * Function to find the Country according to the Country's name
	 * 
	 * <p> 1. loop the country arraylist (nested loop) <p> <br>
	 * <p> 2. compare each country's name with targeted country's name <p><br>
	 * <p> 3. return country if found or null if not <p> <br>
	 * 
	 * @param countryName Country's name
	 * @return Country that found
	 */
	public Country findCountry(String countryName) {
		for (ArrayList<Country> loopList : countries.values()) {
			for (Country loopCountry:loopList){
				if (loopCountry.countryName.equals(countryName)){
					return loopCountry;
				}
			}
		}
		return null;
	}

#### Be professional
We've all encountered frustration when dealing with other libraries, but ranting about it doesn't
do you any favors.  Suppress the expletives and get to the point.

    
    // TODO(Jim): Tuck field validation away in a library.
    try {
      playerId = Integer.parseInt(xml.getName("id"));
    } catch (NumberFormatException e) {
      ...
    }
    
#### Use javadoc features

##### No author tags
Code can change hands numerous times in its lifetime, and quite often the original author of a
source file is irrelevant after several iterations.  We find it's better to trust commit
history and `OWNERS` files to determine ownership of a body of code.

### Imports

#### Import ordering
Imports are grouped by top-level package, with blank lines separating groups.  Static imports are
grouped in the same way, in a section below traditional imports.

    
    import java.*
    import javax.*

    import scala.*

    import com.*

    import net.*

    import org.*

    import com.twitter.*

    import static *

#### No wildcard imports
Wildcard imports make the source of an imported class less clear.  They also tend to hide a high
class [fan-out](http://en.wikipedia.org/wiki/Coupling_(computer_programming)#Module_coupling).<br />
*See also [texas imports](#stay-out-of-texas)*

    
    // Bad.
    import java.awt.event.ActionEvent;
    import java.awt.event.*;

    // Good.
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;


### Use annotations wisely

#### @Nullable
By default - disallow `null`.  When a variable, parameter, or method return value may be `null`,
be explicit about it by marking
[@Nullable](http://code.google.com/p/jsr-305/source/browse/trunk/ri/src/main/java/javax/annotation/Nullable.java?r=24).
This is advisable even for fields/methods with private visibility.

    
    class Database {
      @Nullable private Connection connection;

      @Nullable
      Connection getConnection() {
        return connection;
      }

      void setConnection(@Nullable Connection connection) {
        this.connection = connection;
      }
    }

#### @VisibleForTesting
Sometimes it makes sense to hide members and functions in general, but they may still be required
for good test coverage.  It's usually preferred to make these package-private and tag with
[@VisibleForTesting](http://docs.guava-libraries.googlecode.com/git-history/v11.0.2/javadoc/com/google/common/annotations/VisibleForTesting.html)
to indicate the purpose for visibility.

Constants are a great example of things that are frequently exposed in this way.

    
    //   - The test borrows directly from the same constant.
    class ConfigReader {
      @VisibleForTesting static final String USER_FIELD = "user";

      Config parseConfig(String configData) {
        ...
      }
    }
    public class ConfigReaderTest {
      @Test
      public void testParseConfig() {
        ...
        assertEquals(expectedConfig,
            reader.parseConfig(String.format("{%s: bob}", ConfigReader.USER_FIELD)));
      }
    }

### Use interfaces
Interfaces decouple functionality from implementation, allowing you to use multiple implementations
without changing consumers.
Interfaces are a great way to isolate packages - provide a set of interfaces, and keep your
implementations package private.

Many small interfaces can seem heavyweight, since you end up with a large number of source files.
Consider the pattern below as an alternative.

    
    interface FileFetcher {
      File getFile(String name);

      // All the benefits of an interface, with little source management overhead.
      // This is particularly useful when you only expect one implementation of an interface.
      static class HdfsFileFetcher implements FileFetcher {
        @Override File getFile(String name) {
          ...
        }
      }
    }

#### Leverage or extend existing interfaces
Sometimes an existing interface allows your class to easily 'plug in' to other related classes.

    
    
    class Blobs implements Iterable<byte[]> {
      @Override
      Iterator<byte[]> iterator() {
        ...
      }
    }

Warning - don't bend the definition of an existing interface to make this work.  If the interface
doesn't conceptually apply cleanly, it's best to avoid this.

## Writing testable code
Writing unit tests doesn't have to be hard.  You can make it easy for yourself if you keep
testability in mind while designing your classes and interfaces.

### Fakes and mocks
When testing a class, you often need to provide some kind of canned functionality as a replacement
for real-world behavior.  For example, rather than fetching a row from a real database, you have
a test row that you want to return.  This is most commonly performed with a fake object or a mock
object.  While the difference sounds subtle, mocks have major benefits over fakes.

    
    class RpcClient {
      RpcClient(HttpTransport transport) {
        ...
      }
    }

    
    //   - We can mock the interface and have very fine control over how it is expected to be used.
    public class RpcClientTest {
      private RpcClient client;
      private Transport transport;

      @Before
      public void setUp() {
        transport = EasyMock.createMock(Transport.class);
        client = new RpcClient(transport);
      }

      ...
    }

### Let your callers construct support objects

    
    //   - Testing this class is as easy as using ByteArrayInputStream with a String.
    class ConfigReader {
      private final InputStream configStream;
      ConfigReader(InputStream configStream){
        this.configStream = checkNotNull(configStream);
      }
    }
