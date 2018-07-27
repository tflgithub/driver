package fodel.com.fodelscanner.scanner.api.entity.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fula on 2017/7/19.
 */

public class ResFinalize extends BaseEntity {

    public List<Awb> success = new ArrayList<>();

    public List<Awb> remaining = new ArrayList<>();

    public List<Awb> failure = new ArrayList<>();

    public List<Fail> fail;

    public class Awb {
        public String awb;
    }

    public class Fail {
        public String error;
        public String awb;
    }
}
