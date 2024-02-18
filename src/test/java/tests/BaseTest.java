package tests;

//import adapters.ProjectAPI;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;
//import steps.*;
//import tests.base.TestListener;
import pages.CurrencyPage;
import utils.PropertyReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static java.lang.String.format;
import static org.testng.AssertJUnit.assertEquals;

@Log4j2
//@Listeners(TestListener.class)
public class BaseTest implements ITestListener {
    public CurrencyPage currencyPage;
//    public MainPageSteps mainPageSteps;
//    public SignUpPageSteps signUpPageSteps;
//    public LoginPageSteps loginPageSteps;
//    public ContactUsPageSteps contactUsPageSteps;
//    public TestCasesPageSteps testCasesPageSteps;
//    public ProductsPageSteps productsPageSteps;
//    public ItemPageSteps itemPageSteps;
//    public CartPageSteps cartPageSteps;
//    public ProductsDetailsPageSteps productsDetailsPageSteps;
//    public OrderCheckoutSteps orderCheckoutSteps;
//    public PaymentPageSteps paymentPageSteps;

    String username;
    String password;
    private String testCaseName;


    @BeforeSuite
    public void preconditionBeforeAllTests() {
        log.info("Start getting currency tests....");
        try {
            // Указываем путь к файлу, который необходимо очистить
            String filePathUSD = PropertyReader.getProperty("pathCurrencyTxtUSD");
            String filePathRUR = PropertyReader.getProperty("pathCurrencyTxtRUR");

            // Создаем новый файл с указанным путем
            File fileUSD = new File(filePathUSD);
            File fileRUR = new File(filePathRUR);

            // Проверяем, существует ли файл
            if (fileUSD.exists() && fileRUR.exists()) {
                // Открываем файл в режиме записи, перезаписывая его содержимое
                FileWriter fileWriterUSD = new FileWriter(fileUSD, false);
                FileWriter fileWriterRUR = new FileWriter(fileRUR, false);

                // Закрываем файл
                fileWriterUSD.close();
                fileWriterRUR.close();

                System.out.println("Файлы успешно очищен.");
            } else {
                System.out.println("Файлы не существуют.");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при очистке файла: " + e.getMessage());
        }
//        //clearing folders before starting tests...
//        switch (PropertyReader.getProperty("os")) {
//            case ("windows"):
//                clearFolder(PropertyReader.getProperty("downloadFolderPathWindows"));
//                log.info("Download folder cleared successfully");
//                clearFolder(PropertyReader.getProperty("screenshotFolderWindows"));
//                log.info("Screenshot folder cleared successfully");
//                break;
//            case ("macos"):
//                clearFolder(PropertyReader.getProperty("downloadFolderPathMacOS"));
//                log.info("Download folder cleared successfully");
//                clearFolder(PropertyReader.getProperty("screenshotFolderMacOS"));
//                log.info("Screenshot folder cleared successfully");
//                break;
//        }
    }

    @BeforeMethod
    public void init(ITestResult result) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        // for local launching tests...
        switch (System.getProperty("launchType")) {
            case ("local"):
                WebDriverManager.chromedriver().clearDriverCache().setup();
                Configuration.baseUrl = System.getProperty("URL", PropertyReader.getProperty("base_url"));
                Configuration.headless = Boolean.parseBoolean(PropertyReader.getProperty("headless"));
                Configuration.timeout = 10000;
                //  timeout for full page loading (see on document.readyState in console, 120000 = 2 min)
                Configuration.pageLoadTimeout = 120000;
                Configuration.reportsFolder = "target/screenshots";
                Configuration.savePageSource = false;
                Configuration.downloadsFolder = PropertyReader.getProperty("downloadFolderPathWindows");
                Configuration.browserSize = "1920x1080";
                SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
                ChromeOptions chromeOptions = new ChromeOptions();
                switch (PropertyReader.getProperty("os")) {
                    case ("windows"):
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
//                        chromeOptions.addArguments("--user-data-dir=C:\\Users\\Selecty\\AppData\\Local\\Google\\Chrome\\User Data");
//                        chromeOptions.addArguments("--profile-directory=Default");
                        break;
                    case ("macos"):
                        chromeOptions.addArguments("--user-data-dir=/Volumes/Work/browser_profiles");
                        chromeOptions.addArguments("--profile-directory=Profile 1");
                        break;
                }
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                Configuration.browserCapabilities = capabilities;
                break;
            case ("remote"):
                //for selenoid
                capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                        "enableVNC", true,
                        "enableVideo", Boolean.parseBoolean(System.getProperty("videoTestRecord")),
                        "videoName", format(result.getMethod().getMethodName() + ".mp4"),
                        "enableLog", Boolean.parseBoolean(System.getProperty("logsFromSelenoid")),
                        "logName", format(result.getMethod().getMethodName() + ".log")
                ));
                // capabilities.setCapability("videoScreenSize", "1920x1080");
                Configuration.baseUrl = System.getProperty("URL", PropertyReader.getProperty("base_url"));
                Configuration.browserSize = "1920x1080";
                Configuration.timeout = 10000;
                Configuration.pageLoadTimeout = 120000;
                Configuration.reportsFolder = "target/screenshots";
                SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
                Configuration.remote = "http://localhost:4444/wd/hub";
                Configuration.browserCapabilities = capabilities;
                break;
        }

        // create objects...
        currencyPage = new CurrencyPage();
//        mainPageSteps = new MainPageSteps();
//        signUpPageSteps = new SignUpPageSteps();
//        loginPageSteps = new LoginPageSteps();
//        contactUsPageSteps = new ContactUsPageSteps();
//        testCasesPageSteps = new TestCasesPageSteps();
//        productsPageSteps = new ProductsPageSteps();
//        itemPageSteps = new ItemPageSteps();
//        cartPageSteps = new CartPageSteps();
//        orderCheckoutSteps = new OrderCheckoutSteps();
//        productsDetailsPageSteps = new ProductsDetailsPageSteps();
//        paymentPageSteps = new PaymentPageSteps();
        open();
        getWebDriver().manage().window().maximize();
    }

    @AfterMethod
    public void close() {
        if (Boolean.parseBoolean(PropertyReader.getProperty("api"))) {
        } else if (getWebDriver() != null) {
            getWebDriver().quit();
        }
    }

    @AfterSuite
    public void postConditionAfterAllTests() {
        log.info("The end of performing tests in suite....");
    }

    public void clearFolder(String path) {
        try {
            FileUtils.cleanDirectory(new File(path));
        } catch (IOException e) {
            log.error("Unable to clear the folder: " + e.getMessage());
        }
    }

    public String chooseOS() {
        String filePath = null;
        switch (PropertyReader.getProperty("os")) {
            case ("macos"):
                filePath = "/Volumes/Work/automationExercise/fileForUploading.txt";
                break;
            case ("windows"):
                filePath = "D:\\automationExercise\\fileForUploading.txt";
                break;
        }
        return filePath;
    }

    public void assertScreenshots(String info) {
        String expectedFileName = info + ".png";
        String expectedScreenshotsDir = "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\AutomationExercise\\src\\test\\resources\\expectedScreenshots\\";

        File actualScreenshot = Selenide.screenshot(OutputType.FILE);
        File expectedScreenshot = new File(expectedScreenshotsDir + expectedFileName);
        log.info("expectedScreenshot" + expectedScreenshot);
        if (!expectedScreenshot.exists()) {
            addImgToAllure("actual", actualScreenshot);
            throw new IllegalArgumentException("Can't assert image, because there is no reference. Actual screen can be downloaded from Allure");
        }
        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources(expectedScreenshotsDir + expectedFileName);
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources(actualScreenshot.toPath().toString());

        File resultDestinationDir = new File("C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\AutomationExercise\\build\\screenshotsDiff\\diff_" + expectedFileName);

        log.info("PATH TO SCREENSHOT" + resultDestinationDir);

        ImageComparison imageComparison = new ImageComparison(expectedImage, actualImage, resultDestinationDir);
        ImageComparisonResult result = imageComparison.compareImages();

        if (!result.getImageComparisonState().equals(ImageComparisonState.MATCH)) {
            addImgToAllure("actual", actualScreenshot);
            addImgToAllure("expected", expectedScreenshot);
            addImgToAllure("diff", resultDestinationDir);
        }

        assertEquals(ImageComparisonState.MATCH, result.getImageComparisonState());
    }

    private void addImgToAllure(String name, File file) {
        try {
            byte[] image = Files.readAllBytes(file.toPath());
            saveScreenshots(name, image);
        } catch (IOException e) {
            throw new RuntimeException("Can't read bytes");
        }
    }

    @Attachment(value = "{name}", type = "image/png")
    private static byte[] saveScreenshots(String name, byte[] image) {
        return image;
    }

//    @AfterAll
//    public static void clearingProjects() {
//        Gson gson = new Gson();
//        ProjectAPI projectAPI = new ProjectAPI();
//        Response response = projectAPI.getAllProjects();
//        APIResponse<Project> result = gson.fromJson(response.asString(),
//                new TypeToken<APIResponse<Project>>() {
//                }.getType());
//        List<Entity> entities = result.getResult().entities;
//        List<String> code = entities.stream().map(Entity::getCode).collect(Collectors.toList());
//        for (String eachCode : code) {
//            if (!eachCode.equals("QASE") && !eachCode.equals("SHARELANE") && !eachCode.equals("PFT")) {
//                projectAPI.deleteProjectByCode(eachCode);
//                System.out.printf("Project '%s' has been deleted\n", eachCode);
//                log.info(format("Project '%s' has been deleted\n", eachCode));
//            }
//            else log.info("There is nothing to delete");
//        }
//    }
}