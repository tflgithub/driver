package fodel.com.fodelscanner.scanner.api.entity.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fula on 2017/7/24.
 */

public class ResCodCollection extends BaseEntity {

    public List<Invoice> invoices = new ArrayList<>();

    public String generate_invoice_text;

    public String amount;

    public String currency;

    public class Invoice {

        public String bill_number;

        public int amount;

        public List<Order> order_list;

        public class Order {

            public String awb;

            public int amount;
        }
    }
}
