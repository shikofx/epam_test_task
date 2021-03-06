package by.booking.pkt.web;

import by.booking.pkt.model.Hotel;
import by.booking.pkt.model.Wishlist;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class WishlistsPageHelper extends HelperBase {

   private static final String REGEX_WISHLIST_NAME = ".+(?=\\s)";

   public WishlistsPageHelper(WebDriver webDriver, WebDriverWait wait, int implicitlyWait) {
      super(webDriver, wait, implicitlyWait);
      PageFactory.initElements(webDriver, this);
   }

   @FindBy(css = "button[class*=js-listview-create-list] span")
   private WebElement createButton;

   @FindBy(css = ".js-listview-header-dropdown span")
   private WebElement dropdownListHeader;

   @FindBy(css = "div[class*=bui-dropdown] span")
   private WebElement defaultWishlist;

   @FindBy(css = "div:last-of-type .listview__lists")
   private WebElement actualWishlistPanel;

   @FindBy(css = "span.listmap__remove_list")
   private WebElement removeWishlistButton;

   @FindBy(css = "div:last-of-type .listview__lists div")
   private List<WebElement> allWishlists;

   @FindBy(css = "div[class=bui-dropdown]+button")
   private WebElement shareWishlistButton;

   @FindBy(css = "div.listview-share")
   private WebElement shareWishlistPanel;

   @FindBy(css = "p[class*=content] input")
   private WebElement shareURLInput;

   @FindBy(css = "div.wl-bui-header h1")
   private WebElement wishlistHeader;

   @FindBy(css = ".bui-carousel__item")
   private List<WebElement> hotelsInWishlist;

   public Wishlist createNewWithName(String listName) {
      List<Wishlist> beforeList = getAllWishlists();
      Alert alertCreateList = alertAfterClick(createButton);
      alertCreateList.sendKeys(listName);
      alertCreateList.accept();
      wait.until((WebDriver d) -> dropdownListHeader.getText().equals(listName));
      List<Wishlist> afterList = getAllWishlists();
      if (afterList.size() == (beforeList.size() + 1)) {
         afterList.removeAll(beforeList);
         Wishlist newList = afterList.get(0);
         if (newList.getName().equals(listName))
            return newList;
      }
      return null;

   }

   public void deleteWishlist(Wishlist list) {
      displayDropDown(defaultWishlist, actualWishlistPanel, 5);
      By removeButtonBy = By.cssSelector("span.listmap__remove_list");
      WebElement buttonToRemoveWishlist = wishlistToWeb(list).findElement(removeButtonBy);
      Alert alertDeleteList = alertAfterClick(buttonToRemoveWishlist);
      alertDeleteList.accept();
   }

   private WebElement wishlistToWeb(Wishlist wishlist) {
      By wishlistBy = By.cssSelector("div[data-id='" + wishlist.getId() + "']");
      return actualWishlistPanel.findElement(wishlistBy);
   }

   public void setAsDefault(Wishlist wishlist) {
      displayDropDown(defaultWishlist, actualWishlistPanel, 5);
      WebElement list = wishlistToWeb(wishlist);
      if (!list.getAttribute("class").contains("selected"))
         list.click();
   }

   public boolean isSelectAsDefault(Wishlist wishlist) {
      displayDropDown(defaultWishlist, actualWishlistPanel, 5);
      WebElement list = wishlistToWeb(wishlist);
      return list.getAttribute("class").contains("selected");
   }

   private List<Wishlist> getAllWishlists() {
      displayDropDown(defaultWishlist, actualWishlistPanel, 5);
      hideDropdown(defaultWishlist, actualWishlistPanel);
      List<Wishlist> wishlists = new ArrayList<>();
      for (WebElement e : allWishlists) {
         Wishlist wl = new Wishlist().
                 withId(getWishlistId(e)).
                 withName(getWishlistName(e));
         wishlists.add(wl);
      }
      return wishlists;
   }

   private String getWishlistName(WebElement e) {
      By wishlistNameBy = By.cssSelector("span.listmap__list_name");
      return textByPattern(REGEX_WISHLIST_NAME, e.findElement(wishlistNameBy).getAttribute("textContent"));
   }

   private int getWishlistId(WebElement e) {
      return Integer.parseInt(e.getAttribute("data-id"));
   }

   public String getUrlToSend() {
      isElementPresentAndVisible(shareWishlistButton);
      displayDropDown(shareWishlistButton, shareWishlistPanel, 5);
      return shareURLInput.getAttribute("defaultValue");
   }

   public String defaultWishlistName() {
      wait.until((WebDriver d) -> defaultWishlist.getTagName());
      return defaultWishlist.getText();
   }

   public String sentListName() {
      wait.until((WebDriver d) -> wishlistHeader.getTagName());
      return wishlistHeader.getText();
   }

   public List<Hotel> getWishlistHotels() {
      List<Hotel> hotels = new ArrayList<>();
      for (WebElement e : hotelsInWishlist) {
         hotels.add(webToHotel(e));
      }
      return hotels;
   }

   private Hotel webToHotel(WebElement item) {
      return new Hotel().withID(item.findElement(By.cssSelector("div")).getAttribute("data-id")).
              withName(item.findElement(By.cssSelector("h1 a")).getText());
      //Для тестового задания опустил считывание всех данных об отеле
   }

   public boolean isCreated(Wishlist newWishlist) {
      for (Wishlist wl : getAllWishlists()) {
         if (wl.equals(newWishlist))
            return true;
      }
      return false;
   }
}
