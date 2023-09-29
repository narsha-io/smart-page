package io.narsha.smartpage.spring.mongo.example;

import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO that will be used by smart-page to apply filter and return data */
@DataTable("sales_collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

  private Long itemId;
  private String itemName;
  private Long storeId;
  private String storeName;

  @DataTableProperty(columnName = "qty") // rename
  private Long quantity;
}
