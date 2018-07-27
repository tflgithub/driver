package fodel.com.fodelscanner.scanner.api.cache.bean.adapter;

import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.api.entity.response.ResUser;
import fodel.com.fodelscanner.utils.DateUtil;

/**
 * Created by fula on 2017/7/24.
 */

public class UserAdapter {

    ResUser resUser;

    User user;

    public UserAdapter(ResUser resUser) {
        this.resUser = resUser;
        user = new User();
    }

    public User get() {
        user.firstName = resUser.firstName;
        user.lastName = resUser.lastName;
        user.portraitUrl = resUser.portrait;
        user.phoneNumber = resUser.phoneNumber;
        user.createTime = DateUtil.getCurrentTime();
        return user;
    }
}
