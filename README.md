# smart-page

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=narsha-io_smart-page&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=narsha-io_smart-page)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=narsha-io_smart-page&metric=coverage)](https://sonarcloud.io/summary/new_code?id=narsha-io_smart-page)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=narsha-io_smart-page&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=narsha-io_smart-page)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=narsha-io_smart-page&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=narsha-io_smart-page)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=narsha-io_smart-page&metric=bugs)](https://sonarcloud.io/summary/new_code?id=narsha-io_smart-page)

## Why using smart-page ?

As a developer, how many times in your life have you had to code endpoints to list flat data intended for a frontend in order to display a table with filters, sorting and pagination? 🤔

Even using the same technologies, all your projects will have a different implementation. By using hibernate you will use for example the criteria ? or maybe build your hql query based on dozens of criteria (filters and sorting)?

It is with the aim of overcoming this problem that the idea of smart-page was born. Smart-page will take care of the entire dynamic part of your requests in order to save you development time, reduce your code base and standardize your projects.

## How to use smart-page ?

Using smart-page is very simple, you will only need to create a class representative of your table displayed in your frontend and add some annotations.

For example, for a database query you can write the basic query allowing you to retrieve the desired data (without taking into account filters, sorting or pagination) or externalize it in a separate file if it is too long to keep your code readable.

In the case of a document type database like mongodb specifying the collection will suffice.

## How to contribute ? 🤓

Smart-page does not manage your data source yet? Don't worry, smart-page has been designed to be as extensible as possible, both in terms of data source and also in terms of filters. You will find basic filters like equals, in, contains... but you can implement your own filters simply by registering them with the filter registration service.

many updates are planned like the management of mongodb, csv the addition of filter like "great than", "between" ..., but do not be afraid and open your pull request or simply submit your ideas

## License ⚖️
Smart-page is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).