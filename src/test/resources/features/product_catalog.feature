Feature: Product Catalog

  As a customer,
  I want to easily search, filter, and sort products in the catalog
  So that I can find what I need quickly.

  Sally is an online shopper

  Rule: Customers should be able to search for products by name
    Example: The one where Sally searches for an Adjustable Wrench
      Given Sally is on the home page
      When she searches for "Adjustable Wrench"
      Then the "Adjustable Wrench" product should be displayed

    Example: The one where Sally searches for a more general term
      Given Sally is on the home page
      When she searches for "saw"
      Then the following products should be displayed:
        | Product      | Price  |
        | Wood Saw     | $12.18 |
        | Circular Saw | $80.19 |