package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.EcommerceModule;
import fodel.com.fodelscanner.scanner.ui.EcommerceActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = EcommerceModule.class)
public interface EcommerceComponent {

    EcommerceActivity inject(EcommerceActivity activity);
}
