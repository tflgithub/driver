package fodel.com.fodelscanner.scanner.api.entity.bean;

import java.util.List;

import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;

/**
 * Created by fula on 2017/7/31.
 */

public class HomeBean {

    public List<ResMerchant> merchantList;

    public List<ResMenu> menus;

    public HomeBean(List<ResMerchant> merchantList, List<ResMenu> menus) {
        this.merchantList = merchantList;
        this.menus = menus;
    }
}
