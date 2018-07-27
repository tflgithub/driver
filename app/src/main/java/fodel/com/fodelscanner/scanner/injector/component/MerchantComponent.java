package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.MerchantModule;
import fodel.com.fodelscanner.scanner.ui.MerchantActionsActivity;
import fodel.com.fodelscanner.scanner.ui.MoreMerchantActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = MerchantModule.class)
public interface MerchantComponent {

    MerchantActionsActivity inject(MerchantActionsActivity activity);

    MoreMerchantActivity inject(MoreMerchantActivity activity);
}
