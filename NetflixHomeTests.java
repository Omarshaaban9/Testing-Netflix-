package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class NetflixHomeTests {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup(); // Auto setup ChromeDriver
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void login(String username, String password) {
        driver.get("https://www.netflix.com/login");
        driver.findElement(By.id("id_userLoginId")).sendKeys(username);
        driver.findElement(By.id("id_password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Test
    public void TC01_verifyHomePageDisplaysRecommendations() {
        login("testuser@example.com", "password123");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".recommendations")));
        WebElement recommendations = driver.findElement(By.cssSelector(".recommendations"));
        assertTrue(recommendations.isDisplayed(), "Recommendations section should be visible");
    }

    @Test
    public void TC02_verifyHomepageNoPreferences() {
        login("noprefs@example.com", "noprefpass");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".browse-content")));
        WebElement popularContent = driver.findElement(By.xpath("//*[contains(text(),'Popular') or contains(text(),'Trending')]"));
        assertTrue(popularContent.isDisplayed(), "Popular or Trending section should be visible for users with no preferences");
    }

    @Test
    public void TC03_verifyBrowsingByGenre() {
        login("testuser@example.com", "password123");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".genre-dropdown"))).click();
        driver.findElement(By.xpath("//a[text()='Action']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".genre-content")));
        WebElement genreContent = driver.findElement(By.cssSelector(".genre-content"));
        assertTrue(genreContent.isDisplayed(), "Genre-specific content should be visible after selecting a genre");
    }
}
