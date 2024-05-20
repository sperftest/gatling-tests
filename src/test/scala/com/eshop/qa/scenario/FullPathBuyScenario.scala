package com.eshop.qa.scenario

import com.eshop.qa.BaseSimulation
import com.eshop.qa.models.{CartPage, CategoriesPage, CheckoutPage, HomePage}
import io.gatling.core.Predef._
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}

case class FullPathBuyScenario() extends BaseSimulation{

  val scn: ScenarioBuilder = scenario(getClass.getSimpleName)
    .exec(HomePage.getIndexPage("01_getIndexPage"))
    .exec(CategoriesPage.getCategories("02_getCategories"))
    .exec(CategoriesPage.getCategoryProducts("03_getCategoryProducts"))
    .exec(CartPage.addItemToCart("04_addItemToCart"))
    .exec(CategoriesPage.getCategoryProducts("05_getCategoryProducts"))
    .exec(CartPage.addItemToCart("06_addItemToCart"))
    .exec(CartPage.getCartInfo("07_getCartInfo"))
    .exec(CartPage.updateQuantity("08_updateQuantity"))
    .exec(CartPage.getCartInfo("09_getCartInfo"))
    .exec(CartPage.removeItemFromCart("10_removeItemFromCart"))
    .exec(CheckoutPage.submitPurchase("11_submitPurchase"))

  val populationBuilder: PopulationBuilder = setInjectionProfiling(scn)
    .protocols(httpConf)

  setUp(
    populationBuilder
  )
}