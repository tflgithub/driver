package fodel.com.fodelscanner.scanner.base;

/**
 * Created by Happiness on 2017/2/16.
 */
public interface IBaseView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 完成加载
     */
    void dismiss();

    /**
     * 显示错误提示信息
     *
     * @param msg
     */
    void showError(String msg);
}
