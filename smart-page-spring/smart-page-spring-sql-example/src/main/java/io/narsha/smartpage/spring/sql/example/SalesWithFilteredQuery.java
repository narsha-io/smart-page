package io.narsha.smartpage.spring.sql.example;

import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.core.sql.SqlDataTable;
import lombok.Getter;
import lombok.Setter;

/** DTO that will be used by smart-page to apply filter and return data */
@SqlDataTable(
    query =
        """
        select item.id as itemId, item.name as itemName, store.id as storeId, store.name as storeName, count(1) as qty
        from sale
        join item on item.id = sale.item_id
        join store on store.id = sale.store_id
        where :storeName is null or store.name like :storeName
        group by item.id, item.name, store.id, store.name
        """)
@Getter
@Setter
public class SalesWithFilteredQuery {

  private Long itemId;
  private String itemName;
  private Long storeId;
  private String storeName;

  @DataTableProperty(columnName = "qty") // rename
  private Long quantity;
}
