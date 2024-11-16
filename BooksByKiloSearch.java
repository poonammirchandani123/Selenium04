import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BooksByKiloSearch {
    public static void main(String[] args) {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            // Navigate to the Books By Kilo website
            driver.get("https://booksbykilo.in/");

            // Use JavaScript Executor for scrolling
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Set up an explicit wait (optional if needed for specific elements)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Track the page scroll height
            long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
            boolean bookFound = false;

            // Loop to scroll down until the book is found or we reach the end
            while (true) {
                // Scroll down by 1000 pixels
                js.executeScript("window.scrollBy(0, 1000);");

                // Wait briefly for new content to load
                Thread.sleep(2000); // Adjust the delay if necessary

                // Check if any book titles match "The Vile Victorians"
                List<WebElement> bookTitles = driver.findElements(By.cssSelector(".product-title"));

                for (WebElement book : bookTitles) {
                    String title = book.getText();
                    if (title.equalsIgnoreCase("The Vile Victorians")) {
                        System.out.println("Found the book: " + title);

                        // Optionally, click on the book for more details
                        book.click();
                        bookFound = true;
                        break;
                    }
                }

                // If the book is found, break out of the loop
                if (bookFound) break;

                // Calculate the new scroll height
                long newHeight = (long) js.executeScript("return document.body.scrollHeight");

                // Check if we've reached the bottom of the page
                if (newHeight == lastHeight) {
                    System.out.println("Reached the bottom of the page, book not found.");
                    break;
                }

                // Update lastHeight for the next iteration
                lastHeight = newHeight;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}

