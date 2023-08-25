package io.narsha.smartpage.spring.sql.example;

import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableProperty;

@DataTable(
    """
        select item.id as itemId, item.name as itemName, store.id as storeId, store.name as storeName, count(1) as qty
        from sale
        join item on item.id = sale.item_id
        join store on store.id = sale.store_id
        group by item.id, item.name, store.id, store.name
        """)
public class Sales {

  private Long itemId;
  private String itemName;
  private Long storeId;
  private String storeName;

  @DataTableProperty(columnName = "qty") // rename
  private Long quantity;

  // TODO LOMBOK
  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public Long getStoreId() {
    return storeId;
  }

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }
}
