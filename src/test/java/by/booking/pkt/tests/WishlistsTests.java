package by.booking.pkt.tests;

import by.booking.pkt.data.DataSourceFileAnnotation;
import by.booking.pkt.data.FileDataProvider;
import by.booking.pkt.model.TestData;
import by.booking.pkt.model.Wishlist;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class WishlistsTests extends TestBaseWithLogin {

   @Test(groups = {"positive", "smoke"},
           dataProviderClass = FileDataProvider.class, dataProvider = "testDataFromJSON")
   @DataSourceFileAnnotation("src/test/resources/data-for-wishlist-positive.json")
   public void createWishlist(TestData testData) {
      app.account().profileMenu().goToWishlistsPage();
      Wishlist newWishlist = app.wishlistsPage().
              createNewWithName(testData.wishlistName());

      assertThat("List doesn't create!", newWishlist, notNullValue());

      assertThat("New list name doesn't match with default name!",
              app.wishlistsPage().defaultWishlistName(),
              is(newWishlist.getName()));

      assertThat("New wishlist doesn't selected as default!",
              app.wishlistsPage().isSelectAsDefault(newWishlist),
              is(true));

      app.wishlistsPage().deleteWishlist(newWishlist);
   }

   @Test(groups = {"positive"},
           dataProviderClass = FileDataProvider.class, dataProvider = "testDataFromJSON")
   @DataSourceFileAnnotation("src/test/resources/data-for-wishlist-positive.json")
   public void sendLinkOfWishlist(TestData testData) {
      app.account().profileMenu().goToWishlistsPage();
      Wishlist newWishlist = app.wishlistsPage().
              createNewWithName(testData.wishlistName());
      app.wishlistsPage().refreshPage();
      app.wishlistsPage().setAsDefault(newWishlist);
      String urlToSend = app.wishlistsPage().getUrlToSend();
      app.windowsNavigation().goTo(urlToSend);

      assertThat("Sent list name do not match of required!",
              app.wishlistsPage().sentListName(),
              is(newWishlist.getName()));

      app.account().profileMenu().goToWishlistsPage();
      app.wishlistsPage().deleteWishlist(newWishlist);
   }
}
