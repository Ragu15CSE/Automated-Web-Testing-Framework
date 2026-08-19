# BDD Cucumber Automation Framework

A production-ready, highly modular **Behavior Driven Development (BDD)** test automation framework built with **Java 17/21**, **Selenium WebDriver 4**, **Cucumber 7**, **TestNG**, **Apache POI (Excel reader)**, and **ExtentReports 5**.

---

## 📁 Framework Directory Structure

```
demo/
├── pom.xml                                      # Maven dependencies & build configuration
├── testng.xml                                   # TestNG suite configuration for parallel execution
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/automation/framework/
│   │           ├── base/
│   │           │   ├── DriverFactory.java       # ThreadLocal WebDriver manager (Chrome, Firefox, Edge, Headless)
│   │           │   └── BaseTest.java            # Test setup, timeouts & lifecycle teardown
│   │           ├── constants/
│   │           │   └── FrameworkConstants.java  # Centralized timeouts, paths, default settings
│   │           ├── controls/
│   │           │   └── CommonControls.java      # Reusable WebDriver wrappers (Waits, Clicks, JS, Select, Frames, Alerts)
│   │           └── library/
│   │               ├── ConfigReader.java        # Property file reader with CLI overrides
│   │               ├── ExcelDataReader.java     # Apache POI Excel data provider (sheet to Map/List)
│   │               ├── LoggerUtil.java          # Log4j2 centralized logging wrapper
│   │               └── ScreenshotUtil.java      # Capture screenshots as Base64/File
│   └── test/
│       ├── java/
│       │   └── com/automation/framework/
│       │       ├── pages/                       # Page Object Model (POM)
│       │       │   ├── BasePage.java            # Base page wrapping CommonControls & driver
│       │       │   ├── LoginPage.java           # Login page elements (By locators) and actions
│       │       │   └── DashboardPage.java       # Products dashboard page elements & actions
│       │       ├── stepdefinitions/             # Cucumber Step Definitions
│       │       │   ├── LoginSteps.java          # Step implementations for Login.feature
│       │       │   └── DashboardSteps.java      # Step implementations for Dashboard.feature
│       │       ├── hooks/
│       │       │   └── Hooks.java               # @Before, @After, @AfterStep (screenshot on failure)
│       │       └── runners/                     # Cucumber Test Runners
│       │           ├── TestRunner.java          # Main TestNG runner with parallel scenario support
│       │           └── FailedTestRunner.java    # Re-runs failed scenarios from rerun.txt
│       └── resources/
│           ├── config/
│           │   ├── config.properties            # Environment, browser, URL, timeouts, and credentials
│           │   ├── extent.properties            # ExtentReports adapter configuration
│           │   └── extent-config.xml            # Custom Extent HTML report styling
│           ├── features/
│           │   ├── Login.feature                # Scenarios: Positive, Negative, Scenario Outline, DataTables, ExcelData
│           │   └── Dashboard.feature            # Scenarios: Products listing, Navigation, Logout
│           ├── log4j2.xml                       # Log4j2 console and rolling file appenders
│           └── testdata/
│               ├── TestData.xlsx                # Sample Excel workbook (LoginData sheet)
│               └── testdata.properties          # Supplementary key-value test data
```

---

## 🚀 Key Framework Features

1. **Thread-Safe Parallel Execution**: `DriverFactory` uses `ThreadLocal<WebDriver>` for concurrent scenario execution across multiple browser instances.
2. **Page Object Model (POM)**: Complete separation of page element locators (`By`) and business interaction methods from step definitions.
3. **Common Controls**: Built-in explicit synchronization (`waitForVisibility`, `waitForClickability`), JavaScript fallbacks, dropdown selections, alert handling, and frame switching.
4. **Excel Data-Driven Testing**: `ExcelDataReader` leverages Apache POI to parse test datasets from `.xlsx` files into dynamic `List<Map<String, String>>`.
5. **Cucumber BDD Support**:
   - `Scenario` & `Scenario Outline` with `Examples` tables
   - Multi-column `DataTable` handling
   - `Background` step reusability
   - Categorization via tags (`@Smoke`, `@Regression`, `@Negative`, `@ExcelData`)
6. **Automatic Screenshot Capture on Failure**: `Hooks.java` captures screenshots on failed steps and attaches them directly into Cucumber HTML and ExtentReports.
7. **Interactive ExtentReports**: Spark HTML dashboard report configured via `extent.properties` and `extent-config.xml`.
8. **Failed Test Reruns**: Automatically creates `target/rerun.txt` to execute only failed tests using `FailedTestRunner.java`.

---

## ⚙️ Configuration & Execution

### 1. Configure Settings (`src/test/resources/config/config.properties`)
```properties
url=https://www.saucedemo.com/
browser=chrome
headless=false
implicit.wait=10
explicit.wait=20
```

### 2. Run Tests via Maven
```bash
# Run all tests configured in TestRunner
mvn clean test

# Run with specific browser override
mvn clean test -Dbrowser=firefox

# Run in Headless mode
mvn clean test -Dheadless=true

# Run specific Cucumber tags
mvn clean test -Dcucumber.filter.tags="@Smoke"
```

### 3. Re-run Failed Scenarios
```bash
mvn test -Dtest=FailedTestRunner
```

---

## 📊 Test Reports

After test execution, reports are generated at:
- **Extent Report Dashboard**: `test-output/ExtentReport/SparkReport.html`
- **Cucumber HTML Report**: `target/cucumber-reports/cucumber-html-report.html`
- **Execution Logs**: `target/logs/automation.log`
- **Failed Screenshots**: `test-output/screenshots/`
