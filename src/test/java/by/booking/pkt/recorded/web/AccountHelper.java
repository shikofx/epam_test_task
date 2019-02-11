package by.booking.pkt.recorded.web;

import by.booking.pkt.recorded.model.User;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class AccountHelper extends HelperBase {

  public AccountHelper(WebDriver webDriver, WebDriverWait wait, int implicitlyWait) {
    super(webDriver, wait, implicitlyWait);
  }

  public void wishlists() {
    accountMenu();
    clickOn(By.cssSelector("div[class*=wishlists"));
  }

  public void logout() {
    accountMenu();
    clickOn(By.cssSelector("input[name=logout]+input"));
  }

  public AccountHelper accountMenu() {
    showDropdown(By.cssSelector("#current_account span[class=user_name_block]"), By.cssSelector(".profile-menu"), 5);
    return this;
  }


  public void loginAs(User user) throws NullPointerException {
    By logInButton = findLoginButton();
    boolean devidedForm = isDevidedForm();
    clickOn(logInButton);
    WebElement signInForm = null;
    By usernameLocator = By.cssSelector("[name=username]");
    By passwordLocator = By.cssSelector("[name=password]");
    if (devidedForm) {
      System.out.println("Form 1");
      wait.until(visibilityOfElementLocated(By.cssSelector("form.nw-signin")));
      signInForm = webDriver.findElement(By.cssSelector("form.nw-signin"));
      typeText(signInForm, usernameLocator, user.getUsername());
      clickOn(signInForm, By.cssSelector("[type=submit]"));
      wait.until(visibilityOfElementLocated(passwordLocator));
      signInForm = webDriver.findElement(By.cssSelector("form.nw-signin"));
      typeText(signInForm, passwordLocator, user.getPassword());
      clickOn(signInForm, By.cssSelector("[type=submit]"));
    } else {
      System.out.println("Form 2");
      wait.until(visibilityOfElementLocated(By.cssSelector("form[target=log_tar]")));
      signInForm = webDriver.findElement(By.cssSelector("form[target=log_tar]"));
      typeText(signInForm, usernameLocator, user.getUsername());
      typeText(signInForm, passwordLocator, user.getPassword());
      clickOn(signInForm, By.cssSelector("[type=submit]"));
    }
  }

  private boolean isDevidedForm() {
    boolean devided_form = false;
    try{
      devided_form = getAttribute(By.cssSelector("li#current_account a"), "data-command-params").contains("redirect_uri");
    } catch (NullPointerException e) {
      devided_form = false;
    }
    return devided_form;
  }

  @NotNull
  private By findLoginButton() {
    wait.until(presenceOfElementLocated(By.cssSelector("#current_account a")));
    boolean isDropedMenu = false;
    try{
      isDropedMenu = getAttribute(By.cssSelector("#current_account a"), "role").contains("button");
    } catch (NullPointerException e) {
      isDropedMenu = false;
    }
    if(isDropedMenu) {
      showDropdown(By.cssSelector("#current_account a"), By.cssSelector("div.profile-menu"), 5);
      return By.cssSelector("div.profile-menu-auth-item a.js-header-loginAs-link");
    } else {
      return By.cssSelector("#current_account a");
    }
  }
}