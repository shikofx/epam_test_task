
package by.booking.pkt.web;

import by.booking.pkt.model.Hotel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;


public class ResultsPageHelper extends HelperBase {

   private static final String REGEX_PRICE = "[,.\\s\\d]+\\d";
   private static final String REGEX_PRICE_WITHOUT_STRONG = "\\d+\\w+\\d+.*\\n";
   private static final String REGEX_STARS_COUNT = "\\d";
   private static final String REGEX_DISTANCE_MEASURE = "\\s.*?\\s";
   private static final String REGEX_DISTANCE_VALUE = "[\\,\\d]+";

   public ResultsPageHelper(WebDriver webDriver, WebDriverWait wait, int implicitlyWait) {
      super(webDriver, wait, implicitlyWait);
      PageFactory.initElements(webDriver, this);
   }

   @FindBy(css = "div.sr_separator")
   private WebElement separator;

   @FindBy(xpath = "//*/div[contains(@class,'sr_separator')]/preceding-sibling::div[@data-hotelid]")
   private List<WebElement> allResultsBeforeSeparator;

   @FindBy(css = "[data-hotelid]")
   private List<WebElement> allResults;

   @FindBy(css = "[data-hotelid]")
   private WebElement firstHotelItem;

   @FindBy(css = "#sortbar_dropdown_button")
   private WebElement additionalSortsButton;

   @FindBy(css = "#sortbar_dropdown_options")
   private WebElement additionalSortsPanel;

   @FindBy(css = "li.sort_distance_from_search")
   private WebElement initSortByDistance;

   @FindBy(css = "li.sort_price")
   private WebElement initSortByPrice;

//  public void goTo(int resultNumber) {
//    WebElement item = webDriver.findElement(By.cssSelector("#hotellist_inner>.sr_item:nth-of-type(" + resultNumber + ")"));
//    item.findElement(By.cssSelector(".sr-cta-button-row")).click();
//  }

   public void goToHotelPage(Hotel hotel) {
      hotelToElement(hotel).findElement(By.cssSelector(".sr_cta_button")).click();
   }

   private WebElement hotelToElement(Hotel hotel) {
      By hotelBy = By.cssSelector("div.sr_item[data-hotelid='" + hotel.getId() + "']");
      return webDriver.findElement(hotelBy);
   }


   private List<WebElement> getAllResults() {
      if (isElementPresent(separator, 0)) {
         return allResultsBeforeSeparator;
      }
      return allResults;
   }

   public Hotel getFirstHotel() {
      return resultToHotel(firstHotelItem);
   }

   public List<Hotel> availableHotels() {
      List<Hotel> hotels = new ArrayList<>();
      int i = 0;
      for (WebElement e : getOnlyAvailable()) {
         hotels.add(resultToHotel(e));
      }
      return hotels;
   }

   private Hotel resultToHotel(WebElement item) {
      return new Hotel().
              withID(getID(item)).
              withName(getHotelName(item)).
              withDistance(distanceToDest(item)).
              withStars(getStarsCount(item)).
              withTotalPrice(getTotalPrice(item));
   }

   private String getID(WebElement item) {
      return item.getAttribute("data-hotelid");

   }

   private double distanceToDest(WebElement item) {
      double distance = 0;
      By distanceBy = By.cssSelector("span.distfromdest");
      if (isElementPresentNoWait(item, distanceBy)) {
         distance = Double.parseDouble(textByPatternWithout(REGEX_DISTANCE_VALUE, "\\s",
                 item.findElement(distanceBy).getText()).replace(',', '.'));
         String measure = textByPatternWithout(REGEX_DISTANCE_MEASURE, "\\s",
                 item.findElement(distanceBy).getText());
         if (measure.length() > 1) {
            distance = 1000 * distance;
         }
      }
      return distance;
   }

   private int getStarsCount(WebElement item) {
      int actualStars = 0;
      By starsBy = By.cssSelector("svg[class*=stars]");
      if (this.isElementPresentNoWait(item, starsBy)) {
         actualStars = Integer.parseInt(textByPatternWithout(REGEX_STARS_COUNT, "\\s",
                 item.findElement(starsBy).getAttribute("class")));
      }
      return actualStars;
   }

   private int getTotalPrice(WebElement item) {
      By priceInDivBy = By.cssSelector("div.totalPrice");
      By priceInStrongBy = By.cssSelector("strong.price");
      if (isElementPresentNoWait(item, priceInStrongBy)) {
         return Integer.parseInt(textByPatternWithout(REGEX_PRICE,
                 "[\\D]", item.findElement(priceInStrongBy).getText()));
      } else if (isElementPresentNoWait(item, priceInDivBy)) {
         return Integer.parseInt(textByPatternWithout(REGEX_PRICE_WITHOUT_STRONG,
                 "[\\D]", item.findElement(priceInDivBy).getAttribute("outerText")));
      }
      return 0;
   }


   private String getHotelName(WebElement item) {
      By hotelNameBy = By.cssSelector("span.sr-hotel__name");
      return (item.findElement(hotelNameBy).getText());
   }

   private List<WebElement> getOnlyAvailable() {
      List<WebElement> allResults = getAllResults();
      List<WebElement> availableResults = new ArrayList<>();
      for (WebElement e : allResults) {
         if (!e.getAttribute("className").contains("soldout_property")) {
            availableResults.add(e);
         }
      }
      return availableResults;
   }

   public void initSortByPrice() {
      initSortByPrice.click();
      refreshPage();
   }


   public void initSortByDistance() {
      displayDropDown(additionalSortsButton, additionalSortsPanel, 5);
      initSortByDistance.findElement(By.cssSelector("a")).click();
      refreshPage();
   }

   public String checkSortByDistance(List<Hotel> hotels) {
      String checker = "sorted";
      for (int i = 1; i < hotels.size(); i++) {
         String currentHotelName = hotels.get(i).getName();
         double currentDistance = hotels.get(i).getDistance();
         String previousHotelName = hotels.get(i - 1).getName();
         double previousDistance = hotels.get(i - 1).getDistance();
         if (currentDistance < previousDistance) {
            checker = "The distance to '" + currentHotelName + "' <" + currentDistance +
                    "> less than the distance to " + previousHotelName + " <" + previousDistance + '>';
            return checker;
         }
      }
      return checker;
   }

   public String checkSortByPrice(List<Hotel> hotels) {
      String checker = "sorted";
      for (int i = 1; i < hotels.size(); i++) {
         String currentHotel = hotels.get(i).getName();
         int currentPrice = hotels.get(i).getTotalPrice();
         String previousHotel = hotels.get(i - 1).getName();
         int previousPrice = hotels.get(i - 1).getTotalPrice();
         if (currentPrice < previousPrice) {
            checker = "The total price of '" + currentHotel + "' <" + currentPrice +
                    "> less than total price of '" + previousHotel + "' <" + previousPrice + '>';
            return checker;
         }
      }
      return checker;
   }
}
