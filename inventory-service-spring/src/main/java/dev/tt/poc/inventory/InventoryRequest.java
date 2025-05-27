package dev.tt.poc.inventory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryRequest implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String itemId;
	private int quantity;
	private String status;
}
